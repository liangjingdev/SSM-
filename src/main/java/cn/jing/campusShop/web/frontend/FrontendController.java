package cn.jing.campusShop.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * function:前端展示系统路由
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping(value = "/frontend")
public class FrontendController {

	/**
	 * function:首页展示页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}

	/**
	 * function:店铺列表页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String showShopList() {
		return "frontend/shoplist";
	}

	/**
	 * function:店铺详情页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String showShopDetail() {
		return "frontend/shopdetail";
	}

	/**
	 * function:商品详情页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	private String showProductDetail() {
		return "frontend/productdetail";
	}

	/**
	 * function:奖品详情页路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/awarddetail", method = RequestMethod.GET)
	private String showAwardDetail() {
		return "frontend/awarddetail";
	}

	@RequestMapping(value = "/myrecord", method = RequestMethod.GET)
	private String showMyRecord() {
		// 消费记录列表路由
		return "frontend/myrecord";
	}

	@RequestMapping(value = "/mypoint", method = RequestMethod.GET)
	private String showMyPoint() {
		// 用户各店铺积分信息页路由
		return "frontend/mypoint";
	}

	@RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
	private String showMyAwardDetail() {
		// 奖品详情页路由
		return "frontend/myawarddetail";
	}

	@RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
	private String showPointRecord() {
		// 奖品兑换列表页路由
		return "frontend/pointrecord";
	}

	@RequestMapping(value = "/awardlist", method = RequestMethod.GET)
	private String showAwardList() {
		// 店铺的奖品列表页路由
		return "frontend/awardlist";
	}
}
