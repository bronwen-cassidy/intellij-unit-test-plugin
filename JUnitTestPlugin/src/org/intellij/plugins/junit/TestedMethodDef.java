/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/TestedMethodDef.java,v 1.2 2005/08/20 09:54:27 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/08/20 09:54:27 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TestedMethodDef extends MethodDef {

   public Set testMethods = new HashSet();

   public TestedMethodDef(PsiMethod method) {
      super(method);
   }

   public TestedMethodDef(TestedClassDef testedClassDef, PsiMethod testedMethod) {
      super(testedClassDef,testedMethod);
   }

   public TestMethodDef[] getTestMethods() {
      return (TestMethodDef[]) testMethods.toArray(new TestMethodDef[testMethods.size()]);
   }

   public void addTestMethod(TestMethodDef testMethodDef) {
      testMethodDef.setTestedMethod(this);
      testMethods.add(testMethodDef);      
   }
}