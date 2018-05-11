package cc.darhao.farm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cc.darhao.farm.annoation.Open;
import cc.darhao.farm.entity.vo.Page;
import cc.darhao.farm.entity.vo.ProductionVO;
import cc.darhao.farm.service.ProductionService;
import cc.darhao.farm.util.ResultUtil;

/**
 * 农产品控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
@Controller
@RequestMapping("/production")
public class ProductionController {
	
	@Autowired
	private ProductionService productionService;
	
	
	@Open
	@RequestMapping("/goHome")
	public ModelAndView goHome() {
		return new ModelAndView("production/goHome");
	}
	
	
	@ResponseBody
	@RequestMapping("/offline")
	public ResultUtil offline(String ids) {
		String[] idss = ids.split(",");
		if(idss.length == 0) {
			ResultUtil.failed("无法找到逗号分隔符");
			return ResultUtil.failed();
		}
		List<Integer> idList = new ArrayList<Integer>();
		try {
			for (String id : idss) {
				idList.add(Integer.parseInt(id));
			}
		} catch (NumberFormatException e) {
			ResultUtil.failed("id无法转换成数字");
			return ResultUtil.failed();
		}
		try {
			int result = productionService.offline(idList);
			if(result == 1) {
				return ResultUtil.succeed();
			}else{
				ResultUtil.failed("只有管理员才能下架非自己上架的农产品");
				return ResultUtil.failed("failed_access_denied");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultUtil.failed();
		}
	}
	
	
	@Open
	@ResponseBody
	@RequestMapping("/list")
	public Page<ProductionVO> list(String key, Integer page, String orderBy) {
		if(page == null || page < 0) {
			ResultUtil.failed("错误：页数为格式错误");
			return null;
		}
		try {
			return productionService.list(key, page, orderBy);
		} catch (Exception e) {
			ResultUtil.failed("出错", e);
			return null;
		}
	}
	
	
	@Open
	@ResponseBody
	@RequestMapping("/online")
	public ResultUtil online(String data) {
		List<ProductionVO> dataList = JSONObject.parseArray(data, ProductionVO.class);
		if(dataList != null) {
			if (productionService.storage(dataList) == 1) {
				return ResultUtil.succeed();
			}else {
				return ResultUtil.failed();
			}
		}
		return ResultUtil.failed("参数JSON格式不对");
	}
}
