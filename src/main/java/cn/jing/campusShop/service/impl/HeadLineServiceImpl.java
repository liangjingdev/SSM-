package cn.jing.campusShop.service.impl;

import java.util.List;

import javax.imageio.IIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.dao.HeadLineDao;
import cn.jing.campusShop.entity.HeadLine;
import cn.jing.campusShop.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {

	@Autowired
	private HeadLineDao headLineDao;

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IIOException {

		return headLineDao.queryHeadLine(headLineCondition);
	}

}
