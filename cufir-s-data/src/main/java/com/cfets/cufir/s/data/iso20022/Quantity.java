/**
 */
package com.cfets.cufir.s.data.iso20022;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quantity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A counted number of non-monetary units possibly including fractions
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.cfets.cufir.s.data.iso20022.Quantity#getUnitCode <em>Unit Code</em>}</li>
 * </ul>
 *
 * @see com.cfets.cufir.s.data.iso20022.Iso20022Package#getQuantity()
 * @model
 * @generated
 */
public interface Quantity extends Decimal {
	/**
	 * Returns the value of the '<em><b>Unit Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * qualifies the value of a Quantity
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unit Code</em>' attribute.
	 * @see #setUnitCode(String)
	 * @see com.cfets.cufir.s.data.iso20022.Iso20022Package#getQuantity_UnitCode()
	 * @model ordered="false"
	 * @generated
	 */
	String getUnitCode();

	/**
	 * Sets the value of the '{@link com.cfets.cufir.s.data.iso20022.Quantity#getUnitCode <em>Unit Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Code</em>' attribute.
	 * @see #getUnitCode()
	 * @generated
	 */
	void setUnitCode(String value);

} // Quantity
