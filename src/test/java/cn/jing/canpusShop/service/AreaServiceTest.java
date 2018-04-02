package cn.jing.canpusShop.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.entity.Area;
import cn.jing.campusShop.service.AreaService;
import cn.jing.campusShop.service.CacheService;
import cn.jing.canpusShop.BaseTest;

public class AreaServiceTest extends BaseTest {

	@Autowired
	private AreaService areaService;
	@Autowired
	private CacheService cacheService;

	@Test
	public void testGetAreaList() {
		List<Area> areaList = areaService.getAreaList();
		assertEquals("东莞", areaList.get(0).getAreaName()); // 测试
		cacheService.removeFromCache(AreaService.AREALISTKEY);
		areaList = areaService.getAreaList();
	}
}
