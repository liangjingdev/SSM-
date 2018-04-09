package cn.jing.campusShop.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.ProductSellDaily;

public interface ProductSellDailyDao {

	/**
	 * function:根据查询条件返回某商品日销量的统计列表
	 * 
	 * @param productSellDailyCondition
	 * @param beginTime
	 *            某天开始
	 * @param endTime
	 *            某天结束
	 * @return
	 */
	List<ProductSellDaily> queryProductSellDailyList(
			@Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

	/**
	 * function:统计平台所有商品的日销售量 （对UserProductMap这张表进行分析并获取数据）
	 * 
	 * @return
	 */
	int insertProductSellDaily();

	/**
	 * function:统计平台当天没有销量的商品，补全信息，将它们的销量置为0
	 * (说白了，也就是还需要给它们一个ProductSellDaily实体类对象，避免Echart图表的数据链断开)
	 * 
	 * @return
	 */
	int insertDefaultProductSellDaily();
}
