package org.cufir.s.ecore.iso20022.analysis;

import org.cufir.s.ecore.iso20022.RepositoryConcept;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * 解析Common
 * @author zqh
 *
 */
public class CommonAnalysis {
	
	/**
	 * 解析Common
	 * @param uuid
	 * @param type
	 * @param obj
	 * @param resource
	 */
	public static void parse(String uuid,String type,RepositoryConcept obj,XMIResource resource) {
		//解析Example
		if(obj.getExample()!=null && !obj.getExample().isEmpty()) {
			ExampleAnalysis.parse(uuid,type,obj.getExample());
		}
		//解析Constraint
		if(obj.getConstraint()!=null && !obj.getConstraint().isEmpty()) {
			ConstraintAnalysis.parse(uuid,type,obj.getConstraint(),resource);
		}
		//解析Doclet
		if(obj.getDoclet()!= null && !obj.getDoclet().isEmpty()) {
			DocletAnalysis.parse(uuid,type, obj.getDoclet(), resource);
		}
		//解析NextVersions
		if(obj.getNextVersions()!= null && !obj.getNextVersions().isEmpty()) {
			NextVersionsAnalysis.parse(uuid, type, obj.getNextVersions(), resource);
		}
		//解析SemanticMarkup
		if(obj.getSemanticMarkup()!=null && !obj.getSemanticMarkup().isEmpty()) {
			SemanticMarkupAnalysis.parse(uuid,type, obj.getSemanticMarkup(), resource);
		}
	}
}
