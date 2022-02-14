package com.cfets.cufir.s.data.analysis;

import org.eclipse.emf.ecore.xmi.XMIResource;

import com.cfets.cufir.s.data.iso20022.RepositoryConcept;

/**
 * 解析Common
 * @author zqh
 *
 */
public class AnalysisIso20022Common {
	
	/**
	 * 解析Common
	 * @param uuid
	 * @param type
	 * @param obj
	 * @param resource
	 */
	public static void parseCommon(String uuid,String type,RepositoryConcept obj,XMIResource resource) {
		//解析Example
		if(obj.getExample()!=null && !obj.getExample().isEmpty()) {
			AnalysisIso20022Example.parseExample(uuid,type,obj.getExample());
		}
		//解析Constraint
		if(obj.getConstraint()!=null && !obj.getConstraint().isEmpty()) {
			AnalysisIso20022Constraint.parseConstraint(uuid,type,obj.getConstraint(),resource);
		}
		//解析Doclet
		if(obj.getDoclet()!= null && !obj.getDoclet().isEmpty()) {
			AnalysisIso20022Doclet.parseDoclet(uuid,type, obj.getDoclet(), resource);
		}
		//解析NextVersions
		if(obj.getNextVersions()!= null && !obj.getNextVersions().isEmpty()) {
			AnalysisIso20022NextVersions.parseNextVersions(uuid, type, obj.getNextVersions(), resource);
		}
		//解析SemanticMarkup
		if(obj.getSemanticMarkup()!=null && !obj.getSemanticMarkup().isEmpty()) {
			AnalysisIso20022SemanticMarkup.parseSemanticMarkup(uuid,type, obj.getSemanticMarkup(), resource);
		}
	}
	
}
