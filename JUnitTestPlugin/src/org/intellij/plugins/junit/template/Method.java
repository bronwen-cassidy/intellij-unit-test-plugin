/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/Method.java,v 1.4 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jacques
 * Date: Oct 12, 2003
 * Time: 12:57:23 AM
 */
public class Method {
   private String name;
   private List/*<Parameter>*/ parameters;
   private List/*<String>*/ exceptions;
   private String returnType;

   public Method(PsiMethod method) {
      this.parameters = getParameters(method);
      this.name = method.getName();
      this.returnType = (method.getReturnType() == null)?"void":method.getReturnType().getPresentableText();
      this.exceptions = getExceptions(method);
   }

   public String getName() { return name; }

   public List/*<Parameter>*/ getParameters() { return parameters; }

   public List/*<String>*/ getExceptions() { return exceptions; }

   public String getReturnType() { return returnType; }

   private List/*<Parameter>*/ getParameters(PsiMethod method) {
      List/*<Parameter>*/ params = new ArrayList/*<Parameter>*/();
      PsiParameterList psiParameterList = method.getParameterList();
      if (psiParameterList != null) {
         PsiParameter[] psiParams = psiParameterList.getParameters();
         for (int i = 0; i < psiParams.length; i++) {
            params.add(new Parameter(psiParams[i]));
         }
      }
      return params;
   }

   private List getExceptions(PsiMethod method) {
      List/*<String>*/ exceptions = new ArrayList/*<String>*/();
      PsiReferenceList throwsList = method.getThrowsList();
      PsiClassType[] referencedTypes = throwsList.getReferencedTypes();
      if (referencedTypes != null) {
         for (int i = 0; i < referencedTypes.length; i++) {
            exceptions.add(referencedTypes[i].getPresentableText());
         }
      }
      return exceptions;
   }
}
