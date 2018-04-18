package cc.darhao.farm.entity.vo;

import java.util.List;

/**
 * 分页
 * <br>
 * <b>2018年4月15日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class Page<T> {
	
	/**
	 * 页面数据
	 */
	private List<T> data;
	
	/**
	 * 当前页面
	 */
	private int pageNo;
	
	/**
	 * 页面总数
	 */
	private int pageSum;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPageSum() {
		return pageSum;
	}

	public void setPageSum(int pageSum) {
		this.pageSum = pageSum;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
}
