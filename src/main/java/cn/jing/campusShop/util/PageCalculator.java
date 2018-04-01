package cn.jing.campusShop.util;

/**
 * function:将pageIndex转换成rowIndex
 * 
 * @author liangjing
 *
 */
public class PageCalculator {

	public static int calculateRowIndex(int pageIndex, int pageSize) {
		// 若为第一页的话，则从表中第一行数据开始选取pageSize条数据。若为第二页的话，则从pageSize行数据开始选择pageSize条数据，以此类推。
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}
