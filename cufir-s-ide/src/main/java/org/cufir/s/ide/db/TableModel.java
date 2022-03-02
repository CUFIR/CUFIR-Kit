package org.cufir.s.ide.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 定义Table模型，包括列、数据集集合等，对应数据库的一个表
 */
public class TableModel implements java.io.Serializable {

	private static final long serialVersionUID = 4795280051769554880L;

	/**
	 * 定义列对象
	 */
	private List<TableColumnModel> columns = new ArrayList<TableColumnModel>();

	/**
	 * 对应的DbBean的子类类名
	 */
	private String clzName = "";

	/**
	 * 绑定的Table注解
	 */
	private Table table = null;

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 *  定义数据集对象
	 */
	private List<HashMap<String, Object>> data;

	public void setColumns(List<TableColumnModel> columns) {
		this.columns = columns;
	}

	public List<TableColumnModel> getColumns() {
		return columns;
	}

	public List<HashMap<String, Object>> getData() {
		return data;
	}

	public void setData(List<HashMap<String, Object>> data) {
		this.data = data;
	}

	/**
	 * 返回TableModel的名字,用于缺省的查询
	 */
	public String getName() {
		return table.name();
	}

	public String getClzName() {
		return clzName;
	}

	public void setClzName(String clzName) {
		this.clzName = clzName;
	}
}
