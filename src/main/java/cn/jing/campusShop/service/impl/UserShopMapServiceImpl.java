package cn.jing.campusShop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.jing.campusShop.dao.UserShopMapDao;
import cn.jing.campusShop.dto.UserShopMapExecution;
import cn.jing.campusShop.entity.UserShopMap;
import cn.jing.campusShop.service.UserShopMapService;
import cn.jing.campusShop.util.PageCalculator;

public class UserShopMapServiceImpl implements UserShopMapService {

	@Autowired
	private UserShopMapDao userShopMapDao;

	@Override
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
		// 空值判断(如果int类型的参数值没有传递过来，则默认为-1)
		if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
			// 页转行
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			// 根据传入的查询条件分页返回用户积分列表信息
			List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex,
					pageSize);
			// 返回总数
			int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
			UserShopMapExecution ue = new UserShopMapExecution();
			ue.setUserShopMapList(userShopMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}
	}

	@Override
	public UserShopMap getUserShopMap(long userId, long shopId) {
		return userShopMapDao.queryUserShopMap(userId, shopId);
	}

}
