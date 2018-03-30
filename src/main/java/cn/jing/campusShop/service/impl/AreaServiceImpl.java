package cn.jing.campusShop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.dao.AreaDao;
import cn.jing.campusShop.entity.Area;
import cn.jing.campusShop.service.AreaService;

/**
 * function:AreaService接口的实现类
 * 
 * @author liangjing
 *
 */
@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;

	@Override
	public List<Area> getAreaList() {
		return areaDao.queryArea();
	}

}
