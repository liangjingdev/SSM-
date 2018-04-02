package cn.jing.campusShop.service;

import java.util.List;

import cn.jing.campusShop.entity.Area;

/**
 * function:Area的业务逻辑层
 * 
 * @author liangjing
 *
 */
public interface AreaService {

	//key (静态常量，一经定义即不能更改)
	public static final String AREALISTKEY = "arealist";

	List<Area> getAreaList();
}
