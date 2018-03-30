package cn.jing.campusShop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.dao.ShopCategoryDao;
import cn.jing.campusShop.entity.ShopCategory;
import cn.jing.campusShop.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao; 

	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {

		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}
