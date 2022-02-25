/**
 */
package org.cufir.s.ecore.iso20022;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schema Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Meta class for represensting XML Schema DataTypes
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.cufir.s.ecore.iso20022.SchemaType#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getSchemaType()
 * @model annotation="urn:iso:std:iso:20022:2013:ecore:extension basis='IMPLEMENTATION_ENHANCEMENT' description='Meta class for represensting XML Schema DataTypes'"
 * @generated
 */
public interface SchemaType extends DataType {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.cufir.s.ecore.iso20022.SchemaTypeKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.cufir.s.ecore.iso20022.SchemaTypeKind
	 * @see #setKind(SchemaTypeKind)
	 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getSchemaType_Kind()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	SchemaTypeKind getKind();

	/**
	 * Sets the value of the '{@link org.cufir.s.ecore.iso20022.SchemaType#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.cufir.s.ecore.iso20022.SchemaTypeKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(SchemaTypeKind value);

} // SchemaType
