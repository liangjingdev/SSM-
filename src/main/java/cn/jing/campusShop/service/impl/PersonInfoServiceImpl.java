package cn.jing.campusShop.service.impl;

import java.awt.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.dao.PersonInfoDao;
import cn.jing.campusShop.entity.PersonInfo;
import cn.jing.campusShop.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

}
