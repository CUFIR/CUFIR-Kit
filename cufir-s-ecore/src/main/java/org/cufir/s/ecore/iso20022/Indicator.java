/**
 */
package org.cufir.s.ecore.iso20022;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Indicator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A list of exactly two mutually exclusive values that express the only two possible states of an instance of an object.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.cufir.s.ecore.iso20022.Indicator#getMeaningWhenTrue <em>Meaning When True</em>}</li>
 *   <li>{@link org.cufir.s.ecore.iso20022.Indicator#getMeaningWhenFalse <em>Meaning When False</em>}</li>
 * </ul>
 *
 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getIndicator()
 * @model
 * @generated
 */
public interface Indicator extends org.cufir.s.ecore.iso20022.Boolean {
	/**
	 * Returns the value of the '<em><b>Meaning When True</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Provides the semantic meaning when the Indicator is set to true.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Meaning When True</em>' attribute.
	 * @see #setMeaningWhenTrue(String)
	 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getIndicator_MeaningWhenTrue()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	String getMeaningWhenTrue();

	/**
	 * Sets the value of the '{@link org.cufir.s.ecore.iso20022.Indicator#getMeaningWhenTrue <em>Meaning When True</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meaning When True</em>' attribute.
	 * @see #getMeaningWhenTrue()
	 * @generated
	 */
	void setMeaningWhenTrue(String value);

	/**
	 * Returns the value of the '<em><b>Meaning When False</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Provides the semantic meaning when the Indicator is set to false.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Meaning When False</em>' attribute.
	 * @see #setMeaningWhenFalse(String)
	 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getIndicator_MeaningWhenFalse()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	String getMeaningWhenFalse();

	/**
	 * Sets the value of the '{@link org.cufir.s.ecore.iso20022.Indicator#getMeaningWhenFalse <em>Meaning When False</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meaning When False</em>' attribute.
	 * @see #getMeaningWhenFalse()
	 * @generated
	 */
	void setMeaningWhenFalse(String value);

} // Indicator
