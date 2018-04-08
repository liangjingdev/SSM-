package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.UserProductMap;

public interface UserProductMapDao {
	/**
	 * function:根据查询条件分页返回用户购买的记录列表
	 * 
	 * @param userProductCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * function：配合queryUserProductMapList根据相同的查询条件返回用户购买商品的记录总数
	 * 
	 * @param userProductCondition
	 * @return
	 */
	int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

	/**
	 * function：添加一条用户购买商品的记录
	 * 
	 * @param userProductMap
	 * @return
	 */
	int insertUserProductMap(UserProductMap userProductMap);
}
