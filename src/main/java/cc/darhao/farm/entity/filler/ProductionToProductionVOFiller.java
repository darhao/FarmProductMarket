package cc.darhao.farm.entity.filler;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.darhao.dautils.api.DateUtil;
import cc.darhao.dautils.api.FieldUtil;
import cc.darhao.farm.entity.Production;
import cc.darhao.farm.entity.Type;
import cc.darhao.farm.entity.User;
import cc.darhao.farm.entity.vo.ProductionVO;
import cc.darhao.farm.mapper.TypeMapper;
import cc.darhao.farm.mapper.UserMapper;
import cc.darhao.farm.util.EntityFieldFiller;

/**
 * 农产品实体类显示层填充者
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Component
public class ProductionToProductionVOFiller extends EntityFieldFiller<Production, ProductionVO> {

	@Autowired
	private TypeMapper typeMapper;
	
	private List<Type> types;
	
	@Autowired
	private UserMapper userMapper;
	
	private List<User> users;
	
	
	public void init() {
		types = typeMapper.selectByExample(null);
		users = userMapper.selectByExample(null);
	}
	
	@Override
	public ProductionVO fill(Production production) {
		ProductionVO productionVO = new ProductionVO();
		FieldUtil.copy(production, productionVO);
		for (Type type : types) {
			if(type.getId() == productionVO.getType()) {
				productionVO.setTypeName(type.getName());
				break;
			}
		}
		for (User user : users) {
			if(user.getId() == productionVO.getSupplier()) {
				productionVO.setSupplierName(user.getName());
				break;
			}
		}
		productionVO.setCreateTimeString(DateUtil.toPopularString(productionVO.getCreateTime()));
		return productionVO;
	}
	
	
	@Override
	public Production unfill(ProductionVO productionVO) {
		Production production = new Production();
		FieldUtil.copy(productionVO, production);
		for (Type type : types) {
			if(type.getName().equals(productionVO.getTypeName())) {
				production.setType(type.getId());
				break;
			}
		}
		for (User user : users) {
			if(user.getName().equals(productionVO.getSupplierName())) {
				production.setSupplier(user.getId());
				break;
			}
		}
		try {
			production.setCreateTime(DateUtil.yyyyMMddHHmmss(productionVO.getCreateTimeString()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		production.setIsOffline(false);
		return production;
	}

	
	public List<Type> getTypes() {
		return types;
	}

	public List<User> getUsers() {
		return users;
	}
	
	
}
