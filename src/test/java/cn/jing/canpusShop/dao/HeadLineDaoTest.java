package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.HeadLineDao;
import cn.jing.campusShop.entity.HeadLine;
import cn.jing.canpusShop.BaseTest;

public class HeadLineDaoTest extends BaseTest {

	@Autowired
	private HeadLineDao headLineDao;

	@Test
	public void testQueryHeadLine() {
		List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
		assertEquals(2, headLineList.size());
	}

}
