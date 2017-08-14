package com.webcohesion.enunciate.modules.jaxb.model.types;

import com.webcohesion.enunciate.modules.jaxb.model.Restriction;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Decorator for xml types with restrictions.
 *
 * @author Andrej Kristofic
 */
public class RestrictedXmlType implements XmlType {

	private final XmlType baseType;
	private final List<Restriction> restrictions;

	public RestrictedXmlType(XmlType baseType, List<Restriction> restrictions) {
		if (baseType == null) {
			throw new IllegalArgumentException("A base type must be supplied.");
		}
		this.baseType = baseType;
		this.restrictions = restrictions;
	}

	public String getName() {
		return baseType.getName();
	}

	public String getNamespace() {
		return baseType.getNamespace();
	}

	public QName getQname() {
		return baseType.getQname();
	}

	public boolean isAnonymous() {
		return baseType.isAnonymous();
	}

	public boolean isSimple() {
		return baseType.isSimple();
	}

	/**
	 * The definition of this type is always restricted.
	 *
	 * @return The definition of this type is always restricted.
	 */
	public boolean isRestricted() {
		return true;
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}
}
