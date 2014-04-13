/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/TestClassDef.java,v 1.7 2006/02/27 16:37:51 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2006/02/27 16:37:51 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

import java.util.*;

public class TestClassDef extends ClassDef {

   private TestedClassDef testedClass;
   private String testedClassFieldName;
   private Set testMethods = new LinkedHashSet();

   public TestClassDef(PsiDirectory root, String packageName, String name, String testedClassFieldName) {
      super(root, packageName, name);
      this.testedClassFieldName = testedClassFieldName;
   }

   public TestClassDef(PsiClass psiClass, String testedClassFieldName) {
      super(psiClass);
      this.testedClassFieldName = testedClassFieldName;
   }

   public void setTestedClass(TestedClassDef testedClass) {
      this.testedClass = testedClass;
   }

   public TestedClassDef getTestedClass() {
      return testedClass;
   }

   public String getTestedClassFieldName() {
      return testedClassFieldName;
   }

   public TestMethodDef[] getTestMethods() {
      return (TestMethodDef[]) testMethods.toArray(new TestMethodDef[testMethods.size()]);
   }

   public void addTestMethod(TestMethodDef testMethodDef) {
      testMethodDef.setClassDef(this);
      testMethods.add(testMethodDef);
   }

   public boolean hasTestClass() {
      return psiClass != null;
   }

   public String getQualifiedName() {
      String packageName = getPackageName();
      return packageName.length() > 0 ? packageName + "." + getName() : getName();
   }

   public void setRoot(PsiDirectory root) {
      this.root = root;
   }

   public void setName(String name) {
      this.name = name;
   }
}