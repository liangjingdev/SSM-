package cn.jing.campusShop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.jing.campusShop.entity.HeadLine;

/**
 * function:头条Dao层
 * 
 * @author liangjing
 *
 */
public interface HeadLineDao {

	/**
	 * function:根据传入的查询条件(比如使用头条名查询头条；如果传入的可用状态不为空的话，那么就根据可用状态去筛选出相对应的头条信息)去查找相应的头条信息
	 * 
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

}
