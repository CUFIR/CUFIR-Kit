package org.cufir.plugin.mr.editor;

import org.cufir.s.data.dao.impl.EcoreBusinessAreaImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessComponentRLImpl;
import org.cufir.s.data.dao.impl.EcoreBusinessElementImpl;
import org.cufir.s.data.dao.impl.EcoreCodeImpl;
import org.cufir.s.data.dao.impl.EcoreConstraintImpl;
import org.cufir.s.data.dao.impl.EcoreDataTypeImpl;
import org.cufir.s.data.dao.impl.EcoreExampleImpl;
import org.cufir.s.data.dao.impl.EcoreExternalSchemaImpl;
import org.cufir.s.data.dao.impl.EcoreMessageBuildingBlockImpl;
import org.cufir.s.data.dao.impl.EcoreMessageComponentImpl;
import org.cufir.s.data.dao.impl.EcoreMessageDefinitionImpl;
import org.cufir.s.data.dao.impl.EcoreMessageElementImpl;
import org.cufir.s.data.dao.impl.EcoreMessageSetDefinitionRLImpl;
import org.cufir.s.data.dao.impl.EcoreMessageSetImpl;
import org.cufir.s.data.dao.impl.EcoreNextVersionsImpl;
import org.cufir.s.data.dao.impl.EcoreSemanticMarkupElementImpl;
import org.cufir.s.data.dao.impl.EcoreSemanticMarkupImpl;

/**
 * dataImpl管理
 */
public class MrImplManager {

private static MrImplManager instance = new MrImplManager();
	
	private MrImplManager() {}
	
	public static MrImplManager get() {
		return instance;
	}
	
	private EcoreBusinessAreaImpl ecoreBusinessAreaImpl = new EcoreBusinessAreaImpl();
	private EcoreBusinessComponentImpl ecoreBusinessComponentImpl = new EcoreBusinessComponentImpl();
	private EcoreBusinessComponentRLImpl ecoreBusinessComponentRLImpl = new EcoreBusinessComponentRLImpl();
	private EcoreBusinessElementImpl ecoreBusinessElementImpl = new EcoreBusinessElementImpl();
	private EcoreCodeImpl ecoreCodeImpl = new EcoreCodeImpl();
	private EcoreConstraintImpl ecoreConstraintImpl = new EcoreConstraintImpl();
	private EcoreDataTypeImpl ecoreDataTypeImpl = new EcoreDataTypeImpl();
	private EcoreExampleImpl ecoreExampleImpl = new EcoreExampleImpl();
	private EcoreExternalSchemaImpl ecoreExternalSchemaImpl = new EcoreExternalSchemaImpl();
	private EcoreMessageBuildingBlockImpl ecoreMessageBuildingBlockImpl = new EcoreMessageBuildingBlockImpl();
	private EcoreMessageComponentImpl ecoreMessageComponentImpl = new EcoreMessageComponentImpl();
	private EcoreMessageDefinitionImpl ecoreMessageDefinitionImpl = new EcoreMessageDefinitionImpl();
	private EcoreMessageElementImpl ecoreMessageElementImpl = new EcoreMessageElementImpl();
	private EcoreMessageSetImpl ecoreMessageSetImpl = new EcoreMessageSetImpl();
	private EcoreMessageSetDefinitionRLImpl ecoreMessageSetDefinitionRLImpl = new EcoreMessageSetDefinitionRLImpl();
	private EcoreNextVersionsImpl ecoreNextVersionsImpl = new EcoreNextVersionsImpl();
	private EcoreSemanticMarkupElementImpl ecoreSemanticMarkupElementImpl = new EcoreSemanticMarkupElementImpl();
	private EcoreSemanticMarkupImpl ecoreSemanticMarkupImpl = new EcoreSemanticMarkupImpl();

	public EcoreBusinessAreaImpl getEcoreBusinessAreaImpl() {
		return ecoreBusinessAreaImpl;
	}

	public EcoreBusinessComponentImpl getEcoreBusinessComponentImpl() {
		return ecoreBusinessComponentImpl;
	}

	public EcoreBusinessComponentRLImpl getEcoreBusinessComponentRLImpl() {
		return ecoreBusinessComponentRLImpl;
	}

	public EcoreBusinessElementImpl getEcoreBusinessElementImpl() {
		return ecoreBusinessElementImpl;
	}

	public EcoreCodeImpl getEcoreCodeImpl() {
		return ecoreCodeImpl;
	}

	public EcoreConstraintImpl getEcoreConstraintImpl() {
		return ecoreConstraintImpl;
	}

	public EcoreDataTypeImpl getEcoreDataTypeImpl() {
		return ecoreDataTypeImpl;
	}

	public EcoreExampleImpl getEcoreExampleImpl() {
		return ecoreExampleImpl;
	}

	public EcoreExternalSchemaImpl getEcoreExternalSchemaImpl() {
		return ecoreExternalSchemaImpl;
	}

	public EcoreMessageBuildingBlockImpl getEcoreMessageBuildingBlockImpl() {
		return ecoreMessageBuildingBlockImpl;
	}

	public EcoreMessageComponentImpl getEcoreMessageComponentImpl() {
		return ecoreMessageComponentImpl;
	}

	public EcoreMessageDefinitionImpl getEcoreMessageDefinitionImpl() {
		return ecoreMessageDefinitionImpl;
	}

	public EcoreMessageElementImpl getEcoreMessageElementImpl() {
		return ecoreMessageElementImpl;
	}

	public EcoreMessageSetImpl getEcoreMessageSetImpl() {
		return ecoreMessageSetImpl;
	}

	public EcoreMessageSetDefinitionRLImpl getEcoreMessageSetDefinitionRLImpl() {
		return ecoreMessageSetDefinitionRLImpl;
	}

	public EcoreNextVersionsImpl getEcoreNextVersionsImpl() {
		return ecoreNextVersionsImpl;
	}

	public EcoreSemanticMarkupElementImpl getEcoreSemanticMarkupElementImpl() {
		return ecoreSemanticMarkupElementImpl;
	}

	public EcoreSemanticMarkupImpl getEcoreSemanticMarkupImpl() {
		return ecoreSemanticMarkupImpl;
	}

	
}
