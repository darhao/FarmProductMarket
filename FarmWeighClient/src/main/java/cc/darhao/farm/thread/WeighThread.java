package cc.darhao.farm.thread;

import java.util.Date;
import java.util.Random;

import cc.darhao.dautils.api.DateUtil;
import cc.darhao.farm.controller.MainController;
import cc.darhao.farm.entity.ProductionProperties;
import cc.darhao.farm.entity.ProductionVO;
import javafx.collections.ObservableList;

/**
 * 上称（模拟）线程
 * <br>
 * <b>2018年4月18日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class WeighThread extends Thread {
	
	private String name;
	
	private String type;
	
	private ObservableList<ProductionProperties> productionProperties;
	
	
	public WeighThread(String name, String type, ObservableList<ProductionProperties> productionProperties) {
		this.type = type;
		this.name = name;
		this.productionProperties = productionProperties;
	}
	
	
	@Override
	public void run() {
		while(true) {
			//判断是否继续
			if(!MainController.isWeighing) {
				break;
			}
			//获取上称速度
			double wait = 1010 - MainController.speed * 10;
			try {
				sleep((long) wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//随机生成并添加到列表
			synchronized (productionProperties) {
				productionProperties.add(new ProductionProperties(randomData()));
			}
		}
	}
	
	
	private ProductionVO randomData() {
		ProductionVO productionVO = new ProductionVO();
		Date now = new Date();
		productionVO.setCreateTimeString(DateUtil.yyyyMMddHHmmss(now));
		productionVO.setName(name);
		productionVO.setTypeName(type);
		Random random = new Random(now.getTime());
		productionVO.setWeight(Math.abs(random.nextInt() % 100) + "." + Math.abs(random.nextInt() % 10) + "kg");
		return productionVO;
	}
	
}
