/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/MethodTemplateContextBuilder.java,v 1.9 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.9 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.PsiMethod;
import org.apache.velocity.VelocityContext;
import org.intellij.plugins.junit.TestClassDef;

public class MethodTemplateContextBuilder {

   private PsiMethod testedMethod;
   private ClassTemplateContextBuilder classTemplateContextBuilder;

   public static final String METHOD_NAME = "METHOD_NAME";
   public static final String TESTED_METHOD = "TESTED_METHOD";
   public TestClassDef testClassDef;

   public MethodTemplateContextBuilder(TestClassDef testClassDef, PsiMethod testedMethod) {
      this.testedMethod = testedMethod;
      this.testClassDef = testClassDef;
      classTemplateContextBuilder = new ClassTemplateContextBuilder();
   }

   public VelocityContext getContext() {
      VelocityContext velocityContext = classTemplateContextBuilder.getContext(testClassDef);
      velocityContext.put(METHOD_NAME, getTestMethodName());
      velocityContext.put(TESTED_METHOD, new Method(testedMethod));
      return velocityContext;
   }

   //DEBT remove duplication
   private String getTestMethodName() {
      String methodName = testedMethod.getName();
      return "test" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
   }
}
