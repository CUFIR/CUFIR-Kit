/**
 */
package org.cufir.s.ecore.iso20022.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Map;

import org.cufir.s.ecore.iso20022.ChoiceComponent;
import org.cufir.s.ecore.iso20022.Iso20022Package;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Choice Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ChoiceComponentImpl extends MessageElementContainerImpl implements ChoiceComponent {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChoiceComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Iso20022Package.eINSTANCE.getChoiceComponent();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean AtLeastOneProperty(Map context, DiagnosticChain diagnostics) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case Iso20022Package.CHOICE_COMPONENT___AT_LEAST_ONE_PROPERTY__MAP_DIAGNOSTICCHAIN:
				return AtLeastOneProperty((Map)arguments.get(0), (DiagnosticChain)arguments.get(1));
		}
		return super.eInvoke(operationID, arguments);
	}

} //ChoiceComponentImpl
