package cc.darhao.farm.service;

import cc.darhao.farm.entity.User;

/**
 * 用户服务接口
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public interface UserService {

	/**
	 * @param name 用户名
	 * @param password 密码
	 * @return 成功返回用户名，null表示登录失败
	 */
	User login(String name, String password);
	
}
