/**
 */
package org.cufir.s.ecore.iso20022;

import java.lang.String;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>ISO20022 Version</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Enumeration of the version of ISO20022
 * <!-- end-model-doc -->
 * @see org.cufir.s.ecore.iso20022.Iso20022Package#getISO20022Version()
 * @model
 * @generated
 */
public enum ISO20022Version implements Enumerator {
	/**
	 * The '<em><b>2004</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Version 2004 of ISO20022
	 * <!-- end-model-doc -->
	 * @see #_2004_VALUE
	 * @generated
	 * @ordered
	 */
	_2004(0, "_2004", "2004"),

	/**
	 * The '<em><b>2013</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Version 2013 of ISO20022
	 * <!-- end-model-doc -->
	 * @see #_2013_VALUE
	 * @generated
	 * @ordered
	 */
	_2013(1, "_2013", "2013");

	/**
	 * The '<em><b>2004</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Version 2004 of ISO20022
	 * <!-- end-model-doc -->
	 * @see #_2004
	 * @model literal="2004"
	 * @generated
	 * @ordered
	 */
	public static final int _2004_VALUE = 0;

	/**
	 * The '<em><b>2013</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Version 2013 of ISO20022
	 * <!-- end-model-doc -->
	 * @see #_2013
	 * @model literal="2013"
	 * @generated
	 * @ordered
	 */
	public static final int _2013_VALUE = 1;

	/**
	 * An array of all the '<em><b>ISO20022 Version</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ISO20022Version[] VALUES_ARRAY =
		new ISO20022Version[] {
			_2004,
			_2013,
		};

	/**
	 * A public read-only list of all the '<em><b>ISO20022 Version</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ISO20022Version> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>ISO20022 Version</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ISO20022Version get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ISO20022Version result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ISO20022 Version</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ISO20022Version getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ISO20022Version result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ISO20022 Version</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ISO20022Version get(int value) {
		switch (value) {
			case _2004_VALUE: return _2004;
			case _2013_VALUE: return _2013;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private ISO20022Version(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //ISO20022Version
