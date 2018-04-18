package cc.darhao.farm.entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 农产品实体类属性集
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class ProductionProperties {

	private SimpleStringProperty name;
	
	private SimpleStringProperty type;
	
	private SimpleStringProperty weight;
	
	private SimpleStringProperty createTime;
	
	private SimpleStringProperty isOnline;
	

	public ProductionProperties(ProductionVO productionVO) {
		name = new SimpleStringProperty(productionVO.getName());
		type = new SimpleStringProperty(productionVO.getTypeName());
		weight = new SimpleStringProperty(productionVO.getWeight());
		createTime = new SimpleStringProperty(productionVO.getCreateTimeString());
		isOnline = new SimpleStringProperty("否");
	}


	public String getName() {
		return name.get();
	}

	public String getType() {
		return type.get();
	}

	public String getWeight() {
		return weight.get();
	}

	public String getCreateTime() {
		return createTime.get();
	}

	public String getIsOnline() {
		return isOnline.get();
	}
	
	public void settIsOnline(String isOnline) {
		this.isOnline.set(isOnline);
	}
	
}
