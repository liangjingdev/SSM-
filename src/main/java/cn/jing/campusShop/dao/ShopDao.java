package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.Shop;
import cn.jing.campusShop.entity.ShopCategory;

/**
 * function:店铺的Dao层
 * 
 * @author liangjing
 *
 */
public interface ShopDao {

	/**
	 * function:新增店铺
	 * 
	 * @param shop
	 * @return 1.添加成功 -1.添加失败
	 */
	int insertShop(Shop shop);

	/**
	 * function:更新店铺信息
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);

	/**
	 * function:通过shopId去查询对应的店铺信息
	 * 
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(long shopId);

	/**
	 * function:分页查询店铺，可输入的条件有：店铺名(模糊)，店铺状态，店铺类别，区域Id，owner
	 * 
	 * @param shopCondition
	 * @param rowIndex
	 *            表示从第几行开始去取数据(注意：若要从数据库的表中从第一行开始取出数据，则应该填写0而不是1)
	 * @param pageSize
	 *            表示要取出多少行数据（返回的条数）
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * function:因为queryShopList()是分页查询的，假如查询到数据有十条，而我们定义了一次只能返回6条数据，那么我们就可以分两次去查询，所以首先要得到查询后的数据集的总条数
	 * 即返回shopList总数。
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);

}
