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

import com.sun.tools.javac.code.Attribute;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Ryan Heaton
 */
public class BeanValidationUtils {

  private BeanValidationUtils() {}

  public static boolean isNotNull(Element el, String ... ignoredValidationGroups) {
    return isNotNull(el, true, ignoredValidationGroups);
  }

  private static boolean isNotNull(Element el, boolean recurse, String ... ignoredValidationGroups) {
    if (getActiveValidation(el, NotNull.class, ignoredValidationGroups) != null) {
      return true;
    }
    List<? extends AnnotationMirror> annotations = el.getAnnotationMirrors();
    for (AnnotationMirror annotation : annotations) {
      DeclaredType annotationType = annotation.getAnnotationType();
      if (annotationType != null) {
        Element annotationElement = annotationType.asElement();
        if (getActiveValidation(annotationElement, NotNull.class, ignoredValidationGroups) != null) {
          return true;
        }
        if (recurse && isNotNull(annotationElement, false, ignoredValidationGroups)) {
          return true;
        }
      }
    }

    return false;
  }

  private static <A extends Annotation> A getActiveValidation(Element el, Class<A> annotationClass, String ... ignoredValidationGroups) {
    String annotationClassName = annotationClass.getName();
    for (AnnotationMirror annotationMirror : el.getAnnotationMirrors()) {
      if (annotationMirror.getAnnotationType().toString().equals(annotationClassName)) {
        return isActiveValidationAnnotation(annotationMirror, ignoredValidationGroups) ? el.getAnnotation(annotationClass) : null;
      }
    }
    return null;
  }

  private static boolean isActiveValidationAnnotation(AnnotationMirror annotationMirror, String ... ignoredValidationGroups) {
    List<String> ignored = Arrays.asList(ignoredValidationGroups);
    for (String validationGroup : getValidationGroups(annotationMirror)) {
      if (ignored.contains(validationGroup)) {
        return false;
      }
    }
    return true;
  }

  private static List<String> getValidationGroups(AnnotationMirror annotationMirror) {
    for (ExecutableElement value : annotationMirror.getElementValues().keySet()) {
      if (value.getSimpleName().toString().equals("groups")) {
        AnnotationValue annotationValue = annotationMirror.getElementValues().get(value);
        List<Attribute.Class> groupAttributes = (List<Attribute.Class>) annotationValue.getValue();
        List<String> groups = new ArrayList(groupAttributes.size());
        for (Attribute.Class groupAttribute : groupAttributes) {
          groups.add((groupAttribute.getValue().toString()));
        }
        return groups;
      }
    }
    return Collections.emptyList();
  }

  public static boolean hasConstraints(Element el, boolean required, String ... ignoredValidationGroups) {
    if (required) {
      return true;
    }

    List<? extends AnnotationMirror> annotations = el.getAnnotationMirrors();
    for (AnnotationMirror annotation : annotations) {
      DeclaredType annotationType = annotation.getAnnotationType();
      if (annotationType != null) {
        Element annotationElement = annotationType.asElement();
        if (annotationElement != null) {
          Element pckg = annotationElement.getEnclosingElement();
          if (pckg instanceof PackageElement && ((PackageElement)pckg).getQualifiedName().toString().equals("javax.validation.constraints")) {
            return isActiveValidationAnnotation(annotation, ignoredValidationGroups);
          }
        }
      }
    }

    return false;
  }

  public static String describeConstraints(Element el, boolean required, String ... ignoredValidationGroups) {
    Null mustBeNull = el.getAnnotation(Null.class);
    if (mustBeNull != null) {
      return "must be null";
    }

    List<String> constraints = new ArrayList<String>();
    required = required || getActiveValidation(el, NotNull.class, ignoredValidationGroups) != null;
    if (required) {
      constraints.add("required");
    }

    AssertFalse mustBeFalse = getActiveValidation(el, AssertFalse.class, ignoredValidationGroups);
    if (mustBeFalse != null) {
      constraints.add("must be \"false\"");
    }

    AssertTrue mustBeTrue = getActiveValidation(el, AssertTrue.class, ignoredValidationGroups);
    if (mustBeTrue != null) {
      constraints.add("must be \"true\"");
    }

    DecimalMax decimalMax = getActiveValidation(el, DecimalMax.class, ignoredValidationGroups);
    if (decimalMax != null) {
      constraints.add("max: " + decimalMax.value() + (decimalMax.inclusive() ? "" : " (exclusive)"));
    }

    DecimalMin decimalMin = getActiveValidation(el, DecimalMin.class, ignoredValidationGroups);
    if (decimalMin != null) {
      constraints.add("min: " + decimalMin.value() + (decimalMin.inclusive() ? "" : " (exclusive)"));
    }

    Digits digits = getActiveValidation(el, Digits.class, ignoredValidationGroups);
    if (digits != null) {
      constraints.add("max digits: " + digits.integer() + " (integer), " + digits.fraction() + " (fraction)");
    }

    Future dateInFuture = getActiveValidation(el, Future.class, ignoredValidationGroups);
    if (dateInFuture != null) {
      constraints.add("future date");
    }

    Max max = getActiveValidation(el, Max.class, ignoredValidationGroups);
    if (max != null) {
      constraints.add("max: " + max.value());
    }

    Min min = getActiveValidation(el, Min.class, ignoredValidationGroups);
    if (min != null) {
      constraints.add("min: " + min.value());
    }

    Past dateInPast = getActiveValidation(el, Past.class, ignoredValidationGroups);
    if (dateInPast != null) {
      constraints.add("past date");
    }

    Pattern mustMatchPattern = getActiveValidation(el, Pattern.class, ignoredValidationGroups);
    if (mustMatchPattern != null) {
      constraints.add("regex: " + mustMatchPattern.regexp());
    }

    Size size = getActiveValidation(el, Size.class, ignoredValidationGroups);
    if (size != null) {
      constraints.add("max size: " + size.max() + ", min size: " + size.min());
    }

    if (constraints.isEmpty()) {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    Iterator<String> it = constraints.iterator();
    while (it.hasNext()) {
      String token = it.next();
      builder.append(token);
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    return builder.toString();
  }
}
