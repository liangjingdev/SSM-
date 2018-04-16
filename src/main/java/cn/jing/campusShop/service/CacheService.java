package cn.jing.campusShop.service;

public interface CacheService {

	/**
	 * function:依据key前缀删除匹配模式下的所有key-value，如传入：shopcategory，则shopcategory_allfirstlevel等
	 * 以shopcategory打头的key-value都会被清空（大范围杀害，哈哈哈）
	 * 
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
