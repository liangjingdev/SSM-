package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.ShopCategory;

public interface ShopCategoryDao {

	//以ShopCategory作为参数，因为在有的情况下是需要去查找某个店铺类型下的所有子类型
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
