package cn.jing.campusShop.dto;

import java.util.List;

/**
 * function:迎合Echart里的series项
 * 
 * @author liangjing
 *
 */
public class EchartSeries {

	private String name;
	private String type = "bar";
	private List<Integer> data;// 每天的日销量

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}

}
