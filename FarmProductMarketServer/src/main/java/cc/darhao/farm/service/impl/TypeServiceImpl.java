package cc.darhao.farm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.darhao.farm.entity.Type;
import cc.darhao.farm.mapper.TypeMapper;
import cc.darhao.farm.service.TypeService;

/**
 * 农产品类型服务接口实现类
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Service
public class TypeServiceImpl implements TypeService {

	@Autowired
	private TypeMapper typeMapper;
	
	
	@Override
	public List<Type> list() {
		return typeMapper.selectByExample(null);
	}

}
