package cn.jing.campusShop.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.dao.ProductSellDailyDao;
import cn.jing.campusShop.entity.ProductSellDaily;
import cn.jing.campusShop.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

	private static final Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
	@Autowired
	private ProductSellDailyDao productSellDailyDao;

	/**
	 * function:开启quartz定时任务，每天凌晨准时统计昨天的商品日销售量
	 */
	@Override
	public void dailyCalculate() {
		logger.info("Quartz Running");
		// 统计在tb_user_product_map里面产生销量的每个店铺的各件商品的日销量
		productSellDailyDao.insertProductSellDaily();
		// 统计余下的商品的日销量，全部置为0（为了迎合echart的数据请求）
		productSellDailyDao.insertDefaultProductSellDaily();
	}

	@Override
	public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
			Date endTime) {
		return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
	}

}
