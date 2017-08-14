package com.webcohesion.enunciate.modules.jaxb.model;

/**
 * Restriction definition.
 *
 * @author Andrej Kristofic
 */
public class Restriction {

	private String name;
	private String value;

	public Restriction(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
