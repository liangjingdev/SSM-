package cn.jing.campusShop.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jing.campusShop.entity.Award;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.service.AwardService;
import cn.jing.campusShop.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "frontend")
public class AwardDetailController {

	@Autowired
	private AwardService awardService;

	/**
	 * function:根据奖品id获取奖品详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listawarddetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listAwardDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台传递过来的awardId
		long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		Award award = null;
		// 空值判断
		if (awardId != -1) {
			// 根据awardId获取奖品信息
			award = awardService.getAwardById(awardId);
			modelMap.put("award", award);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}
}
