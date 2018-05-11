package cc.darhao.farm.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cc.darhao.farm.controller.MainController;
import cc.darhao.farm.entity.ProductionProperties;
import cc.darhao.farm.entity.ProductionVO;
import cc.darhao.farm.util.HttpUtils;
import cc.darhao.farm.util.HttpUtils.MyCallback;
import io.jafka.producer.Producer;
import io.jafka.producer.ProducerConfig;
import io.jafka.producer.StringProducerData;
import io.jafka.producer.serializer.StringEncoder;
import javafx.collections.ObservableList;

/**
 * 发布线程
 * <br>
 * <b>2018年4月18日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class OnlineThread extends Thread {
	
	//0：jafak模式，1：传统模式
	public static int UPDATE_MODE = 1;
	
	//最后一次发布的列表项索引
	private int lastOnlineIndex = 0;
	//最后一次被选中用于发布的节点
	private int lastProducerNode = 0;
	
	//手动发布模式开关
	private CountDownLatch countDownLatch;
	
	//当前登录名
	private String user;
	//农产品列表
	private ObservableList<ProductionProperties> productionProperties;
	
	//Jafka服务器节点列表
	private static final String[] JAFKAS_IP = new String[] { "darhao.cc", "hzu580.cn", "www.ikataomoi.com" };
	//Jafka生产者列表
	private static final List<Producer<String, String>> producers = new ArrayList<Producer<String, String>>();
	//Jafka服务器端口
	private static final int JAFKAS_PORT = 9092;
	//Jafka话题
	private static final String TOPIC = "farmss";
	
	//自动发布模式间隔时间【单位：毫秒】（防止过度消耗cpu）
	private static final long AUTO_MODE_INTERVAL = 10;
	
	
	public OnlineThread(String user, ObservableList<ProductionProperties> productionProperties) {
		this.user = user;
		this.productionProperties = productionProperties;
		//初始化Jafka
		producers.clear();
		for (String ip : JAFKAS_IP) {
			Properties props = new Properties();
	        props.put("broker.list", "0:"+ip +":"+JAFKAS_PORT);
	        props.put("serializer.class", StringEncoder.class.getName());
	        ProducerConfig config = new ProducerConfig(props);
	        Producer<String, String> producer = new Producer<String, String>(config);
			producers.add(producer);
		}
	}
	
	
	@Override
	public void run() {
		while(true) {
			//判断是否继续
			if(!MainController.isLogined) {
				break;
			}
			//获取发布模式
			switch (MainController.onlineMode) {
			case 0:
				try {
					sleep(AUTO_MODE_INTERVAL);
				} catch (InterruptedException e) {
				}
				online();
				break;
			case 1:
				try {
					sleep(MainController.intervalMin * 60 * 1000);
				} catch (InterruptedException e) {
				}
				online();
				break;
			case 2:
				countDownLatch = new CountDownLatch(1);
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
				}
				online();
				break;
			default:
				break;
			}
		}
		close();
	}
	
	
	private void online() {
		//构造发布的消息
        StringProducerData data = new StringProducerData(TOPIC);
		List<ProductionVO> vos = new ArrayList<ProductionVO>();
        //取出未发布元素
        synchronized (productionProperties) {
        	for (int i = lastOnlineIndex; i < productionProperties.size(); i++) {
    			ProductionProperties properties = productionProperties.get(i);
    			//设置农产品列表项目为已发布
    			properties.settIsOnline("是");
    			productionProperties.set(i, properties);
    			//构造发布的子消息
    			ProductionVO productionVO = new ProductionVO();
    			productionVO.setName(properties.getName());
    			productionVO.setTypeName(properties.getType());
    			productionVO.setCreateTimeString(properties.getCreateTime());
    			productionVO.setWeight(properties.getWeight());
    			productionVO.setSupplierName(user);
    			vos.add(productionVO);
    			
    		}
        	//更新索引
			lastOnlineIndex = productionProperties.size();
		}
        
        if(UPDATE_MODE == 0) {
        	for (ProductionVO productionVO : vos) {
        		String json = JSONObject.toJSONString(productionVO);
    			data.add(json);
			}
        	send(data);
        }else {
        	sendInTraditionalWay(vos);
        }
	}

	
	private void sendInTraditionalWay(List<ProductionVO> vos) {
		Map<String, String> postBody = new HashMap<String, String>();
		postBody.put("data", JSONArray.toJSONString(vos));
		HttpUtils.asynRequest(MainController.BASE + "/production/online", postBody, new MyCallback() {
			
			@Override
			public void onSucceed(String data) {
				if(!data.contains("succeed")) {
					System.err.println(data);
				}
			}
			
			@Override
			public void onFailed() {
				System.err.println("调用HTTP上传失败");
			}
		});
	}
	

	private void send(StringProducerData data) {
		//循环发送到一个节点
		lastProducerNode++;
		lastProducerNode %= producers.size();
		int nodeId = lastProducerNode;
		Producer<String, String> producer = producers.get(nodeId);
		try {
			producer.send(data);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("节点无法连接，正在尝试恢复当前节点并开始重新循环发送...");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
			//尝试恢复节点
			recoveryNode(nodeId);
			//重新随机发送
			send(data);
		}
	}


	private void recoveryNode(int nodeId) {
		Properties props = new Properties();
		props.put("broker.list", "0:"+JAFKAS_IP[nodeId] +":"+JAFKAS_PORT);
		props.put("serializer.class", StringEncoder.class.getName());
		ProducerConfig config = new ProducerConfig(props);
		producers.set(nodeId, new Producer<String, String>(config));
	}
	
	
	private void close() {
		for (Producer<String, String> producer : producers) {
			producer.close();
		}
	}
	
}
