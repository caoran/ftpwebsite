package com.boco.henan.ftpwebsite.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表结果集数据显示格式
 * @author chengzhenhua
 *
 */
public class ListResult {

	private Long total;			//数据总条数
	private List<?> rows;		//一页显示条数
	
	public ListResult() {
		super();
	}

	public ListResult(Long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = new ArrayList<>(rows);
	}
	
	public Long getTotal() {
		return total;
	}
	
	public void setTotal(Long total) {
		this.total = total;
	}
	
	public List<?> getRows() {
		return rows;
	}
	
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
