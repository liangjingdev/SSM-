package cn.jing.campusShop.dao;

import java.util.List;

import cn.jing.campusShop.entity.Area;

/**
 * function:Area的Dao层
 * 
 * @author liangjing
 *
 */
public interface AreaDao {

	/**
	 * function:列出区域列表
	 * 
	 * @return
	 */
	List<Area> queryArea();

}
