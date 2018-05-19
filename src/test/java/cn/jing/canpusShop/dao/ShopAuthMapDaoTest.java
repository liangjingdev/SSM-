package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.ShopAuthMapDao;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopAuthMap;
import cn.jing.canpusShop.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopAuthMapDaoTest extends BaseTest {
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Test
	public void testAInsertShopAuthMap() throws Exception {
		// 创建店铺授权信息1
		ShopAuthMap shopAuthMap1 = new ShopAuthMap();
		PersonInfo employee = new PersonInfo();
		employee.setUserId(1L);
		shopAuthMap1.setEmployee(employee);
		Shop shop = new Shop();
		shop.setShopId(1L);
		shopAuthMap1.setShop(shop);
		shopAuthMap1.setTitle("CEO");
		shopAuthMap1.setTitleFlag(1);
		shopAuthMap1.setCreateTime(new Date());
		shopAuthMap1.setLastEditTime(new Date());
		shopAuthMap1.setEnableStatus(1);
		int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
		assertEquals(1, effectedNum);
		// 创建店铺授权信息2
		ShopAuthMap shopAuthMap2 = new ShopAuthMap();
		PersonInfo employee2 = new PersonInfo();
		employee2.setUserId(1L);
		shopAuthMap2.setEmployee(employee2);
		Shop shop2 = new Shop();
		shop2.setShopId(25L);
		shopAuthMap2.setShop(shop2);
		shopAuthMap2.setTitle("打工仔");
		shopAuthMap2.setTitleFlag(2);
		shopAuthMap2.setCreateTime(new Date());
		shopAuthMap2.setLastEditTime(new Date());
		shopAuthMap2.setEnableStatus(1);
		effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryShopAuthMapListByShopId() throws Exception {
		// 测试queryShopAuthMapListByShopId
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 1);
		assertEquals(1, shopAuthMapList.size());
		// 测试queryShopAuthMapListById
		ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
		assertEquals("CEO", shopAuthMap.getTitle());
		// 测试queryShopAuthMapCountByShopId
		int count = shopAuthMapDao.queryShopAuthCountByShopId(1);
		assertEquals(1, count);
	}

	@Test
	public void testCUpdateShopAuthMap() throws Exception {
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 1);
		shopAuthMapList.get(0).setTitle("CCO");
		shopAuthMapList.get(0).setTitleFlag(2);
		int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
		assertEquals(1, effectedNum);
	}

	@Test
	public void testDeleteShopAuthMap() throws Exception {
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 1);
		List<ShopAuthMap> shopAuthMapList2 = shopAuthMapDao.queryShopAuthMapListByShopId(25, 0, 1);
		int effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
		effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList2.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
	}
}
