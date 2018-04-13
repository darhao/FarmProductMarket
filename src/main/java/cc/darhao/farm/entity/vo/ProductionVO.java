package cc.darhao.farm.entity.vo;

import cc.darhao.farm.entity.Production;

public class ProductionVO extends Production {
	
	private String createTimeString;
	
	private String supplierName;
	
	private String typeName;

	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}
}
