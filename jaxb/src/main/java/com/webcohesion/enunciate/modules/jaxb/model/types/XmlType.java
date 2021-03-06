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

import javax.xml.namespace.QName;

/**
 * Type mirror that provides its qname.
 *
 * @author Ryan Heaton
 */
public interface XmlType {

  /**
   * The (local) name of this xml type.
   *
   * @return The (local) name of this xml type.
   */
  String getName();

  /**
   * The namespace for this xml type.
   *
   * @return The namespace for this xml type.
   */
  String getNamespace();

  /**
   * The qname of the xml type mirror.
   *
   * @return The qname of the xml type mirror.
   */
  QName getQname();

  /**
   * Whether this type is anonymous.
   *
   * @return Whether this type is anonymous.
   */
  boolean isAnonymous();

  /**
   * Whether this is a simple XML type.
   *
   * @return Whether this is a simple XML type.
   */
  boolean isSimple();

  /**
   * Whether this is a XML type with restriction.
   *
   * @return Whether this is a XML type with restriction.
   */
  boolean isRestricted();
}
