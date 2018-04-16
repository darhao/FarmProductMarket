package cc.darhao.farm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 根部控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("/production/goHome");
	}
	
}
