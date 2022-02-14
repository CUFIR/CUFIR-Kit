package org.cufir.s.data.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.cufir.s.data.Constant;
import org.cufir.s.data.ListRowMapper;
import org.cufir.s.data.dao.impl.EcoreCommonImpl;
import org.cufir.s.ecore.bean.Table;
import org.cufir.s.ecore.bean.TableColumn;
import org.cufir.s.ecore.bean.TableColumnModel;
import org.cufir.s.ecore.bean.TableModel;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;

/**
 * 数据操作工具类
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年10月8日
 */
public class DbUtil {
	
	private static final Logger logger = Logger.getLogger(DbUtil.class);

	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
//	private static final String dburl = "C://Users//tangmaoquan_ntt//Desktop//cide-2.0.0//config//db";
	private static final String dburl = Constant.CONFIG_PATH + "//db";
	private static final String connUrl = "jdbc:derby:" + dburl + ";create=false;rewriteBatchedStatements=true";
	
	/**
	 * 创建DataSource，供编程调试用
	 */
	private static DataSource buildDS() {
		// 采用Druid进行实现
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName(DRIVER);
//		ds.setUsername(this.getUsername());
//		ds.setPassword(this.getPassword());
		ds.setUrl(connUrl);
		ds.setTestWhileIdle(true);
		//配置初始化大小、最小、最大
		ds.setInitialSize(5);
		ds.setMinIdle(20);
		ds.setMaxActive(50);
		//配置从连接池获取等待超时时间
		ds.setMaxWait(60000);
		ds.setMinEvictableIdleTimeMillis(300000);
		//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		ds.setTimeBetweenEvictionRunsMillis(60000);
		//配置一个连接在池中最小生存的时间，单位是毫秒
		ds.setMinEvictableIdleTimeMillis(300000);
		ds.setPoolPreparedStatements(true);
		ds.setMaxPoolPreparedStatementPerConnectionSize(20);
		//配置监控统计拦截的filters
		try {
			ds.setFilters("stat");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//指明是否在池中取出连接进行验证，如如果检验失败，则从池中去除连接并尝试取出另一个
		ds.setTestOnBorrow(false);
		//指明连接是否被空闲连接回收期（如果有）进行监测，如果监测失败，则连接被从池中去除
		ds.setTestWhileIdle(true);
		//归还连接时执行  validationQuery 监测连接是否有效
		ds.setTestOnReturn(false);
		//是否自动回收超时连接
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(1800);
		//移除 Abandoned 连接时输出错误日志
		ds.setLogAbandoned(true);
		//keepalive 防止防火墙 断开 ，保持 Tcp 连接
		ds.setKeepAlive(true);
		return ds;
	}
	
	private static JdbcTemplate jt = null;
	
	/**
	 * 查询第一个
	 * @param sql
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findOne(String sql,Class<? extends T> clz, Object ... values) {
		List<?> list = find(sql, clz, values);
		if (list.size() > 0) {
			return (T) list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 查询
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 */
	public <T> List<T> find(String sql,Class<T> clz, Object ... values) {
		List<T> items = new ArrayList<T>();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(getLogggerSQL(sql, values));
			}
			try {
				TableModel tm = getTableModel(clz);
				ListRowMapper<T> listRowMapper = new ListRowMapper<T>(clz, tm);
				items = jt.query(sql, values, listRowMapper);
			} catch (Exception e) {
				logger.error("jt.query() exception", e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("执行语句失败:" + sql, e);
			return items;
		}
		return items;
	}
	
	/**
	 * 查询
	 * @param sql
	 * @param clz
	 * @param values
	 * @return
	 */
	public List<Map<String, Object>> find(String sql, Object ... values) {
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(getLogggerSQL(sql, values));
			}
			try {
				items = jt.queryForList(sql, values);
			} catch (Exception e) {
				logger.error("jt.query() exception", e);
				e.printStackTrace();
			}
//			
		} catch (Exception e) {
			logger.error("执行语句失败:" + sql, e);
			return items;
		}
		return items;
	}
	
	/**
	 * 统计查询Integer
	 * @param sql
	 * @return
	 */
	public Integer count(String sql, Object ... values) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(getLogggerSQL(sql));
			}
			return jt.queryForObject(sql, Integer.class, values);
		} catch (Exception e) {
			logger.error("执行语句失败:" + sql, e);
			return 0;
		}
	}
	
	/**
	 * 修改
	 * @param sql
	 * @param values
	 * @return
	 */
	public int update(String sql, Object ... values) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(getLogggerSQL(sql, values));
			}
			return jt.update(sql, values);
		} catch (Exception e) {
			logger.error("执行语句失败:" + sql, e);
			return 0;
		}
	}
	
	/**
	 * 保存
	 * @param sql
	 * @param values
	 * @return
	 */
	public int save(String sql, Object ... values) {
		return update(sql, values);
	}
	
	/**
	 * 批量保存
	 * @param sql
	 * @param values
	 * @return
	 */
	public int batchSave(String sql, List<Object[]> values) {
		try {
			System.out.println("入参数量" + values.size());
			int[] is = jt.batchUpdate(sql, values);
			System.out.println("成功数量" + is.length);
			return is.length;
		} catch (Exception e) {
			logger.error("执行语句失败:" + sql, e);
			return 0;
		}
	}
	
	/**
	 * 获得LoggerSQL
	 * @param sql
	 * @param values
	 * @return
	 */
	private static String getLogggerSQL(String sql, Object... values) {
		StringBuffer sb = new StringBuffer();
		sb.append(sql + "   (With Parameters:");
		for (Object object : values) {
			if (object == null) {
				object = "";
			} else {
				object = object.toString();
			}
			sb.append(object + " ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 私有构造函数
	 */
	private DbUtil() {
		if(jt == null) {
			jt = new JdbcTemplate(buildDS());
		}
	}

	/**
	 * 通过单例进行获得
	 */
	private static DbUtil instance = null;

	public synchronized static DbUtil get() {
		if (instance == null) {
			instance = new DbUtil();
		}
		return instance;
	}

	/**
	 * 定义Map对象，建立类名和TableModel的映射关系
	 */
	private static Map<String, TableModel> models = new HashMap<String, TableModel>();

	/**
	 * 通过类获取对应的TableModel，参见@Table/@TableColumn
	 */
	public static TableModel getTableModel(Class<?> clz) {
		// 从models中获取，以提高性能
		TableModel tm = models.get(clz.getName());
		if (tm == null) {
			// 获得@Table注解
			Table table = (Table) clz.getAnnotation(Table.class);
			if (null == table) {
				logger.error(clz.getName() + " 不存在注解@Table...");
				return null;
			}
			tm = new TableModel();
			// 设置数据库表对应的类名
			tm.setClzName(clz.getName());
			// 设置Table
			tm.setTable(table);
			// 进行TableColumnModel设置
			List<TableColumnModel> tcms = new ArrayList<TableColumnModel>();
			// 获得类包括父类的字段
			List<Field> fields = getClassSimpleFields(clz, true);
			int pos = 1;
			for (Field field : fields) {
				TableColumn tc = field.getAnnotation(TableColumn.class);
				if (null != tc) {
					TableColumnModel tcm = new TableColumnModel();
					String fieldName = field.getName();
					String colName = tc.name();
					if (StringUtils.isEmpty(colName)) {
						colName = fieldName;
					}
					tcm.setJavatype(field.getType().getName());
					tcm.setName(colName);
					tcm.setFieldName(fieldName);
					tcm.setAuto(tc.auto());
					// 设置序列显示格式
					if (!StringUtils.isEmpty(tc.format())) {
						tcm.setFormat(tc.format());
					}
					tcm.setTc(tc);
					tcm.setPosition(pos++);
					tcms.add(tcm);
				}
			}
			// 设置ColumnModels
			tm.setColumns(tcms);
			models.put(clz.getName(), tm);
		}
		return tm;
	}
	
	/**
	 * 获取一个类，及其全部祖先的类的字段和
	 * 
	 * @param c
	 * @param ancestor
	 * @return
	 */
	private static List<Field> getClassSimpleFields(Class<?> c, boolean ancestor) {
		List<Field> fields = new ArrayList<Field>();
		setClassSimpleFields(c, fields, ancestor);
		return fields;
	}
	
	/**
	 * 循环设置字段
	 * 
	 * @param c
	 * @param fields
	 * @param ancestor
	 */
	private static void setClassSimpleFields(Class<?> c, List<Field> fields,
			boolean ancestor) {
		for (Field field : c.getDeclaredFields()) {
			if (!ignoreFilterField(field)) {
				fields.add(field);
			}
		}
		if (ancestor) {
			Class<?> p = c.getSuperclass();
			if (p != null) {
				setClassSimpleFields(p, fields, ancestor);
			}
		}
	}
	
	/**
	 * 判断字段是否是要忽略的，即复杂对象不是SimpleField，SimpleField为常用的String|Integer|Date等字段
	 * 
	 * @param field
	 * @return
	 */
	private static boolean ignoreFilterField(Field field) {
		// 如果是序列化字段,忽略之
		if (field.getName().equalsIgnoreCase("serialVersionUID")) {
			return true;
		}
		// 如果是static字段,忽略之
		boolean staticField = Modifier.isStatic(field.getModifiers());
		if (staticField) {
			return true;
		}
		// 常规字段类型支持
		String type = field.getGenericType().toString();
		if (type.endsWith("java.lang.String")
				|| type.endsWith("java.lang.Integer")
				|| type.endsWith("java.lang.Double")
				|| type.endsWith("java.lang.Long")
				|| type.endsWith("java.lang.Boolean")
				|| type.endsWith("java.lang.Short")
				|| type.endsWith("java.util.Date")
				|| type.endsWith("java.math.BigDecimal")
				|| type.endsWith("java.sql.Timestamp")) {
				// 增加Timestamp @since 2.1.8.0
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
//		String dsql = "SELECT T.ID,T.NAME,T.TYPE,T.TRACE,t.registration_status FROM ECORE_DATA_TYPE T ORDER BY T.NAME";
//		String querySql = " select * from ECORE_MESSAGE_SET t where t.id = ? ";
//		List<EcoreMessageSet> find = DbUtil.get().find(querySql,EcoreMessageSet.class, "_RZFL1f2VEeGjgKzdN0DbWA");
//		String codeSetId  = "_awwx5dp-Ed-ak6NoX_4Aeg_-1525096597";
//		String sql = "select id from ECORE_CODE where CODE_SET_ID=? ";
//		List<Map<String, Object>> listMap = DbUtil.get().find(sql, codeSetId);
//		List<String> list = new ArrayList<>();
//		if(listMap.size() > 0) {
//			list = listMap.stream().map(m -> m.get("ID") + "").collect(Collectors.toList());
//		}
//		System.out.println("");
		EcoreCommonImpl ecoreCommonImpl = new EcoreCommonImpl();
		try {
			ecoreCommonImpl.deleteIso20022EcoreData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
