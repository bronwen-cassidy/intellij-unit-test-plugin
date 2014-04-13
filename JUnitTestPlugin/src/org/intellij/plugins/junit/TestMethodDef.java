/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/TestMethodDef.java,v 1.3 2005/08/24 06:24:36 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/24 06:24:36 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiMethod;

public class TestMethodDef extends MethodDef {
   public TestedMethodDef testedMethod = null;

   public TestMethodDef(String name) {
      super(name);
   }

   public TestMethodDef(PsiMethod testMethod) {
      super(testMethod);
   }

   public TestMethodDef(TestClassDef testClassDef, PsiMethod testMethod) {
      super(testClassDef, testMethod);
   }

   public TestedMethodDef getTestedMethod() {
      return testedMethod;
   }

   public void setTestedMethod(TestedMethodDef testedMethod) {
      this.testedMethod = testedMethod;
   }
}