/**
 */
package org.cufir.s.ecore.iso20022;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Code</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A character string (letters, figures or symbols) that for brevity and/or language independence may be used to represent or replace a definitive value or text of an attribute.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.cufir.s.ecore.iso20022.Code#getCodeName <em>Code Name</em>}</li>
 *   <li>{@link org.cufir.s.ecore.iso20022.Code#getOwner <em>Owner</em>}</li>
 * </ul>
 *
 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getCode()
 * @model
 * @generated
 */
public interface Code extends RepositoryConcept {
	/**
	 * Returns the value of the '<em><b>Code Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Provides the full, non-abbreviated name of the Code
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Code Name</em>' attribute.
	 * @see #setCodeName(String)
	 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getCode_CodeName()
	 * @model ordered="false"
	 * @generated
	 */
	String getCodeName();

	/**
	 * Sets the value of the '{@link org.cufir.s.ecore.iso20022.Code#getCodeName <em>Code Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code Name</em>' attribute.
	 * @see #getCodeName()
	 * @generated
	 */
	void setCodeName(String value);

	/**
	 * Returns the value of the '<em><b>Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.cufir.s.ecore.iso20022.CodeSet#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Direct reference to the CodeSet owning this Code.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owner</em>' container reference.
	 * @see #setOwner(CodeSet)
	 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getCode_Owner()
	 * @see org.cufir.s.ecore.iso20022.CodeSet#getCode
	 * @model opposite="code" required="true" transient="false" ordered="false"
	 * @generated
	 */
	CodeSet getOwner();

	/**
	 * Sets the value of the '{@link org.cufir.s.ecore.iso20022.Code#getOwner <em>Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owner</em>' container reference.
	 * @see #getOwner()
	 * @generated
	 */
	void setOwner(CodeSet value);

} // Code
