/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/ClassTemplateContextBuilder.java,v 1.5 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.PsiMethod;
import org.apache.velocity.VelocityContext;
import org.intellij.plugins.junit.ClassDef;
import org.intellij.plugins.junit.TestClassDef;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassTemplateContextBuilder {

   private TestClassDef testClassDef;
   public static final String DATE_KEY = "DATE";
   public static final String TIME_KEY = "TIME";
   public static final String USER_KEY = "USER";
   public static final String TESTED_CLASS_NAME = "TESTED_CLASS_NAME";
   public static final String TESTED_CLASS_PACKAGE_NAME = "TESTED_CLASS_PACKAGE_NAME";
   public static final String TESTED_CLASS_VARIABLE_NAME = "TESTED_CLASS_VARIABLE_NAME";
   public static final String CLASS_NAME = "NAME";
   public static final String PACKAGE_NAME = "PACKAGE_NAME";

   public VelocityContext getContext(TestClassDef testClassDef) {
      this.testClassDef = testClassDef;
      return getContext();
   }

   public VelocityContext getContext() {
      VelocityContext result = new VelocityContext();
      Date date = new Date();
      result.put(DATE_KEY, DateFormat.getDateInstance().format(date));
      result.put(TIME_KEY, DateFormat.getTimeInstance().format(date));
      result.put(USER_KEY, System.getProperty("user.name"));
      result.put(TESTED_CLASS_NAME, getTestedClassName());
      result.put(TESTED_CLASS_PACKAGE_NAME, getTestedClassPackageName());
      result.put(TESTED_CLASS_VARIABLE_NAME, getInstanceName());
      result.put(CLASS_NAME, testClassDef.getName());
      result.put(PACKAGE_NAME, testClassDef.getPackageName());

      return result;
   }

   private String getInstanceName() {
      return testClassDef.getTestedClassFieldName();
   }

   public String getExtendsClass() { return "junit.framework.TestCase"; }

   public String getTestedClassName() {
      return getTestedClassDef().getName(); }

   public String getTestedClassPackageName() {
      return getTestedClassDef().getPackageName();
   }

   //TODO Finish providing access to all tested methods
   public List getMethods() {
      List methods = new ArrayList();
      PsiMethod[] psiMethods = getTestedClassDef().getPsiClass().getMethods();
      for (int i = 0; i < psiMethods.length; i++) {
         PsiMethod psiMethod = psiMethods[i];
         String name = psiMethod.getName();
         methods.add(getTestFormattedMethodName(name));
      }
      return methods;
   }

   //todo remove duplication
   public static String getTestFormattedMethodName(String methodName) {
      return "test" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
   }


   public ClassDef getTestedClassDef() {
      return testClassDef.getTestedClass();
   }
}
