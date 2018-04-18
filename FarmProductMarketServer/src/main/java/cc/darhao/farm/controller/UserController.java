package cc.darhao.farm.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cc.darhao.farm.annoation.Open;
import cc.darhao.farm.entity.User;
import cc.darhao.farm.service.UserService;
import cc.darhao.farm.util.ResultUtil;

/**
 * 用户控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Open
	@RequestMapping("/goLogin")
	public ModelAndView goLogin() {
		return new ModelAndView("user/goLogin");
	}
	
	@Open
	@ResponseBody
	@RequestMapping("/login")
	public ResultUtil login(String name, String password, HttpSession session) {
		User user = userService.login(name, password);
		if(user == null) {
			session.setAttribute("logined", null);
			return ResultUtil.failed();
		}else{
			session.setAttribute("logined", user);
			return ResultUtil.succeed();
		}
	}
}
