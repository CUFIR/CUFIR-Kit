/**
 */
package org.cufir.s.ecore.iso20022.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;
import java.util.Map;

import org.cufir.s.ecore.iso20022.Encoding;
import org.cufir.s.ecore.iso20022.Iso20022Package;
import org.cufir.s.ecore.iso20022.MessageSet;
import org.cufir.s.ecore.iso20022.Syntax;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Syntax</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.cufir.s.ecore.iso20022.impl.SyntaxImpl#getPossibleEncodings <em>Possible Encodings</em>}</li>
 *   <li>{@link org.cufir.s.ecore.iso20022.impl.SyntaxImpl#getGeneratedFor <em>Generated For</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SyntaxImpl extends ModelEntityImpl implements Syntax {
	/**
	 * The cached value of the '{@link #getPossibleEncodings() <em>Possible Encodings</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPossibleEncodings()
	 * @generated
	 * @ordered
	 */
	protected EList<Encoding> possibleEncodings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SyntaxImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Iso20022Package.eINSTANCE.getSyntax();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Encoding> getPossibleEncodings() {
		if (possibleEncodings == null) {
			possibleEncodings = new EObjectWithInverseResolvingEList<Encoding>(Encoding.class, this, Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS, Iso20022Package.ENCODING__SYNTAX);
		}
		return possibleEncodings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MessageSet> getGeneratedFor() {
		// TODO: implement this method to return the 'Generated For' reference list
		// Ensure that you remove @generated or mark it @generated NOT
		// The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and org.eclipse.emf.ecore.EStructuralFeature.Setting
		// so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean GeneratedForDerivation(Map context, DiagnosticChain diagnostics) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPossibleEncodings()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				return ((InternalEList<?>)getPossibleEncodings()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				return getPossibleEncodings();
			case Iso20022Package.SYNTAX__GENERATED_FOR:
				return getGeneratedFor();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				getPossibleEncodings().clear();
				getPossibleEncodings().addAll((Collection<? extends Encoding>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				getPossibleEncodings().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Iso20022Package.SYNTAX__POSSIBLE_ENCODINGS:
				return possibleEncodings != null && !possibleEncodings.isEmpty();
			case Iso20022Package.SYNTAX__GENERATED_FOR:
				return !getGeneratedFor().isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case Iso20022Package.SYNTAX___GENERATED_FOR_DERIVATION__MAP_DIAGNOSTICCHAIN:
				return GeneratedForDerivation((Map)arguments.get(0), (DiagnosticChain)arguments.get(1));
		}
		return super.eInvoke(operationID, arguments);
	}

} //SyntaxImpl
