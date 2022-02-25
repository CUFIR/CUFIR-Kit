package org.cufir.rcp.kit;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public class Activator extends AbstractUIPlugin {

//	public final static String CUFIR_NAME = "中国金融业通用报文开发工具";
	public final static String CUFIR_NAME = "CUFIR Kit";

//	public final static String CUFIR_ABBR = "164";

	public final static String CUFIR_DATE = "since 2019";

	public final static String CUFIR_VERSION = "1.0.0";

	public final static String CUFIR_URL = "http://192.168.212.221/index";

	// The plug-in ID
	public static final String PLUGIN_ID = "cufir-rcp-kit"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
