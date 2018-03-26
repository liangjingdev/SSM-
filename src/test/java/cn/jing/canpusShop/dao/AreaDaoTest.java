package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.AreaDao;
import cn.jing.campusShop.entity.Area;
import cn.jing.canpusShop.BaseTest;

/**
 * function：AreaDao单元测试 继承BaseTest基类，作用是每次进行该类的单元测试时都会自动地去加载基类中所定义的一些配置。
 * 
 * @author liangjing
 *
 */
public class AreaDaoTest extends BaseTest {

	@Autowired
	private AreaDao areaDao;

	@Test
	public void testQueryArea() {
		List<Area> areaList = areaDao.queryArea();
		assertEquals(2, areaList.size()); // 利用assertEquals()方法来判断查询后返回的areaList中的条目数量是否于你给的值相等
	}
}
