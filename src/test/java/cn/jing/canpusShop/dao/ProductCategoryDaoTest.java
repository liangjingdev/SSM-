package cn.jing.canpusShop.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.ProductCategoryDao;
import cn.jing.campusShop.entity.ProductCategory;
import cn.jing.canpusShop.BaseTest;

/**
 * function:使用Junit做单端测试时，并不是按顺序执行以下测试方法的，而是随机进行调用的。
 * 有时候需要让Junit按顺序执行以下测试方法，可以使用注解@FixMethodOrder标注该类。
 * （有三种顺序(属性)可以选择：MethodSorters.JVM，MethodSorters.NAME_ASCENDING--按照方法的名字依次执行(比如字母A-testA先执行，字母B-testB后执行)，
 * MethodSorters.DEFAULT）
 * 
 * 注意单元测试所谓的测试的回环：指的是测试时你给数据库添加了数据，之后又从数据库中删除了之前添加的数据，形成了回环，很好的一种测试效果。
 * 
 * @author liangjing
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Test
	@Ignore
	public void testAQueryByShopId() {
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数为：" + productCategoryList.size());
	}

	@Test
	@Ignore
	public void testBBatchInsertProductCategory() {
		ProductCategory productCategory1 = new ProductCategory();
		productCategory1.setProductCategoryName("商品类别1");
		productCategory1.setPriority(1);
		productCategory1.setCreateTime(new Date());
		productCategory1.setShopId(1L);
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory1);
		productCategoryList.add(productCategory2);
		int effectNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectNum);
	}

	@Test
	public void testCDeleteProductCategory() throws Exception {
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		for (ProductCategory productCategory : productCategoryList) {
			if ("商品类别1".equals(productCategory.getProductCategoryName())
					|| "商品类别2".equals(productCategory.getProductCategoryName())) {
				int effectNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(),
						shopId);
				assertEquals(1, effectNum);
			}
		}
	}
}
