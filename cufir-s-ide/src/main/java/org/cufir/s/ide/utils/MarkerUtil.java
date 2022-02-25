package org.cufir.s.ide.utils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;

public class MarkerUtil {

	// private static Logger logger = Logger.getLogger(ProblemHandler.class);

	/**
	 * 向Problems 添加一条 error 信息
	 * 
	 * @param resource
	 * @param message
	 * @param location
	 */
	public static void addMarkerError(IResource resource, String message, String location) {
		addMarker(IMarker.PROBLEM, resource, message, location, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_HIGH);
	}

	/**
	 * 向Problems列表 添加一条 Warning 信息
	 * 
	 * @param resource
	 * @param message
	 * @param location
	 */
	public static void addMarkerWarning(IResource resource, String message, String location) {
		addMarker(IMarker.PROBLEM, resource, message, location, IMarker.SEVERITY_WARNING, IMarker.PRIORITY_NORMAL);
	}

	/**
	 * 向Problems 添加一条 info 信息
	 * 
	 * @param resource
	 * @param message
	 * @param location
	 */
	public static void addMarkerInfo(IResource resource, String message, String location) {
		addMarker(IMarker.PROBLEM, resource, message, location, IMarker.SEVERITY_INFO, IMarker.PRIORITY_LOW);
	}

	/**
	 * 向Problems 添加一条信息
	 * 
	 * @param resource 资源
	 * @param message  设置问题的详细信息
	 * @param location location 信息
	 * @param severity 设置错误类型 info ｜ error ｜warning
	 * @param priority 设置优先级 HIGH | NORMAL | LOW
	 */
	public static void addMarker(String type, IResource resource, String message, String location, int severity,
			int priority) {
		try {
			IMarker marker = resource.createMarker(type); // 可设置 Prolem 或 Task
			// IMarker marker = resource.createMarker(IMarker.TASK);
			marker.setAttribute(IMarker.MESSAGE, message); // 设置问题的详细信息
			marker.setAttribute(IMarker.LOCATION, location); // 设置 location 信息，可以 int 类型 lineNumber 还可以是字符串类型
			marker.setAttribute(IMarker.SEVERITY, severity); // 设置错误类型 info ｜ error ｜warning
			marker.setAttribute(IMarker.PRIORITY, priority); //
		} catch (Exception e) {
			// logger.error("添加Marker失败！");
			e.printStackTrace();
		}
	}

}
