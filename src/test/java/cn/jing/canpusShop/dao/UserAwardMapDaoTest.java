package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.UserAwardMapDao;
import cn.jing.campusShop.entity.Award;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.UserAwardMap;
import cn.jing.canpusShop.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest extends BaseTest {
	@Autowired
	private UserAwardMapDao userAwardMapDao;

	@Test
	public void testAInsertUserAwardMap() throws Exception {
		// 创建用户奖品映射信息1
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userAwardMap.setUser(customer);
		userAwardMap.setOperator(customer);
		Award award = new Award();
		award.setAwardId(1L);
		userAwardMap.setAward(award);
		Shop shop = new Shop();
		shop.setShopId(25L);
		userAwardMap.setShop(shop);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(1);
		userAwardMap.setPoint(2);
		int effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
		assertEquals(1, effectedNum);
		// 创建用户奖品映射信息2
		UserAwardMap userAwardMap2 = new UserAwardMap();
		PersonInfo customer2 = new PersonInfo();
		customer.setUserId(1L);
		userAwardMap.setUser(customer);
		userAwardMap.setOperator(customer);
		Award award2 = new Award();
		award.setAwardId(2L);
		userAwardMap.setAward(award);
		Shop shop2 = new Shop();
		shop.setShopId(25L);
		userAwardMap.setShop(shop);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(1);
		userAwardMap.setPoint(2);
		effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryUserAwardMapList() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();
		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(2, userAwardMapList.size());
		int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(2, count);
		PersonInfo customer = new PersonInfo();
		customer.setName("测试");
		// 按用户名模糊查询
		userAwardMap.setUser(customer);
		userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(2, userAwardMapList.size());
		// 测试queryUserAwardmapById，预期按优先级排列返回第二个奖品的信息
		userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardMapList.get(0).getUserAwardId());
		assertEquals("奖品2", userAwardMap.getAward().getAwardName());
	}

	@Test
	public void testCUpdateUserAwardmap() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		// 按用户名模糊查询
		customer.setName("测试");
		userAwardMap.setUser(customer);
		// 取出的是第二条，因为按时间优先级排列
		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 1);
		assertTrue("Error,积分不一致！", 0 == userAwardMapList.get(0).getUsedStatus());
		userAwardMapList.get(0).setUsedStatus(1);
		int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMapList.get(0));
		assertEquals(1, effectedNum);
	}
}
