package cc.darhao.farm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.darhao.farm.entity.User;
import cc.darhao.farm.entity.UserExample;
import cc.darhao.farm.mapper.UserMapper;
import cc.darhao.farm.service.UserService;

/**
 * 用户服务层实现类
 * <br>
 * <b>2018年4月11日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User login(String name, String password) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
		List<User> users = userMapper.selectByExample(userExample);
		if(users.size() == 0) {
			return null;
		}else {
			return users.get(0);
		}
	}

}
