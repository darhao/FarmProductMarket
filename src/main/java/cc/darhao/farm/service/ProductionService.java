package cc.darhao.farm.service;

import java.util.List;

import cc.darhao.farm.entity.vo.ProductionVO;

/**
 * 农产品服务接口
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface ProductionService {

	/**
	 * 下架指定农产品
	 * @param idss id数组
	 * @return 1表示成功，0表示没有下架权限
	 */
	int offline(List<Integer> ids);
	
	/**
	 * 根据条件列出农产品
	 * @param name 按农产品名称模糊筛选
	 * @param supplier 按菜商筛选
	 * @param type 按农产品类型筛选
	 * @param page 页数
	 * @param ordBy 可以根据name，supplier，type，create_time(默认)排序，支持asc升序，desc降序
	 * @return 农产品VO列表
	 */
	List<ProductionVO> list(String key, Integer page, String orderBy);
	
}
