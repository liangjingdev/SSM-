package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.Award;

public interface AwardDao {

	/**
	 * function:依据传入进来的查询条件分页显示奖品信息列表
	 * 
	 * @param awardCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Award> queryAwardList(@Param("awardCondition") Award awardCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/**
	 * function:配合queryAwardList返回相同查询条件下的奖品数
	 * 
	 * @param awardCondition
	 * @return
	 */
	int queryAwardCount(@Param("awardCondition") Award awardCondition);

	/**
	 * function:通过awardId查询奖品信息
	 * 
	 * @param awardId
	 * @return
	 */
	Award queryAwardByAwardId(long awardId);

	/**
	 * function:添加奖品信息
	 * 
	 * @param award
	 * @return
	 */
	int insertAward(Award award);

	/**
	 * function:更新奖品信息
	 * 
	 * @param award
	 * @return
	 */
	int updateAward(Award award);

	/**
	 * function:删除奖品信息
	 * 因为当店家登录的时候操作店铺时会在session当中保存当前所操作的店铺Id，这里设置shopId是为了验证是否为该店家的操作，
	 * 避免非法从url进行注入参数更改数据。（双保险）
	 * 
	 * @param awardId
	 * @return
	 */
	int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
}
