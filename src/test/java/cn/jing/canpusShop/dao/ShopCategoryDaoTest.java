package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.ShopCategoryDao;
import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.canpusShop.BaseTest;

public class ShopCategoryDaoTest extends BaseTest {

	@Autowired
	ShopCategoryDao shopCategoryDao;

	@Test
	public void testQueryShopCategory() {
		List<ShopCategory> shopCategorielList = shopCategoryDao.queryShopCategory(null);
		System.out.println(shopCategorielList.size());
	}

}
