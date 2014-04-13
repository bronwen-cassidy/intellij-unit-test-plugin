/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassUntestedMethodsLocator.java,v 1.6 2005/09/24 09:20:13 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/09/24 09:20:13 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;

import java.util.HashSet;
import java.util.Set;

//DEBT The filtering needs to go to the XXXDefFactory

public class ClassUntestedMethodsLocator {
   private ClassLocator classLocator;
   private MethodLocator methodLocator;
   private boolean testGetters = false;
   private boolean testSetters = false;

   public ClassUntestedMethodsLocator(ClassLocator classLocator, MethodLocator methodLocator) {
      this.classLocator = classLocator;
      this.methodLocator = methodLocator;
   }

   private boolean isPrivate(PsiMethod method) {
      return method.getModifierList().hasModifierProperty(PsiModifier.PRIVATE);
   }

   private boolean isAbstract(PsiMethod method) {
      return method.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT);
   }

   private boolean matchesExcludedPattern(PsiMethod method) {
      return isExcludedGetter(method) || isExcludedSetter(method);
   }

   private boolean isExcludedSetter(PsiMethod method) {
      return !testSetters && method.getName().startsWith("set");
   }

   private boolean isExcludedGetter(PsiMethod method) {
      return !testGetters && (method.getName().startsWith("get") || method.getName().startsWith("is"));
   }

   public boolean isTestSetters() {
      return testSetters;
   }

   public boolean isTestGetters() {
      return testGetters;
   }

   public void setOptions(boolean testSetters, boolean testGetters) {
      this.testSetters = testSetters;
      this.testGetters = testGetters;
   }
   
   public Set getUntestedMethods(TestedClassDef testedClassDef, TestClassDef testClassDef) {
      return getTestableUntestedMethods(testedClassDef, testClassDef, new TestClassDef[0]);
   }

   private Set getTestableUntestedMethods(TestedClassDef testedClassDef, TestClassDef testClassDef, TestClassDef[] testClassDefs) {
      Set untestedMethods = new HashSet();

      TestedMethodDef[] testedMethodDefs = testedClassDef.getTestedClassMethods();

      for (int i = 0; i < testedMethodDefs.length; i++) {
         TestedMethodDef testedMethodDef = testedMethodDefs[i];
         if ((isMethodTestable(testedMethodDef)) && (!isMethodTested(testedMethodDef, testClassDef)) && (!isMethodInOneTest(testClassDefs, testedMethodDef))) {
            untestedMethods.add(testedMethodDef);
         }
      }
      return untestedMethods;
   }

   public Set getUntestedMethods(PsiClass testedClass) {
      TestedClassDef testedClassDef = classLocator.getTestedClassDef(testedClass);
      Set untestedInNoneMethods = new HashSet();
      if(testedClassDef == null) return untestedInNoneMethods;
      TestClassDef[] testClassDefs = testedClassDef.getTestClasses();
      for (int i = 0; i < testClassDefs.length; i++) {
         TestClassDef testClassDef = testClassDefs[i];
         untestedInNoneMethods.addAll(getTestableUntestedMethods(testedClassDef, testClassDef, testClassDefs));

      }
      return untestedInNoneMethods;
   }

   private boolean isMethodTested(TestedMethodDef testedClassMethod, TestClassDef testClassDef) {
      if (testClassDef.getPsiClass() == null) return false;
      TestMethodDef[] testMethods = methodLocator.findTestMethods(testedClassMethod, testClassDef);
      return testMethods != null && testMethods.length > 0;
   }

   private boolean isMethodTestable(TestedMethodDef testedClassMethod) {
      PsiMethod method = testedClassMethod.getPsiMethod();
      return !isPrivate(method) && !isAbstract(method) && !matchesExcludedPattern(method);
   }

   private boolean isMethodInOneTest(TestClassDef[] testClassDefs,
                                     TestedMethodDef testedMethodDef) {
      for (int i = 0; i < testClassDefs.length; i++) {
         TestClassDef testClassDef = testClassDefs[i];
         if (isMethodTested(testedMethodDef, testClassDef)) {
            return true;
         }
      }
      return false;
   }
}