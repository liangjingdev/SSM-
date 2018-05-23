package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.UserProductMapDao;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Product;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.UserProductMap;
import cn.jing.canpusShop.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMapDaoTest extends BaseTest {
	@Autowired
	private UserProductMapDao userProductMapDao;

	@Test
	public void testAInsertUserProductMap() throws Exception {
		// 创建用户商品映射信息1
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userProductMap.setUser(customer);
		userProductMap.setOperator(customer);
		Product product = new Product();
		product.setProductId(1L);
		userProductMap.setProduct(product);
		Shop shop = new Shop();
		shop.setShopId(25L);
		userProductMap.setShop(shop);
		userProductMap.setCreateTime(new Date());
		int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
		assertEquals(1, effectedNum);
		// 创建用户商品映射信息2
		UserProductMap userProductMap2 = new UserProductMap();
		PersonInfo customer2 = new PersonInfo();
		customer.setUserId(1L);
		userProductMap.setUser(customer);
		userProductMap.setOperator(customer);
		Product product2 = new Product();
		product.setProductId(1L);
		userProductMap.setProduct(product);
		Shop shop2 = new Shop();
		shop.setShopId(25L);
		userProductMap.setShop(shop);
		userProductMap.setCreateTime(new Date());
		int effectedNum2 = userProductMapDao.insertUserProductMap(userProductMap);
		assertEquals(1, effectedNum2);
	}

	@Test
	public void testBQueryUserProductMapList() throws Exception {
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		// 按顾客名字查询
		customer.setName("测试");
		userProductMap.setUser(customer);
		List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);
		assertEquals(2, userProductMapList.size());
		int count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
		// 叠加店铺去查询
		Shop shop = new Shop();
		shop.setShopId(25L);
		userProductMap.setShop(shop);
		userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);
		assertEquals(2, userProductMapList.size());
		count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
	}
}
