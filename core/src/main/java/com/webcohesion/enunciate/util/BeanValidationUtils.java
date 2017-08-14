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
import java.text.MessageFormat;
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

  public static List<Constraint> collectConstraints(Element el, String ... ignoredValidationGroups) {
    List<Constraint> constraints = new ArrayList<Constraint>();

    Null mustBeNull = getActiveValidation(el, Null.class, ignoredValidationGroups);
    if (mustBeNull != null) {
      constraints.add(new Constraint(ConstraintType.Null));
      return constraints;
    }

    NotNull notNull = getActiveValidation(el, NotNull.class, ignoredValidationGroups);
    if (notNull != null) {
      constraints.add(new Constraint(ConstraintType.NotNull));
    }

    AssertFalse mustBeFalse = getActiveValidation(el, AssertFalse.class, ignoredValidationGroups);
    if (mustBeFalse != null) {
      constraints.add(new Constraint(ConstraintType.False));
    }

    AssertTrue mustBeTrue = getActiveValidation(el, AssertTrue.class, ignoredValidationGroups);
    if (mustBeTrue != null) {
      constraints.add(new Constraint(ConstraintType.True));
    }

    DecimalMax decimalMax = getActiveValidation(el, DecimalMax.class, ignoredValidationGroups);
    if (decimalMax != null) {
      constraints.add(new Constraint(ConstraintType.DecimalMax, decimalMax.value(), decimalMax.inclusive()));
    }

    DecimalMin decimalMin = getActiveValidation(el, DecimalMin.class, ignoredValidationGroups);
    if (decimalMin != null) {
      constraints.add(new Constraint(ConstraintType.DecimalMin, decimalMin.value(), decimalMin.inclusive()));
    }

    Digits digits = getActiveValidation(el, Digits.class, ignoredValidationGroups);
    if (digits != null) {
      constraints.add(new Constraint(ConstraintType.Digits, digits.integer(), digits.fraction()));
    }

    Future dateInFuture = getActiveValidation(el, Future.class, ignoredValidationGroups);
    if (dateInFuture != null) {
      constraints.add(new Constraint(ConstraintType.Future));
    }

    Max max = getActiveValidation(el, Max.class, ignoredValidationGroups);
    if (max != null) {
      constraints.add(new Constraint(ConstraintType.Max, max.value()));
    }

    Min min = getActiveValidation(el, Min.class, ignoredValidationGroups);
    if (min != null) {
      constraints.add(new Constraint(ConstraintType.Min, min.value()));
    }

    Past dateInPast = getActiveValidation(el, Past.class, ignoredValidationGroups);
    if (dateInPast != null) {
      constraints.add(new Constraint(ConstraintType.Past));
    }

    Pattern mustMatchPattern = getActiveValidation(el, Pattern.class, ignoredValidationGroups);
    if (mustMatchPattern != null) {
      constraints.add(new Constraint(ConstraintType.Regexp, mustMatchPattern.regexp()));
    }

    Size size = getActiveValidation(el, Size.class, ignoredValidationGroups);
    if (size != null) {
      constraints.add(new Constraint(ConstraintType.Size, size.max(), size.min()));
    }

    return constraints;
  }

  public static String describeConstraints(Element el, boolean required, String ... ignoredValidationGroups) {
    List<Constraint> constraints = collectConstraints(el, ignoredValidationGroups);

    if (required) {
      addRequiredIfNotPresent(constraints);
    }

    if (constraints.isEmpty()) {
      return null;
    }

    StringBuilder builder = new StringBuilder();
    Iterator<Constraint> it = constraints.iterator();
    while (it.hasNext()) {
      String token = it.next().toString();
      builder.append(token);
      if (it.hasNext()) {
        builder.append(", ");
      }
    }
    return builder.toString();
  }

  private static void addRequiredIfNotPresent(List<Constraint> constraints) {
    for (Constraint constraint : constraints) {
      if (constraint.getType() == ConstraintType.NotNull) {
        return;
      }
    }

    constraints.add(0, new Constraint(ConstraintType.NotNull));
  }

  public static class Constraint {
    private ConstraintType type;
    private Object [] params;

    public Constraint(ConstraintType type, Object ... params) {
      this.type = type;
      this.params = params;
    }

    public ConstraintType getType() {
      return this.type;
    }

    public Object[] getParams() {
      return this.params;
    }

    @Override
    public String toString() {
      return this.type.getDescription(this.params);
    }
  }

  public enum ConstraintType {
    Null("must be null"),
    NotNull("required"),
    False("must be \"false\""),
    True("must be \"true\""),
    DecimalMax("max: {0}{1}") {
      @Override
      protected Object processParam(int i, Object param) {
        if (i == 1) {
          return Boolean.TRUE.equals(param) ? "" : " (exclusive)";
        } else {
          return param;
        }
      }
    },
    DecimalMin("min: {0}{1}") {
      @Override
      protected Object processParam(int i, Object param) {
        if (i == 1) {
          return Boolean.TRUE.equals(param) ? "" : " (exclusive)";
        } else {
          return param;
        }
      }
    },
    Digits("max digits: {0} (integer), {1} (fraction)"),
    Future("future date"),
    Max("max: {0}"),
    Min("min: {0}"),
    Past("past date"),
    Regexp("regex: {0}"),
    Size("max size: {0}, min size: {1}");

    private String description;

    ConstraintType(String description) {
      this.description = description;
    }

    public String getDescription(Object ... params) {
      return MessageFormat.format(this.description, processParams(params));
    }

    private Object [] processParams(Object ... params) {
      Object [] processedParams = new Object[params.length];
      for (int i = 0; i < params.length; i++) {
        processedParams[i] = processParam(i, params[i]);
      }
      return  processedParams;
    }

    protected Object processParam(int i, Object param) {
      return param;
    }
  }
}
