package org.cufir.plugin.mr.utils;

import org.cufir.plugin.mr.Activator;
import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * 图片工具
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月29日
 */
public class ImgUtil {

	//所有的父级菜单-----------------------start
	/**
	 * 数据类型图片地址
	 */
	public static String DATA_TYPES = "/icons/library-icon_16.png";
	
	/**
	 * 整合业务？？图片地址
	 */
	public static String BUSINESS_COMPONENTS = "/icons/binary-tree-icon_16.png";
	
	/**
	 * 自定义外部元素图片地址
	 */
	public static String EXTERNAL_SCHEMAS = "/icons/FAQ-icon_16.png";
	
	/**
	 * 元素组图片地址
	 */
	public static String MESSAGE_COMPONENTS = "/icons/building_block_16.png";
	
	/**
	 * 报文图片地址
	 */
	public static String MESSAGE_DEFINITIONS = "/icons/new-message-icon_16.png";
	
	/**
	 * 工作域图片地址
	 */
	public static String BUSINESS_AREAS = "/icons/city-icon_16.png";
	
	/**
	 * 报文集图片地址
	 */
	public static String MESSAGE_SETS = "/icons/set_packages_16.png";
	public static String VIEW_IDENTICAL_COMPONENTS = "/icons/Magnifying-Glass-icon_16.png";
	
