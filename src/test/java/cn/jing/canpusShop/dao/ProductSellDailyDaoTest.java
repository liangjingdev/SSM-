package cn.jing.canpusShop.dao;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.ProductSellDailyDao;
import cn.jing.campusShop.entity.ProductSellDaily;
import cn.jing.campusShop.entity.Shop;
import cn.jing.canpusShop.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest extends BaseTest {

	@Autowired
	private ProductSellDailyDao productSellDailyDao;

	// 测试添加功能
	@Test
	public void testAnsertProductSellDaily() throws Exception {
		// 创建商品日销量统计
		int effectnum = productSellDailyDao.insertProductSellDaily();
		assertEquals(3, effectnum);
	}

	// 测试查询功能
	@Test
	public void testBQueryProductSellDaily() throws Exception {
		ProductSellDaily productSellDaily = new ProductSellDaily();
		// 叠加店铺去查询
		Shop shop = new Shop();
		shop.setShopId(25L);
		productSellDaily.setShop(shop);
		List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily,
				null, null);
		assertEquals(3, productSellDailyList.size());
	}

	// 测试添加功能
	@Test
	public void testBInsertDefaultProductSellDaily() throws Exception {
		// 创建商品日销售量统计
		int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
		assertEquals(7, effectedNum);
	}
}
