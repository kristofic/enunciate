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
import com.webcohesion.enunciate.EnunciateException;
import com.webcohesion.enunciate.javac.decorations.Annotations;
import com.webcohesion.enunciate.javac.decorations.TypeMirrorDecorator;
import com.webcohesion.enunciate.javac.decorations.type.DecoratedTypeMirror;
import com.webcohesion.enunciate.javac.decorations.type.TypeMirrorUtils;
import com.webcohesion.enunciate.modules.jaxb.EnunciateJaxbContext;
import com.webcohesion.enunciate.modules.jaxb.model.Accessor;
import com.webcohesion.enunciate.modules.jaxb.model.Restriction;
import com.webcohesion.enunciate.modules.jaxb.model.adapters.Adaptable;
import com.webcohesion.enunciate.util.BeanValidationUtils;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSchemaTypes;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * A decorator that decorates the relevant type mirrors as xml type mirrors.
 *
 * @author Ryan Heaton
 */
public class XmlTypeFactory {

  /**
   * Find the specified type of the given adaptable element, if it exists.
   *
   * @param adaptable The adaptable element for which to find the specified type.
   * @param context The context
   * @return The specified XML type, or null if it doesn't exist.
   */
  public static XmlType findSpecifiedType(Adaptable adaptable, EnunciateJaxbContext context) {
    XmlType xmlType = null;

    if (adaptable instanceof Accessor) {
      XmlSchemaType specified = ((Accessor) adaptable).getAnnotation(XmlSchemaType.class);
      if (specified != null) {
        return new SpecifiedXmlType(specified);
      }
    }

    if (adaptable.isAdapted()) {
      xmlType = getXmlType(adaptable.getAdapterType().getAdaptingType(), context);
    }
    else if (adaptable instanceof Accessor) {
      //The XML type of accessors can be explicitly defined...
      xmlType = findExplicitSchemaType((Accessor) adaptable, context);
    }

    return xmlType;
  }

  /**
   * Finds the explicit schema type for the given accessor.
   *
   * @param accessor    The accessor.
   * @param context The JAXB context.
   * @return The XML type, or null if none was specified.
   */
  public static XmlType findExplicitSchemaType(Accessor accessor, EnunciateJaxbContext context) {
    TypeMirror typeMirror = accessor.getCollectionItemType();

    XmlType xmlType = null;
    XmlSchemaType schemaType = accessor.getAnnotation(XmlSchemaType.class);
    if ((schemaType == null) && (typeMirror instanceof DeclaredType)) {
      PackageElement pckg = context.getContext().getProcessingEnvironment().getElementUtils().getPackageOf(accessor.getEnclosingElement());
      String packageName = pckg.getQualifiedName().toString();
      Map<String, XmlSchemaType> explicitTypes = context.getPackageSpecifiedTypes(packageName);
      if (explicitTypes == null) {
        explicitTypes = loadPackageExplicitTypes(pckg, context);
        context.setPackageSpecifiedTypes(packageName, explicitTypes);
      }

      schemaType = explicitTypes.get(((TypeElement) ((DeclaredType) typeMirror).asElement()).getQualifiedName().toString());
    }

    if (schemaType != null) {
      xmlType = new SpecifiedXmlType(schemaType);
    }

    return xmlType;
  }

  /**
   * Load any explicit schema types specified by the package.
   *
   * @param pckg The package.
   * @param context The context.
   * @return Any explicit schema types specified by the package.
   */
  protected static Map<String, XmlSchemaType> loadPackageExplicitTypes(PackageElement pckg, EnunciateJaxbContext context) {
    Map<String, XmlSchemaType> explicitTypes = new HashMap<String, XmlSchemaType>();

    XmlSchemaType schemaTypeInfo = pckg.getAnnotation(XmlSchemaType.class);
    XmlSchemaTypes schemaTypes = pckg.getAnnotation(XmlSchemaTypes.class);

    if ((schemaTypeInfo != null) || (schemaTypes != null)) {
      ArrayList<XmlSchemaType> allSpecifiedTypes = new ArrayList<XmlSchemaType>();
      if (schemaTypeInfo != null) {
        allSpecifiedTypes.add(schemaTypeInfo);
      }

      if (schemaTypes != null) {
        allSpecifiedTypes.addAll(Arrays.asList(schemaTypes.value()));
      }

      for (final XmlSchemaType specifiedType : allSpecifiedTypes) {
        DecoratedTypeMirror typeMirror = Annotations.mirrorOf(new Callable<Class<?>>() {
          @Override
          public Class<?> call() throws Exception {
            return specifiedType.type();
          }
        }, context.getContext().getProcessingEnvironment(), XmlSchemaType.DEFAULT.class);

        if (typeMirror == null) {
          throw new EnunciateException(pckg.getQualifiedName() + ": a type must be specified in " + XmlSchemaType.class.getName() + " at the package-level.");
        }

        if (!(typeMirror instanceof DeclaredType)) {
          throw new EnunciateException(pckg.getQualifiedName() + ": only a declared type can be adapted.  Offending type: " + typeMirror);
        }

        explicitTypes.put(((TypeElement)((DeclaredType)typeMirror).asElement()).getQualifiedName().toString(), specifiedType);
      }
    }

    return Collections.unmodifiableMap(explicitTypes);
  }

