package cc.darhao.farm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cc.darhao.farm.annoation.Open;
import cc.darhao.farm.entity.Type;
import cc.darhao.farm.service.TypeService;
import cc.darhao.farm.util.ResultUtil;

/**
 * 农产品类型控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/type")
public class TypeController {
	
	@Autowired
	private TypeService typeService;
	
	
	@Open
	@ResponseBody
	@RequestMapping("/list")
	public List<Type> list() {
		try {
			return typeService.list();
		} catch (Exception e) {
			ResultUtil.failed("出错", e);
			return null;
		}
	}
}
