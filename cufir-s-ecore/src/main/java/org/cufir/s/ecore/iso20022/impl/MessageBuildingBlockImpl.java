/**
 */
package org.cufir.s.ecore.iso20022.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Map;

import org.cufir.s.ecore.iso20022.DataType;
import org.cufir.s.ecore.iso20022.Iso20022Package;
import org.cufir.s.ecore.iso20022.MessageBuildingBlock;
import org.cufir.s.ecore.iso20022.MessageComponentType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message Building Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.cufir.s.ecore.iso20022.impl.MessageBuildingBlockImpl#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.cufir.s.ecore.iso20022.impl.MessageBuildingBlockImpl#getComplexType <em>Complex Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MessageBuildingBlockImpl extends MessageConstructImpl implements MessageBuildingBlock {
	/**
	 * The cached value of the '{@link #getSimpleType() <em>Simple Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimpleType()
	 * @generated
	 * @ordered
	 */
	protected DataType simpleType;

	/**
	 * The cached value of the '{@link #getComplexType() <em>Complex Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComplexType()
	 * @generated
	 * @ordered
	 */
	protected MessageComponentType complexType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MessageBuildingBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Iso20022Package.eINSTANCE.getMessageBuildingBlock();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataType getSimpleType() {
		if (simpleType != null && simpleType.eIsProxy()) {
			InternalEObject oldSimpleType = (InternalEObject)simpleType;
			simpleType = (DataType)eResolveProxy(oldSimpleType);
			if (simpleType != oldSimpleType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE, oldSimpleType, simpleType));
			}
		}
		return simpleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataType basicGetSimpleType() {
		return simpleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSimpleType(DataType newSimpleType) {
		DataType oldSimpleType = simpleType;
		simpleType = newSimpleType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE, oldSimpleType, simpleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageComponentType getComplexType() {
		if (complexType != null && complexType.eIsProxy()) {
			InternalEObject oldComplexType = (InternalEObject)complexType;
			complexType = (MessageComponentType)eResolveProxy(oldComplexType);
			if (complexType != oldComplexType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE, oldComplexType, complexType));
			}
		}
		return complexType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageComponentType basicGetComplexType() {
		return complexType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetComplexType(MessageComponentType newComplexType, NotificationChain msgs) {
		MessageComponentType oldComplexType = complexType;
		complexType = newComplexType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE, oldComplexType, newComplexType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComplexType(MessageComponentType newComplexType) {
		if (newComplexType != complexType) {
			NotificationChain msgs = null;
			if (complexType != null)
				msgs = ((InternalEObject)complexType).eInverseRemove(this, Iso20022Package.MESSAGE_COMPONENT_TYPE__MESSAGE_BUILDING_BLOCK, MessageComponentType.class, msgs);
			if (newComplexType != null)
				msgs = ((InternalEObject)newComplexType).eInverseAdd(this, Iso20022Package.MESSAGE_COMPONENT_TYPE__MESSAGE_BUILDING_BLOCK, MessageComponentType.class, msgs);
			msgs = basicSetComplexType(newComplexType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE, newComplexType, newComplexType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean MessageBuildingBlockHasExactlyOneType(Map context, DiagnosticChain diagnostics) {
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
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				if (complexType != null)
					msgs = ((InternalEObject)complexType).eInverseRemove(this, Iso20022Package.MESSAGE_COMPONENT_TYPE__MESSAGE_BUILDING_BLOCK, MessageComponentType.class, msgs);
				return basicSetComplexType((MessageComponentType)otherEnd, msgs);
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
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				return basicSetComplexType(null, msgs);
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
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE:
				if (resolve) return getSimpleType();
				return basicGetSimpleType();
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				if (resolve) return getComplexType();
				return basicGetComplexType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE:
				setSimpleType((DataType)newValue);
				return;
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				setComplexType((MessageComponentType)newValue);
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
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE:
				setSimpleType((DataType)null);
				return;
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				setComplexType((MessageComponentType)null);
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
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__SIMPLE_TYPE:
				return simpleType != null;
			case Iso20022Package.MESSAGE_BUILDING_BLOCK__COMPLEX_TYPE:
				return complexType != null;
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
			case Iso20022Package.MESSAGE_BUILDING_BLOCK___MESSAGE_BUILDING_BLOCK_HAS_EXACTLY_ONE_TYPE__MAP_DIAGNOSTICCHAIN:
				return MessageBuildingBlockHasExactlyOneType((Map)arguments.get(0), (DiagnosticChain)arguments.get(1));
		}
		return super.eInvoke(operationID, arguments);
	}

} //MessageBuildingBlockImpl
