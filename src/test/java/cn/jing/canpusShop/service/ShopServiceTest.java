package cn.jing.canpusShop.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import cn.jing.campusShop.dto.ShopExecution;
import cn.jing.campusShop.entity.Area;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.campusShop.enums.ShopStateEnum;
import cn.jing.campusShop.exceptions.ShopOperationException;
import cn.jing.campusShop.service.ShopService;
import cn.jing.campusShop.util.ImageHolder;
import cn.jing.canpusShop.BaseTest;

public class ShopServiceTest extends BaseTest {

	@Autowired
	private ShopService shopService;

	@Test
	@Ignore
	public void testAddShop() throws FileNotFoundException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(1);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺2");
		shop.setShopDesc("test2");
		shop.setShopAddr("test2");
		shop.setPhone("test2");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("/Users/liangjing/Documents/xiaohuangren.jpg");
		InputStream iStream = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(), iStream);
		ShopExecution sExecution = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), sExecution.getState());
	}

	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException, FileNotFoundException {
		Shop shop = shopService.getByShopId(22L);
		shop.setShopName("修改后的店铺名称");
		File shopImg = new File("/Users/liangjing/Documents/dabai.jpg");
		InputStream iStream = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(), iStream);
		ShopExecution shopExecution = shopService.modifyShop(shop, imageHolder);
		System.out.println("新的图片地址:" + shopExecution.getShop().getShopImg());
	}

	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		shopCondition.setShopCategory(shopCategory);
		// 注意此处的getShopList()方法的第二个参数是pageIndex而不是rowIndex
		ShopExecution shopExecution = shopService.getShopList(shopCondition, 1, 2);
		System.out.println("第一页店铺列表数为：" + shopExecution.getShopList().size());
		System.out.println("店铺总数为：" + shopExecution.getCount());
		shopExecution = shopService.getShopList(shopCondition, 2, 2);
		System.out.println("第二页店铺列表数为：" + shopExecution.getShopList().size());
		System.out.println("店铺总数为：" + shopExecution.getCount());
	}
}
