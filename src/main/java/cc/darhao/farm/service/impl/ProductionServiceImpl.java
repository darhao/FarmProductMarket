package cc.darhao.farm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.darhao.farm.entity.Production;
import cc.darhao.farm.entity.ProductionExample;
import cc.darhao.farm.entity.User;
import cc.darhao.farm.entity.filler.ProductionToProductionVOFiller;
import cc.darhao.farm.entity.vo.ProductionVO;
import cc.darhao.farm.interceptor.LoginInterceptor;
import cc.darhao.farm.mapper.ProductionMapper;
import cc.darhao.farm.service.ProductionService;

/**
 * 农产品服务层接口实现类
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Service
public class ProductionServiceImpl implements ProductionService {

	/**
	 * 每页数据条数
	 */
	private final static int pageSize = 20;
	
	@Autowired
	private ProductionMapper productionMapper;
	
	@Autowired
	private ProductionToProductionVOFiller filler;
	
	
	@Override
	public int offline(List<Integer> ids) {
		ProductionExample productionExample = new ProductionExample();
		productionExample.createCriteria().andIdIn(ids);
		List<Production> productions = productionMapper.selectByExample(productionExample);
		
		//获取当前登录用户
		User user = LoginInterceptor.getLoginUser();
		
		for (Production production : productions) {
			//如果是管理员或者是当前登录的用户
			if(user.getIsAdmin() == true || production.getSupplier() == user.getId()) {
				production.setIsOffline(true);
				productionMapper.updateByPrimaryKey(production);
			}else {
				return 0;
			}
		}
		
		return 1;
	}

	
	@Override
	public List<ProductionVO> list(String name, String supplier, String type, Integer page, String orderBy) {
		//初始化filler，获取数据
		filler.init();
		
		List<User> users = filler.getUsers();
		
		ProductionExample productionExample = new ProductionExample();
		ProductionExample.Criteria productionCriteria = productionExample.createCriteria();
		
		 //排序
		if(orderBy == null) {
			//默认按时间降序
			productionExample.setOrderByClause("create_time desc");
		}else {
			productionExample.setOrderByClause(orderBy);
		}
		
		 //筛选品名
		if(name != null && !name.equals("")) {
			productionCriteria.andNameLike(name);
		}
		
		 //筛选供应商名
		if(supplier != null && !supplier.equals("")) {
			for (User user : users) {
				if(user.getName().contains(supplier)) {
					productionCriteria.andSupplierEqualTo(user.getId());
					break;
				}
			}
		}
		
		 //筛选类型
		if(type != null && !type.equals("")) {
			productionCriteria.andTypeEqualTo(Integer.parseInt(type));
		}
		
		//分页
		List<Production> productions = productionMapper.selectByExample(productionExample);
		int lastPageNo = productions.size() / pageSize;
		int lastPageItemNum = productions.size() % pageSize;
		//所请求的页码大于总页数
		if(page > lastPageNo) {
			return null;
		//所请求的页码是最后一页
		}else if(page == lastPageNo) {
			productions = productions.subList(page * pageSize, page * pageSize + lastPageItemNum);
		//其他情况	
		}else {
			productions = productions.subList(page * pageSize, (page + 1) * pageSize);
		}
		
		//填充
		return filler.fill(productions);
	}

}