	static Image DATA_TYPES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,DATA_TYPES).createImage();
	static Image BUSINESS_COMPONENTS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BUSINESS_COMPONENTS).createImage();
	static Image EXTERNAL_SCHEMAS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,EXTERNAL_SCHEMAS).createImage();
	static Image MESSAGE_COMPONENTS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MESSAGE_COMPONENTS).createImage();
	static Image MESSAGE_DEFINITIONS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MESSAGE_DEFINITIONS).createImage();
	static Image BUSINESS_AREAS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BUSINESS_AREAS).createImage();
	static Image MESSAGE_SETS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MESSAGE_SETS).createImage();
	static Image VIEW_IDENTICAL_COMPONENTS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,VIEW_IDENTICAL_COMPONENTS).createImage();
	//所有的父级菜单-----------------------end
	
	
	//data-types-----------------------start
	public static String CODE_SET_SUB1 = "/icons/vote_16.png";
	public static String CODE_SET_SUB1_LOCK = "icons/vote_lock_16.png";
	public static String CODE_SET_SUB1_2 = "/icons/list_icon_16.png";
	public static String CODE_SET_SUB1_2_LOCK = "icons/list_icon_lock_16.png";
	public static String CODE_SET_SUB2 = "/icons/MXMessageSet.gif";
	public static String TEXT_SUB1 = "/icons/contact-icon_lock_16.png";
	public static String TEXT_SUB1_WITHOUT_LOCK = "/icons/text_16.png";
	public static String BOOLEAN_SUB1 = "/icons/yes_no_16.png";
	public static String BOOLEAN_SUB1_LOCK = "/icons/yes_no_lock_16.png";
	public static String INDICATOR_SUB1 = "/icons/indicator_lock_16.png";
	public static String INDICATOR_SUB1_WITHOUT_LOCK = "/icons/indicator_16.png";
	public static String DECIMAL_SUB1 = "/icons/decimal_16.png";
	public static String DECIMAL_SUB1_LOCK = "/icons/decimal_lock_16.png";
	public static String RATE_SUB1 = "/icons/rate_lock_16.png";
	public static String RATE_SUB1_WITHOUT_LOCK = "/icons/rate_16.png";
	public static String AMOUNT_SUB1 = "/icons/coins_lock_16.png";
	public static String AMOUNT_SUB1_WITHOUT_LOCK = "/icons/coins_16.png";
	public static String QUANTITY_SUB1 = "/icons/balance-icon_lock_16.png";
	public static String QUANTITY_SUB1_WITHOUT_LOCK = "/icons/balance-icon_16.png";
	public static String TIME_SUB1 = "/icons/Calendar-icon_lock_16.png";
	public static String TIME_SUB1_WITHOUT_LOCK = "/icons/Calendar-icon_16.png";
	public static String BINARY_SUB1 = "/icons/binary_lock_16.png";
	public static String BINARY_SUB1_WITHOUT_LOCK = "/icons/binary_16.png";
	public static String SCHEMA_TYPES_SUB1 = "/icons/MXPrimitiveType.gif";
	public static String SCHEMA_TYPES_SUB1_LOCK = "/icons/MXPrimitiveType_lock.gif";
	public static String USER_DEFINED_SUB1 = "/icons/FAQ-icon_16.png";
	public static String USER_DEFINED_SUB1_LOCK = "/icons/FAQ-icon_lock_16.png";
	public static String CONSTRAINT = "/icons/validation_textual.png";
	
	static Image CODE_SETS_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.CODE_SETS.getImgPath()).createImage();
	static Image TEXT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.TEXT.getImgPath()).createImage();
	static Image BOOLEAN_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.BOOLEAN.getImgPath()).createImage();
	static Image INDICATOR_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.INDICATOR.getImgPath()).createImage();
	static Image DECIMAL_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.DECIMAL.getImgPath()).createImage();
	static Image RATE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.RATE.getImgPath()).createImage();
	static Image AMOUNT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.AMOUNT.getImgPath()).createImage();
	static Image QUANTITY_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.QUANTITY.getImgPath()).createImage();
	static Image TIME_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.TIME.getImgPath()).createImage();
	static Image BINARY_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.BINARY.getImgPath()).createImage();
	static Image SCHEMA_TYPES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.SCHEMA_TYPES.getImgPath()).createImage();
	static Image USER_DEFINED_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DataTypesEnum.USER_DEFINED.getImgPath()).createImage();
	
	static Image CODE_SET_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CODE_SET_SUB1).createImage();
	static Image CODE_SET_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CODE_SET_SUB1_LOCK).createImage();
	static Image CODE_SET_SUB1_2_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CODE_SET_SUB1_2).createImage();
	static Image CODE_SET_SUB1_2_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CODE_SET_SUB1_2_LOCK).createImage();
	
	static Image CODE_SET_SUB2_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CODE_SET_SUB2).createImage();
	static Image TEXT_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,TEXT_SUB1).createImage();
	static Image TEXT_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,TEXT_SUB1_WITHOUT_LOCK).createImage();
	static Image BOOLEAN_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BOOLEAN_SUB1).createImage();
	static Image BOOLEAN_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BOOLEAN_SUB1_LOCK).createImage();
	static Image INDICATOR_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,INDICATOR_SUB1).createImage();
	static Image INDICATOR_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,INDICATOR_SUB1_WITHOUT_LOCK).createImage();
	static Image DECIMAL_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,DECIMAL_SUB1).createImage();
	static Image DECIMAL_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,DECIMAL_SUB1_LOCK).createImage();
	static Image RATE_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,RATE_SUB1).createImage();
	static Image RATE_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,RATE_SUB1_WITHOUT_LOCK).createImage();
	static Image AMOUNT_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,AMOUNT_SUB1).createImage();
	static Image AMOUNT_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,AMOUNT_SUB1_WITHOUT_LOCK).createImage();
	static Image QUANTITY_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,QUANTITY_SUB1).createImage();
	static Image QUANTITY_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,QUANTITY_SUB1_WITHOUT_LOCK).createImage();
	static Image TIME_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,TIME_SUB1).createImage();
	static Image TIME_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,TIME_SUB1_WITHOUT_LOCK).createImage();
	static Image BINARY_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BINARY_SUB1).createImage();
	static Image BINARY_SUB1_WITHOUT_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BINARY_SUB1_WITHOUT_LOCK).createImage();
	static Image SCHEMA_TYPES_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,SCHEMA_TYPES_SUB1).createImage();
	static Image SCHEMA_TYPES_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,SCHEMA_TYPES_SUB1_LOCK).createImage();
	static Image USER_DEFINED_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,USER_DEFINED_SUB1).createImage();
	static Image USER_DEFINED_SUB1_LOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,USER_DEFINED_SUB1_LOCK).createImage();
	static Image CONSTRAINT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, CONSTRAINT).createImage();
	//data-types-----------------------end
	
	//business-components-----------------------start
	public static String BC = "/icons/BusinessComponent_16.png";
	public static String BC_BE = "/icons/BusinessElement_16.png";
	public static String BC_SUB_TYPES = "/icons/binary-tree-icon_16.png";
	public static String BC_SUPER_TYPES = "/icons/binary-tree-icon_16.png";
	
	static Image BC_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BC).createImage();
	static Image BC_BE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BC_BE).createImage();
	static Image BC_SUB_TYPES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BC_SUB_TYPES).createImage();
	static Image BC_SUPER_TYPES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BC_SUPER_TYPES).createImage();
	//business-components-----------------------end
	
	//message-component-----------------------start
	public static String MC_SUB1_COMPONENT = "/icons/MessageComponent_lock.gif";
	public static String MC_SUB3_COMPONENT = "/icons/MessageComponent.gif";
	public static String MC_SUB4_COMPONENT = "/icons/MessageComponent_Provisionally_lock.png";
	public static String MC_SUB1_CHOICE = "/icons/ChoiceComponent_lock.gif";
	public static String MC_SUB1_CHOICE1 = "/icons/ChoiceComponent.gif";
	public static String MC_SUB4_CHOICE = "/icons/ChoiceComponent_Provisionally_lock.png";
	/**
	 * 元素图片地址
	 */
	public static String MC_SUB2_DATA_TYPE = "/icons/xor_lock_16.png";
	
	/**
	 * 组件图片地址
	 */
	public static String MC_SUB2_COMPONENT = "/icons/dot_lock_16.png";
	public static String MC_SUB2_COMPONENT1 = "/icons/dot_16.png";
	public static String ME = "/icons/dot_16.png";
	
	static Image MC_SUB1_COMPONENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB1_COMPONENT).createImage();
	static Image MC_SUB3_COMPONENT1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB3_COMPONENT).createImage();
	static Image MC_SUB4_COMPONENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB4_COMPONENT).createImage();
	static Image MC_SUB1_CHOICE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB1_CHOICE).createImage();
	static Image MC_SUB1_CHOICE1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB1_CHOICE1).createImage();
	static Image MC_SUB4_CHOICE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB4_CHOICE).createImage();
	/**
	 * 元素图片
	 */
	static Image MC_SUB2_DATA_TYPE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB2_DATA_TYPE).createImage();
	static Image MC_SUB2_COMPONENT1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB2_COMPONENT1).createImage();
	/**
	 * 组件图片
	 */
	static Image MC_SUB2_COMPONENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MC_SUB2_COMPONENT).createImage();
	static Image ME_ELEMENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,ME).createImage();
	//message-component-----------------------end
	
	//message-definitions-----------------------start
	public static String MD_SUB1 = "/icons/new-message-icon_lock_16.png";
	public static String MD_SUB4 = "/icons/Message_Definition_Provisionally.png";
	/**
	 * 组件图片地址
	 */
	public static String MD_SUB2_COMPONENT = "/icons/dot_lock_16.png";
	/**
	 * 元素图片地址
	 */
	public static String MD_SUB2_DATA_TYPE = "/icons/xor_lock_16.png";
	public static String MD_SUB2_DATA_TYPE_WITHOUTLOCK = "/icons/xor_16.png";
	
	static Image MD_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MD_SUB1).createImage();
	static Image MD_SUB4_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MD_SUB4).createImage();
	/**
	 * 组件图片
	 */
	static Image MD_SUB2_COMPONENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MD_SUB2_COMPONENT).createImage();
	/**
	 * 元素图片
	 */
	static Image MD_SUB2_DATA_TYPE_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MD_SUB2_DATA_TYPE).createImage();
	static Image MD_SUB2_DATA_TYPE_WITHOUTLOCK_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MD_SUB2_DATA_TYPE_WITHOUTLOCK).createImage();
	//message-definitions-----------------------end
	
	//message-sets-----------------------start
	public static String MS_SUB1 = "/icons/MXMessageSet.gif";
	
	static Image MS_SUB1_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,MS_SUB1).createImage();
	//message-sets-----------------------end
	
	//add-img---------------------------start
	public static String BC_ADD_ICON = "/icons/CreateMXBusinessComponent_elements_MXBusinessElement.gif";
	//add-img---------------------------end
	static Image BC_ADD_ICON_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,BC_ADD_ICON).createImage();
	
	//添加按钮---------------------------start
	public static String ADD_IMG_TOOLBAR = "/icons/toolbar/nav-add_16.png";
	static Image ADD_IMG_TOOLBAR_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,ADD_IMG_TOOLBAR).createImage();
	//添加按钮---------------------------end
	
	//编辑按钮---------------------------start
	public static String EDIT_IMG_TOOLBAR = "/icons/toolbar/cut_edit.gif";
	static Image EDIT_IMG_TOOLBAR_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,EDIT_IMG_TOOLBAR).createImage();
	//编辑按钮---------------------------end
	
	//删除按钮---------------------------start
	public static String DELETE_IMG_YES = "/icons/toolbar/delete_edit_yes.gif";
	public static String DELETE_IMG_NO = "/icons/toolbar/delete_edit_no.gif";
	//删除按钮---------------------------end
	static Image DELETE_IMG_YES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DELETE_IMG_YES).createImage();
	static Image DELETE_IMG_NO_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, DELETE_IMG_NO).createImage();

	//搜索按钮
	public static String SEARCH_BTN = "/icons/xmag_16.png";
	static Image SEARCH_BTN_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, SEARCH_BTN).createImage();

	public static String BC_SEARCH_SELECTED_NO = "/icons/BusinessComponent_16_normal.png";
	public static String BC_SEARCH_SELECTED_YES = "/icons/BusinessComponent_16_selected.png";
	static Image BC_SEARCH_SELECTED_NO_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, BC_SEARCH_SELECTED_NO).createImage();
	static Image BC_SEARCH_SELECTED_YES_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, BC_SEARCH_SELECTED_YES).createImage();

	//MC-ADD
	public static String MC_ADD_ELEMENT = "/icons/toolbar/nav-add_element_16.png";
	public static String MC_ADD_CONSTRAINT = "/icons/toolbar/nav-add_rule_16.png";
	static Image MC_ADD_ELEMENT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, MC_ADD_ELEMENT).createImage();
	static Image MC_ADD_CONSTRAINT_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, MC_ADD_CONSTRAINT).createImage();
	
	//NEW VERSION
	public static String NEW_VERSION = "/icons/new-version_16.png";
	static Image NEW_VERSION_IMAGE = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, NEW_VERSION).createImage();
	
	public static Image createImg(String imgPath) {

		if (imgPath.equals(DataTypesEnum.CODE_SETS.getImgPath())) {
			return CODE_SETS_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.TEXT.getImgPath())) {
			return TEXT_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.BOOLEAN.getImgPath())) {
			return BOOLEAN_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.INDICATOR.getImgPath())) {
			return INDICATOR_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.DECIMAL.getImgPath())) {
			return DECIMAL_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.RATE.getImgPath())) {
			return RATE_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.AMOUNT.getImgPath())) {
			return AMOUNT_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.QUANTITY.getImgPath())) {
			return QUANTITY_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.TIME.getImgPath())) {
			return TIME_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.BINARY.getImgPath())) {
			return BINARY_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.SCHEMA_TYPES.getImgPath())) {
			return SCHEMA_TYPES_IMAGE;
		} else if (imgPath.equals(DataTypesEnum.USER_DEFINED.getImgPath())) {
			return USER_DEFINED_IMAGE;
		}

		if (imgPath.equals(CODE_SET_SUB1)) {
			return CODE_SET_SUB1_IMAGE;
		}else if (imgPath.equals(CODE_SET_SUB1_2)) {
			return CODE_SET_SUB1_2_IMAGE;
		} else if (imgPath.equals(CODE_SET_SUB2)) {
			return CODE_SET_SUB2_IMAGE;
		}else if(imgPath.equals(TEXT_SUB1)){
			return TEXT_SUB1_IMAGE;
		}else if(imgPath.equals(BOOLEAN_SUB1)){
			return BOOLEAN_SUB1_IMAGE;
		}else if(imgPath.equals(INDICATOR_SUB1)){
			return INDICATOR_SUB1_IMAGE;
		}else if(imgPath.equals(DECIMAL_SUB1)){
			return DECIMAL_SUB1_IMAGE;
		}else if(imgPath.equals(RATE_SUB1)){
			return RATE_SUB1_IMAGE;
		}else if(imgPath.equals(AMOUNT_SUB1)){
			return AMOUNT_SUB1_IMAGE;
		}else if(imgPath.equals(QUANTITY_SUB1)){
			return QUANTITY_SUB1_IMAGE;
		}else if(imgPath.equals(TIME_SUB1)){
			return TIME_SUB1_IMAGE;
		}else if(imgPath.equals(BINARY_SUB1)){
			return BINARY_SUB1_IMAGE;
		}else if(imgPath.equals(SCHEMA_TYPES_SUB1)){
			return SCHEMA_TYPES_SUB1_IMAGE;
		}else if(imgPath.equals(USER_DEFINED_SUB1)){
			return USER_DEFINED_SUB1_IMAGE;
		}else if(imgPath.equals(CONSTRAINT)) {
			return CONSTRAINT_IMAGE;
		}else if (imgPath.equals(CODE_SET_SUB1_LOCK)){
			return CODE_SET_SUB1_LOCK_IMAGE;
		}else if (imgPath.equals(CODE_SET_SUB1_2_LOCK)) {
			return CODE_SET_SUB1_2_LOCK_IMAGE;
		}else if(imgPath.equals(TEXT_SUB1_WITHOUT_LOCK)) {
			return TEXT_SUB1_LOCK_IMAGE;
		}else if(imgPath.equals(INDICATOR_SUB1_WITHOUT_LOCK)) {
			return INDICATOR_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(RATE_SUB1_WITHOUT_LOCK)) {
			return RATE_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(AMOUNT_SUB1_WITHOUT_LOCK)) {
			return AMOUNT_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(QUANTITY_SUB1_WITHOUT_LOCK)) {
			return QUANTITY_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(TIME_SUB1_WITHOUT_LOCK)) {
			return TIME_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(BINARY_SUB1_WITHOUT_LOCK)) {
			return BINARY_SUB1_WITHOUT_LOCK_IMAGE;
		}else if(imgPath.equals(BOOLEAN_SUB1_LOCK)) {
			return BOOLEAN_SUB1_LOCK_IMAGE;
		}else if(imgPath.equals(DECIMAL_SUB1_LOCK)) {
			return DECIMAL_SUB1_LOCK_IMAGE;
		}else if(imgPath.equals(SCHEMA_TYPES_SUB1_LOCK)) {
			return SCHEMA_TYPES_SUB1_LOCK_IMAGE;
		}else if(imgPath.equals(USER_DEFINED_SUB1_LOCK)) {
			return USER_DEFINED_SUB1_LOCK_IMAGE;
		}
		
		if(imgPath.equals(DATA_TYPES)) {
			return DATA_TYPES_IMAGE;
		}else if(imgPath.equals(BUSINESS_COMPONENTS)){
			return BUSINESS_COMPONENTS_IMAGE;
		}else if(imgPath.equals(EXTERNAL_SCHEMAS)){
			return EXTERNAL_SCHEMAS_IMAGE;
		}else if(imgPath.equals(MESSAGE_COMPONENTS)){
			return MESSAGE_COMPONENTS_IMAGE;
		}else if(imgPath.equals(MESSAGE_DEFINITIONS)){
			return MESSAGE_DEFINITIONS_IMAGE;
		}else if(imgPath.equals(BUSINESS_AREAS)){
			return BUSINESS_AREAS_IMAGE;
		}else if(imgPath.equals(MESSAGE_SETS)){
			return MESSAGE_SETS_IMAGE;
		}else if(imgPath.equals(VIEW_IDENTICAL_COMPONENTS)){
			return VIEW_IDENTICAL_COMPONENTS_IMAGE;
		}
		
		if(imgPath.equals(BC)) {
			return BC_IMAGE;
		}else if(imgPath.equals(BC_BE)){
			return BC_BE_IMAGE;
		}else if(imgPath.equals(BC_SUB_TYPES)){
			return BC_SUB_TYPES_IMAGE;
		}else if(imgPath.equals(BC_SUPER_TYPES)){
			return BC_SUPER_TYPES_IMAGE;
		}
		
		if(imgPath.equals(MC_SUB1_COMPONENT)) {
			return MC_SUB1_COMPONENT_IMAGE;
		}else if(imgPath.equals(MC_SUB3_COMPONENT)){
			return MC_SUB3_COMPONENT1_IMAGE;
		}else if(imgPath.equals(MC_SUB1_CHOICE)){
			return MC_SUB1_CHOICE_IMAGE;
		}else if(imgPath.equals(MC_SUB1_CHOICE1)){
			return MC_SUB1_CHOICE1_IMAGE;
		}else if(imgPath.equals(MC_SUB4_CHOICE)){
			return MC_SUB4_CHOICE_IMAGE;
		}else if(imgPath.equals(MC_SUB4_COMPONENT)){
			return MC_SUB4_COMPONENT_IMAGE;
		}else if(imgPath.equals(MC_SUB2_COMPONENT1)) {
			return MC_SUB2_COMPONENT1_IMAGE;
		}else if(imgPath.equals(MC_SUB2_COMPONENT)) {
			return MC_SUB2_COMPONENT_IMAGE;
		}else if(imgPath.equals(MC_SUB2_DATA_TYPE)) {
			return MC_SUB2_DATA_TYPE_IMAGE;
		}else if (imgPath.equals(ME)) {
			return ME_ELEMENT_IMAGE;
		}
		
		if(imgPath.equals(MD_SUB1)){
			return MD_SUB1_IMAGE;
		}else if(imgPath.equals(MD_SUB2_COMPONENT)){
			return MD_SUB2_COMPONENT_IMAGE;
		}else if(imgPath.equals(MD_SUB2_DATA_TYPE)){
			return MD_SUB2_DATA_TYPE_IMAGE;
		}else if(imgPath.equals(MD_SUB2_DATA_TYPE_WITHOUTLOCK)) {
			return MD_SUB2_DATA_TYPE_WITHOUTLOCK_IMAGE;
		}else if(imgPath.equals(MD_SUB4)){
			return MD_SUB4_IMAGE;
		}
		
		//message-sets-----------------------start
		if(imgPath.equals(MS_SUB1)){
			return MS_SUB1_IMAGE;
		}
		//message-sets-----------------------end
		
		//add-img------------------------start
		if(imgPath.equals(BC_ADD_ICON)) {
			return BC_ADD_ICON_IMAGE;
		}
		//add-img------------------------end
		
		//delete-img------------------------start
		if(imgPath.equals(DELETE_IMG_YES)) {
			return DELETE_IMG_YES_IMAGE;
		}else if(imgPath.equals(DELETE_IMG_NO)) {
			return DELETE_IMG_NO_IMAGE;
		}
		//delete-img------------------------end
		
		//add-img------------------------start
		if(imgPath.equals(ADD_IMG_TOOLBAR)) {
			return ADD_IMG_TOOLBAR_IMAGE;
		}else if(imgPath.equals(EDIT_IMG_TOOLBAR)) {
			return EDIT_IMG_TOOLBAR_IMAGE;
		}
		//add-img------------------------end
		
		if(imgPath.equals(SEARCH_BTN)) {
			return SEARCH_BTN_IMAGE;
		}
		
		if(imgPath.equals(BC_SEARCH_SELECTED_NO)) {
			return BC_SEARCH_SELECTED_NO_IMAGE;
		}else if(imgPath.equals(BC_SEARCH_SELECTED_YES)) {
			return BC_SEARCH_SELECTED_YES_IMAGE;
		}
		
		if(imgPath.equals(MC_ADD_ELEMENT)) {
			return MC_ADD_ELEMENT_IMAGE;
		}else if(imgPath.equals(MC_ADD_CONSTRAINT)) {
			return MC_ADD_CONSTRAINT_IMAGE;
		}
		
		if(imgPath.equals(NEW_VERSION)) {
			return NEW_VERSION_IMAGE;
		}

		return null;
	}
	
	/***
	 * 获取图片
	 * 
	 * @param args
	 */
	public static Image getImage(String imgPath) {
		Image img = getImageDescriptor(imgPath).createImage();
		return img;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, path);
	}
}