  /**
   * Get the XML type for the specified type mirror.
   *
   * @param typeMirror The type mirror.
   * @param context The context.
   * @return The xml type for the specified type mirror.
   */
  public static XmlType getXmlType(TypeMirror typeMirror, EnunciateJaxbContext context) {
    DecoratedTypeMirror decorated = (DecoratedTypeMirror) TypeMirrorDecorator.decorate(typeMirror, context.getContext().getProcessingEnvironment());
    XmlTypeVisitor visitor = new XmlTypeVisitor();
    TypeMirror componentType = TypeMirrorUtils.getComponentType(decorated, context.getContext().getProcessingEnvironment());
    componentType = componentType == null ? decorated : componentType;
    return componentType.accept(visitor, new XmlTypeVisitor.Context(context, decorated.isArray(), decorated.isCollection(), new LinkedList<String>()));
  }

  public static XmlType decorateWithRestrictions(XmlType baseType, Accessor accessor, EnunciateJaxbContext context) {
    List<Restriction> restrictions = getTypeRestrictions(baseType, accessor, context);
    if (restrictions.isEmpty()) {
      return baseType;
    }
    return new RestrictedXmlType(baseType, restrictions);
  }

  private static List<Restriction> getTypeRestrictions(XmlType baseType, Accessor accessor, EnunciateJaxbContext context) {
    List<BeanValidationUtils.Constraint> constraints = BeanValidationUtils.collectConstraints(accessor, context.getIgnoredValidationGroups());
    List<Restriction> restrictions = new ArrayList(constraints.size());
    for (BeanValidationUtils.Constraint constraint : constraints) {
      switch (constraint.getType()) {
        case False: {
          restrictions.add(new Restriction(XSFacet.FACET_PATTERN, "false"));
          break;
        }
        case True: {
          restrictions.add(new Restriction(XSFacet.FACET_PATTERN, "true"));
          break;
        }
        case DecimalMax: {
          boolean inclusive = Boolean.TRUE.equals(constraint.getParams()[1]);
          restrictions.add(new Restriction(inclusive ? XSFacet.FACET_MAXINCLUSIVE : XSFacet.FACET_MAXEXCLUSIVE, String.valueOf(constraint.getParams()[0])));
          break;
        }
        case DecimalMin: {
          boolean inclusive = Boolean.TRUE.equals(constraint.getParams()[1]);
          restrictions.add(new Restriction(inclusive ? XSFacet.FACET_MININCLUSIVE : XSFacet.FACET_MINEXCLUSIVE, String.valueOf(constraint.getParams()[0])));
          break;
        }
        case Digits: {
          int integer = (Integer) constraint.getParams()[0];
          int fraction = (Integer) constraint.getParams()[1];
          restrictions.add(new Restriction(XSFacet.FACET_TOTALDIGITS, String.valueOf(integer + fraction)));
          restrictions.add(new Restriction(XSFacet.FACET_FRACTIONDIGITS, String.valueOf(fraction)));
          break;
        }
        case Max: {
          if (baseType == KnownXmlType.STRING || baseType == KnownXmlType.NORMALIZED_STRING) {
            restrictions.add(new Restriction(XSFacet.FACET_MAXLENGTH, String.valueOf(constraint.getParams()[0])));
          } else {
            restrictions.add(new Restriction(XSFacet.FACET_MAXINCLUSIVE, String.valueOf(constraint.getParams()[0])));
          }
          break;
        }
        case Min: {
          if (baseType == KnownXmlType.STRING || baseType == KnownXmlType.NORMALIZED_STRING) {
            restrictions.add(new Restriction(XSFacet.FACET_MINLENGTH, String.valueOf(constraint.getParams()[0])));
          } else {
            restrictions.add(new Restriction(XSFacet.FACET_MININCLUSIVE, String.valueOf(constraint.getParams()[0])));
          }
          break;
        }
        case Regexp: {
          restrictions.add(new Restriction(XSFacet.FACET_PATTERN, String.valueOf(constraint.getParams()[0])));
          break;
        }
        case Size: {
          int max = (Integer) constraint.getParams()[0];
          int min = (Integer) constraint.getParams()[1];
          restrictions.add(new Restriction(XSFacet.FACET_MAXLENGTH, String.valueOf(max)));
          if (min > 0) {
            restrictions.add(new Restriction(XSFacet.FACET_MINLENGTH, String.valueOf(min)));
          }
          break;
        }
      }
    }

    return restrictions;
  }
}
