package cn.jing.campusShop.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * function:店家系统开发路由 (主要用来解析路由并转发到相应的html中)
 * 
 * @author liangjing
 *
 */
@Controller
@RequestMapping(value = "/shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {

	/**
	 * function:店铺注册页面、店铺信息修改页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopoperation", method = RequestMethod.GET)
	public String shopOperation() {
		return "shop/shopoperation";
	}

	/**
	 * function:店铺列表展示页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	public String shopList() {
		return "shop/shoplist";
	}

	/**
	 * function:店铺管理页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}

	/**
	 * function:店铺的商品类别管理页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
	public String productCategoryManage() {
		return "shop/productcategorymanagement";
	}

	/**
	 * function:商品添加页面、商品信息修改页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		// 转发至商品添加/编辑页面
		return "shop/productoperation";
	}

	/**
	 * function:商品管理页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		// 转发至商品管理页面
		return "shop/productmanagement";
	}

	/**
	 * function:店铺授权页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopauthmanagement")
	public String shopAuthManagement() {
		return "shop/shopauthmanagement";
	}

	/**
	 * function:授权信息修改页面路由
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopauthedit")
	public String shopAuthEdit() {
		return "shop/shopauthedit";
	}

	@RequestMapping(value = "/operationsuccess", method = RequestMethod.GET)
	private String operationSuccess() {
		// 转发至操作成功的页面
		return "shop/operatinsuccess";
	}

	@RequestMapping(value = "/operationfail", method = RequestMethod.GET)
	private String operationFail() {
		// 转发至操作失败的页面
		return "shop/operationfail";
	}

	@RequestMapping(value = "/productbuycheck", method = RequestMethod.GET)
	private String productBuyCheck() {
		// 转发至店铺的消费记录的页面
		return "shop/productbuycheck";
	}

	@RequestMapping(value = "/usershopcheck", method = RequestMethod.GET)
	private String userShopCheck() {
		// 店铺用户积分统计页面路由
		return "shop/usershopcheck";
	}

	@RequestMapping(value = "/awarddelivercheck", method = RequestMethod.GET)
	private String awardDeliverCheck() {
		// 店铺顾客积分兑换奖品列表页面路由
		return "shop/awarddelivercheck";
	}

	@RequestMapping(value = "/awardmanagement", method = RequestMethod.GET)
	private String awardManagement() {
		// 奖品管理页面路由
		return "shop/awardmanagement";
	}

	@RequestMapping(value = "/awardoperation", method = RequestMethod.GET)
	private String awardEdit() {
		// 奖品编辑页路由
		return "shop/awardoperation";
	}

}
