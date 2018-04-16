package cn.jing.campusShop.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jing.campusShop.cache.JedisUtil;
import cn.jing.campusShop.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {

	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Override
	public void removeFromCache(String keyPrefix) {
		// jedis.keys(“*”);表示搜索所有key
		// jedis.keys(“abc*”)表示搜索开头为abc的key数据
		Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
		for (String key : keySet) {
			jedisKeys.del(key);
		}
	}

}
