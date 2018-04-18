package cc.darhao.farm.timer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cc.darhao.farm.entity.vo.ProductionVO;
import cc.darhao.farm.service.ProductionService;
import io.jafka.api.FetchRequest;
import io.jafka.consumer.SimpleConsumer;
import io.jafka.message.MessageAndOffset;
import io.jafka.utils.Utils;

@Component
public class StorageTimer {

	@Autowired
	private ProductionService productionService;

	//Jafka服务器节点列表
	private static final String[] JAFKAS_IP = new String[] { "darhao.cc", "hzu580.cn", "www.ikataomoi.com" };
	//Jafka消费者列表
	private static final List<SimpleConsumer> consumers = new ArrayList<SimpleConsumer>();
	//Jafka服务器端口
	private static final int JAFKAS_PORT = 9092;
	//Jafka消费信息偏移量
	private static long[] offsets = new long[] { 0, 0, 0 };
	//Jafka话题
	private static final String TOPIC = "farm";

	/**
	 * 每隔N秒消费来自Jafka集群的消息
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void consume(){
		List<String> strings = new ArrayList<String>();
		for (int i = 0; i < consumers.size(); i++) {
			FetchRequest request = new FetchRequest(TOPIC, 0, offsets[i]);
			try {
				for (MessageAndOffset msg : consumers.get(i).fetch(request)) {
					String data = Utils.toString(msg.message.payload(), "UTF-8");
					strings.add(data);
					offsets[i] = msg.offset;
				}
			} catch (IOException e) {
				System.err.println(JAFKAS_IP[i] + "服务无法访问，正在尝试重连...");
				//断线重连
				consumers.set(i, new SimpleConsumer(JAFKAS_IP[i], JAFKAS_PORT));
			}
		}
		storage(strings);
	}

	
	/**
	 * 插入数据库中
	 */
	private void storage(List<String> data) {
		List<ProductionVO> productionVOs = new ArrayList<ProductionVO>();
		for (String string : data) {
			ProductionVO vo = JSONObject.parseObject(string, ProductionVO.class);
			productionVOs.add(vo);
		}
		int result = productionService.storage(productionVOs);
		if(result == 0) {
			System.err.println("农产品插入数据库失败");
		}
	}
	
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void connectJafkas() {
		for (String ip : JAFKAS_IP) {
			consumers.add(new SimpleConsumer(ip, JAFKAS_PORT));
		}
	}

}
