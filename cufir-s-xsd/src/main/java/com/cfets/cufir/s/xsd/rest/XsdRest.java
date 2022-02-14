package com.cfets.cufir.s.xsd.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cfets.cufir.s.xsd.XsdHelper;
import com.cfets.cwap.s.daemon.GenericRest;
import com.cfets.cwap.s.stp.SimpleMessage;
import com.cfets.cwap.s.util.annotation.Note;
import com.cfets.cwap.s.util.db.JdbcManager;
import com.cfets.cwap.s.util.db.filter.Filter;
import com.cfets.cwap.s.util.db.filter.FilterAttrs;
import com.cfets.cwap.s.util.db.filter.FilterOrder;
import com.cfets.cwap.s.util.db.filter.FilterOrders;


@RestController
@RequestMapping(XsdHelper.PLUGIN_MAPPING)
public class XsdRest  extends GenericRest {
	
	public JdbcManager jdbcManager = XsdHelper.getJdbcManager();

	@Note(note = "REST方法名注释", since = "1.0.0.0", author = "")
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public SimpleMessage<?> query(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 通过HttpServletRequest获得请求参数
			String k1 = this.getParam(request, "k1", "defaultValue1");
			String k2 = this.getParam(request, "k2", "defaultValue2");

			// 定义筛选条件,参见FilterAttrs
			FilterAttrs fa = FilterAttrs.blank().addIfNotNull("k1", k1).addIfNotNull("k2", k2);
			// 排序字段
			FilterOrders fo = FilterOrders.blank().add("orderColumn", FilterOrder.TYPE_DESC);
			Filter filter = Filter.blank().attrs(fa).orders(fo);
			// 获取查询结果
			List<Object> list = jdbcManager.find(Object.class, filter);

			// 返回数据
			SimpleMessage<Object> resp = new SimpleMessage<Object>();
			resp.addRecords(list);
			return resp;
		} catch (Exception e) {
			// logger.err(e);
			return SimpleMessage.error(e.getMessage());
		}
	}

}

