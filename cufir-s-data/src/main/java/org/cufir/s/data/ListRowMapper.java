package org.cufir.s.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.TableColumn;
import org.cufir.s.ecore.bean.TableColumnModel;
import org.cufir.s.ecore.bean.TableModel;
import org.springframework.jdbc.core.RowMapper;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;

/**
 * 列表数据的Mapper对象
 * @author tangmaoquan
 * @Date 2021年10月20日
 */
public class ListRowMapper<T> implements RowMapper<T> {

	private static Logger logger = Logger.getLogger(ListRowMapper.class);

	/**
	 * 传入类对象,供创建实例用
	 */
	private Class<T> clz;
	/**
	 * 传入clz的DataModel
	 */
	private TableModel tm;

	/**
	 * 构造函数，传入类名和对应的TableModel
	 * 
	 * @param clz
	 * @param tm
	 */
	public ListRowMapper(Class<T> clz, TableModel tm) {
		this.clz = clz;
		this.tm = tm;
	}

	/**
	 * 数据库类型
	 * 
	 */
	public static final String DATA_TYPE_LONG = "java.lang.Long";
	public static final String DATA_TYPE_INTEGER = "java.lang.Integer";
	public static final String DATA_TYPE_DOUBLE = "java.lang.Double";
	public static final String DATA_TYPE_BIGDECIMAL = "java.math.BigDecimal";
	public static final String DATA_TYPE_BOOLEAN = "java.lang.Boolean";
	public static final String DATA_TYPE_DATE = "java.util.Date";
	public static final String DATA_TYPE_TIMESTAMP = "java.sql.Timestamp";
	public static final String DATA_TYPE_EMBEDCLOB = "org.apache.derby.impl.jdbc.EmbedClob";

	@Override
	public T mapRow(ResultSet rs, int index) throws SQLException {
		T bean = null;
		try {
			// Merge数据
			List<TableColumnModel> tcms = tm.getColumns();
			// Bean对象实例化
			bean = clz.newInstance();
			for (TableColumnModel tcm : tcms) {
				String colName = tcm.getName();
				Object colValue = null;
				String fieldName = tcm.getFieldName();
				String javatype = tcm.getJavatype();
				switch (javatype) {
				case DATA_TYPE_LONG:
					colValue = rs.getLong(colName);
					break;
				case DATA_TYPE_INTEGER:
					colValue = rs.getInt(colName);
					break;
				case DATA_TYPE_DOUBLE:
					colValue = rs.getDouble(colName);
					break;
				case DATA_TYPE_BIGDECIMAL:
					colValue = rs.getBigDecimal(colName);
					try {
						if (tcm.getFormat() != null && colValue != null) {
							DecimalFormat format = new DecimalFormat(tcm.getFormat());
							colValue = new BigDecimal(format.format(colValue));
						}
					} catch (Exception e) {
						// 扔出可能的其它格式化异常错误
						logger.error(e);
					}
					break;
				case DATA_TYPE_BOOLEAN:
					colValue = rs.getInt(colName);
					break;
				case DATA_TYPE_DATE:
					if (tcm.getFormat() != null && tcm.getFormat().equals(TableColumn.DATE_DATE)) {
						colValue = rs.getDate(colName);
					} else {
						colValue = rs.getTimestamp(colName);
					}
					break;
				case DATA_TYPE_TIMESTAMP:
					// 增加对Timestamp的支持
					colValue = rs.getTimestamp(colName);
					break;
				default:
					colValue = rs.getObject(colName);
					if(colValue instanceof ClobProxyImpl) {
						ClobProxyImpl clobProxyImpl = (ClobProxyImpl)colValue;
						Clob clob = clobProxyImpl.getRawClob();
						colValue = ClobToString(clob);
					}
					break;
				}

				if (rs.wasNull()) {
					colValue = null;
				}
				try {
					PropertyUtils.setSimpleProperty(bean, fieldName, colValue);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("设置对象字段失败:" + clz.getName() + "\t" + fieldName + "\t" + colValue);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return bean;
	}
	
	private String ClobToString(Clob clob) throws SQLException, IOException {
		String reString = "";
		Reader reader = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(reader);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while(s != null) {
			sb.append(s);
			s= br.readLine();
		}
		reString = sb.toString();
		if(br!= null) {
			br.close();
		}
		if(reader != null) {
			reader.close();
		}
		return reString;
	}
}
