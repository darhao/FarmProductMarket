package cc.darhao.farm.service;

import java.util.List;

import cc.darhao.farm.entity.Type;

/**
 * 农产品类型服务接口
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface TypeService {
	
	/**
	 * 列出农产品类型
	 * @return 农产品类型列表
	 */
	List<Type> list();
	
}
