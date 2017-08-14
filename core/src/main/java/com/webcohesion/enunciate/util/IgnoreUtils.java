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
package com.webcohesion.enunciate.util;

import java.util.List;

import javax.lang.model.element.Element;

import com.webcohesion.enunciate.javac.decorations.element.DecoratedElement;
import com.webcohesion.enunciate.javac.javadoc.JavaDoc.JavaDocTagList;
import com.webcohesion.enunciate.metadata.Ignore;

/**
 * @author Ryan Heaton
 */
public class IgnoreUtils {

  private IgnoreUtils() {}

  public static boolean isIgnored(Element element) {
    if (element instanceof DecoratedElement) {
      List<JavaDocTagList> ignoreTags = AnnotationUtils.getJavaDocTags("ignore", (DecoratedElement<?>) element);
      if (!ignoreTags.isEmpty()) {
        return true;
      }
    }

    return element.getAnnotation(Ignore.class) != null;
  }
}
