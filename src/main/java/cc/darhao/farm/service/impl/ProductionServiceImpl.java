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
	public List<ProductionVO> list(String key,Integer page, String orderBy) {
		//初始化filler，获取数据
		filler.init();
		
		List<User> users = filler.getUsers();
		
		ProductionExample productionExample = new ProductionExample();
		
		 //排序
		if(orderBy == null || orderBy.equals("")) {
			//默认按时间降序
			productionExample.setOrderByClause("create_time desc");
		}else {
			productionExample.setOrderByClause(orderBy);
		}
		
		try {
			//通配搜索
			if(key != null && !key.equals("")) {
				//匹配供应商
				for (User user : users) {
					if(user.getName().contains(key)) {
						productionExample.or().andSupplierEqualTo(user.getId());
						break;
					}
				}
				//匹配农产品名称
				productionExample.or().andNameEqualTo(key);
				//匹配类型
				productionExample.or().andTypeEqualTo(Integer.parseInt(key));
			}
		} catch (Exception e) {
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
