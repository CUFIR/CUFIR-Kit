package org.cufir.s.ide.db;

/**
 * 模板
 */
public class TableColumnModel implements java.io.Serializable {

	private static final long serialVersionUID = 3940429581735847590L;

	public TableColumnModel() {}

	/**
	 * 字段标示
	 */
	private String remarks = "";
	/**
	 * 字段类型
	 */
	private String name = "";
	/**
	 * 对应的JavaBean的属性名称
	 */
	private String fieldName = "";
	/**
	 * 字段类型
	 */
	private String type = "";
	/**
	 * 字段长度
	 */
	private int length = 0;
	/**
	 * 缺省值
	 */
	private String defvalue = "";
	/**
	 * 是否为空
	 */
	private Boolean nullable = Boolean.FALSE;
	/**
	 * 是否是自增主键
	 */
	private boolean auto = Boolean.FALSE;
	/**
	 * 字段Java类型
	 */
	private String javatype = "";
	/**
	 * 某一列在某一行的值，用于数据的插入实现
	 */
	private Object value = null;
	/**
	 * 列ID的Sequence名称,参见Oracle的主键自增
	 */
	private String sequence = null;
	/**
	 * 数字|日期等字段的展示格式
	 */
	private String format = null;

	/**
	 * 绑定的TableColumn对象
	 */
	private TableColumn tc = null;

	private int position = 0;

	/**
	 * 设置lob字段
	 */
	private int lob = 0;

	public TableColumn getTc() {
		return tc;
	}

	public void setTc(TableColumn tc) {
		this.tc = tc;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable == 0 ? false : true;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDefvalue() {
		return defvalue;
	}

	public void setDefvalue(String defvalue) {
		if (defvalue == null) {
			defvalue = "null";
		}
		this.defvalue = defvalue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getJavatype() {
		return javatype;
	}

	public void setJavatype(String javatype) {
		this.javatype = javatype;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getLob() {
		return lob;
	}

	public void setLob(int lob) {
		this.lob = lob;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
