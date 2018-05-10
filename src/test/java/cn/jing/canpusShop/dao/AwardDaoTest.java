package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.AwardDao;
import cn.jing.campusShop.entity.Award;
import cn.jing.canpusShop.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest extends BaseTest {

	@Autowired
	private AwardDao awardDao;

	@Test
	public void testAInsertAward() throws Exception {
		long shopId = 25;
		// 奖品一
		Award award1 = new Award();
		award1.setAwardName("测试一");
		award1.setAwardImg("test1");
		award1.setPoint(5);
		award1.setPriority(1);
		award1.setAwardDesc("我的奖品1");
		award1.setEnableStatus(1);
		award1.setCreateTime(new Date());
		award1.setLastEditTime(new Date());
		award1.setShopId(shopId);
		int effectedNum = awardDao.insertAward(award1);
		assertEquals(1, effectedNum);
		// 奖品二
		Award award2 = new Award();
		award2.setAwardName("测试二");
		award2.setAwardImg("test2");
		award2.setPoint(5);
		award2.setPriority(1);
		award2.setAwardDesc("我的奖品2");
		award2.setEnableStatus(1);
		award2.setCreateTime(new Date());
		award2.setLastEditTime(new Date());
		award2.setShopId(shopId);
		effectedNum = awardDao.insertAward(award2);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testBQueryAwardList() throws Exception {
		Award award = new Award();
		List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
		assertEquals(2, awardList.size());
		int count = awardDao.queryAwardCount(award);
		assertEquals(2, count);
		award.setAwardName("测试一");
		awardList = awardDao.queryAwardList(award, 0, 3);
		assertEquals(1, awardList.size());
		count = awardDao.queryAwardCount(award);
		assertEquals(1, count);
	}

	@Test
	public void testCQueryAwardByAwardId() throws Exception {
		Award awardCondition = new Award();
		awardCondition.setAwardName("测试二");
		List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 1);
		assertEquals(1, awardList.size());
		Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
		assertEquals("测试二", award.getAwardName());
	}

	@Test
	public void testDUpdateAward() throws Exception {
		Award award = new Award();
		List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
		award.setAwardId(awardList.get(0).getAwardId());
		award.setShopId(awardList.get(0).getShopId());
		award.setAwardName("测试三");
		int effectedNum = awardDao.updateAward(award);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testEDeleteAward() throws Exception {
		Award awardCondition = new Award();
		awardCondition.setAwardName("测试");
		List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 3);
		assertEquals(2, awardList.size());
		for (Award award : awardList) {
			int effectedNum = awardDao.deleteAward(award.getAwardId(), award.getShopId());
			assertEquals(1, effectedNum);
		}
	}
}
