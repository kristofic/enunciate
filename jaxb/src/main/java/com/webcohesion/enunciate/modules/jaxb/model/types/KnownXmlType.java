/**
 * Copyright © 2006-2016 Web Cohesion (info@webcohesion.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webcohesion.enunciate.modules.jaxb.model.types;

import com.sun.xml.internal.xsom.XSFacet;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Set of known xml types.
 *
 * @author Ryan Heaton
 */
public enum KnownXmlType implements XmlType {

  //known xml-schema types.
  STRING("string", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  NORMALIZED_STRING("normalizedString", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  TOKEN("token", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  BASE64_BINARY("base64Binary", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  HEX_BINARY("hexBinary", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  INTEGER("integer", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  POSITIVE_INTEGER("positiveInteger", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  NEGATIVE_INTEGER("negativeInteger", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  NONPOSITIVE_INTEGER("nonPositiveInteger", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  NONNEGATIVE_INTEGER("nonNegativeInteger", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  LONG("long", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  UNSIGNED_LONG("unsignedLong", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  INT("int", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  UNSIGNED_INT("unsignedInt", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  SHORT("short", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  UNSIGNED_SHORT("unsignedShort", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  BYTE("byte", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  UNSIGNED_BYTE("unsignedByte", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  DECIMAL("decimal", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_TOTALDIGITS, XSFacet.FACET_FRACTIONDIGITS, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  FLOAT("float", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  DOUBLE("double", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  BOOLEAN("boolean", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_PATTERN),
  DURATION("duration", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  DATE_TIME("dateTime", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  DATE("date", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  TIME("time", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  GYEAR("gYear", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  GYEAR_MONTH("gYearMonth", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  GMONTH("gMonth", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  GMONTH_DAY("gMonthDay", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  GDAY("gDay", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE, XSFacet.FACET_MAXINCLUSIVE, XSFacet.FACET_MAXEXCLUSIVE, XSFacet.FACET_MININCLUSIVE, XSFacet.FACET_MINEXCLUSIVE),
  NAME("Name", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  QNAME("QName", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  NCNAME("NCName", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  ANY_URI("anyURI", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  ANY_SIMPLE_TYPE("anySimpleType", "http://www.w3.org/2001/XMLSchema"),
  ANY_TYPE("anyType", "http://www.w3.org/2001/XMLSchema"),
  LANGUAGE("language", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  ID("ID", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  IDREF("IDREF", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  IDREFS("IDREFS", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  ENTITY("ENTITY", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  ENTITIES("ENTITIES", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  NOTATION("NOTATION", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  NMTOKEN("NMTOKEN", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),
  NMTOKENS("NMTOKENS", "http://www.w3.org/2001/XMLSchema", XSFacet.FACET_LENGTH, XSFacet.FACET_MINLENGTH, XSFacet.FACET_MAXLENGTH, XSFacet.FACET_PATTERN, XSFacet.FACET_ENUMERATION, XSFacet.FACET_WHITESPACE),

  //others...
  SWAREF("swaRef", "http://ws-i.org/profiles/basic/1.1/xsd");

  private final String name;
  private final String namespace;
  private final Set<String> applicableFacets;

  KnownXmlType(String name, String namespace, String ... applicableFacets) {
    this.name = name;
    this.namespace = namespace;
    this.applicableFacets = new HashSet<String>(Arrays.asList(applicableFacets));
  }

  /**
   * The name of the known type.
   *
   * @return The name of the known type.
   */
  public String getName() {
    return name;
  }

  /**
   * The name of the known type.
   *
   * @return The name of the known type.
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * The qname of the known type.
   *
   * @return The qname of the known type.
   */
  public QName getQname() {
    return new QName(getNamespace(), getName());
  }

  /**
   * The definition of this type is never anonymous.
   *
   * @return The definition of this type is never anonymous.
   */
  public boolean isAnonymous() {
    return false;
  }

  /**
   * The only known type that is not simple is xs:anyType.
   *
   * @return true
   */
  public boolean isSimple() {
    return !equals(KnownXmlType.ANY_TYPE);
  }

  /**
   * The definition of this type is never restricted.
   *
   * @return The definition of this type is never restricted.
   */
  public boolean isRestricted() {
    return false;
  }

  public boolean isFacetApplicable(String facet) {
    return applicableFacets.contains(facet);
  }
}
