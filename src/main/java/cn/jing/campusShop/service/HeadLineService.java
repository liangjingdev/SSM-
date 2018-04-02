package cn.jing.campusShop.service;

import java.util.List;

import javax.imageio.IIOException;

import cn.jing.campusShop.entity.HeadLine;

public interface HeadLineService {

	//key (静态常量，一经定义即不能更改)
	public static final String HLLISTKEY = "headlinelist";

	/**
	 * function:根据传入的条件返回指定的头条列表
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws IIOException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
