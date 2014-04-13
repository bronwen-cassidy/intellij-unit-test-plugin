/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/TestedClassDef.java,v 1.5 2005/09/24 09:20:13 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/09/24 09:20:13 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;

public class TestedClassDef extends ClassDef {

   /* created test class defs or both ? */
   public Set testClasses = new HashSet();
   public Set testedMethods = new LinkedHashSet();

   public TestedClassDef(PsiClass psiClass) {
      super( psiClass );
   }

   public TestClassDef[] getTestClasses() {
      return (TestClassDef[]) testClasses.toArray(new TestClassDef[testClasses.size()]);
   }

   public void addTestClass(TestClassDef testClassDef) {
      testClasses.add(testClassDef);
      testClassDef.setTestedClass(this);
   }

   public TestedMethodDef[] getTestedClassMethods() {
      return (TestedMethodDef[]) testedMethods.toArray(new TestedMethodDef[testedMethods.size()]);
   }

   public void addTestedMethod(TestedMethodDef testedMethodDef) {
      testedMethodDef.setClassDef(this);
      testedMethods.add(testedMethodDef);
   }

   /**
    * Finds the method on the tested class
    * @param method
    * @return the found method as the TestedMethodDef or null if none found
    */
   public TestedMethodDef findMethod(PsiMethod method) {
      for (Iterator iterator = testedMethods.iterator(); iterator.hasNext();) {
         TestedMethodDef methodDef = (TestedMethodDef) iterator.next();
         if (methodDef.getPsiMethod().equals(method))
            return methodDef;
      }
      return null;
   }

   public boolean isTestClassCreated() {
      return getTestClasses()[0].getPsiClass() != null;
   }

   public TestClassDef getTestClassDef() {
      return getTestClasses()[0];
   }

   private List getTestDefsWithTests() {
      List testedTestClasses = new ArrayList();
      for (Iterator iterator = testClasses.iterator(); iterator.hasNext();) {
         TestClassDef testClassDef = (TestClassDef) iterator.next();
         if(testClassDef.hasTestClass()) testedTestClasses.add(testClassDef);
      }
      return testedTestClasses;
   }

   public TestClassDef[] getExistingTests() {
      List testedTestClasses = getTestDefsWithTests();
      return (TestClassDef[]) testedTestClasses.toArray(new TestClassDef[testedTestClasses.size()]);
   }

   public TestClassDef[] getUntestedTests() {
      List testedTestClasses = getTestDefsWithTests();
      return (TestClassDef[]) CollectionUtils.disjunction(testClasses, testedTestClasses).toArray(new TestClassDef[(testClasses.size() - testedTestClasses.size())]);
   }

   public boolean hasExistingTestClasses() {
      return getExistingTests().length > 0;
   }

   public boolean hasUntestedTestClasses() {
      return getUntestedTests().length > 0;
   }

   public TestClassDef getTestClassDef(PsiClass originClass) {
      for (int i = 0; i < getExistingTests().length; i++) {
         TestClassDef testClassDef = getExistingTests()[i];
         if(originClass.getQualifiedName().equals(testClassDef.getQualifiedName())) {
            if(!originClass.equals(testClassDef.getPsiClass())) testClassDef.setPsiClass(originClass);
            return testClassDef;
         }
      }
      return null;
   }

   public boolean hasCreatedClass() {
      return getPsiClass() != null;
   }
}