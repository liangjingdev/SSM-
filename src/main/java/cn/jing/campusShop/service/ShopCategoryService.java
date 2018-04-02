package cn.jing.campusShop.service;

import java.util.List;

import cn.jing.campusShop.entity.ShopCategory;

public interface ShopCategoryService {

	// key (静态常量，一经定义即不能更改)
	public final static String SCLISTKEY = "shopcategorylist";

	/**
	 * function:根据查询条件获取ShopCategory列表
	 * 
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
