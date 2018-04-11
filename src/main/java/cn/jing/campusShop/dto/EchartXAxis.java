package cn.jing.campusShop.dto;

import java.util.HashSet;

/**
 * function:迎合Echart里的xAxis项
 * 
 * @author liangjing
 *
 */
public class EchartXAxis {

	private String type = "category";
	// 为了去重，所以使用Set集合（保证数据的唯一性）
	private HashSet<String> data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashSet<String> getData() {
		return data;
	}

	public void setData(HashSet<String> data) {
		this.data = data;
	}

}
