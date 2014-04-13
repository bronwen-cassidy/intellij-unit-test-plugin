/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodLocator.java,v 1.24 2005/08/21 16:59:11 shadow12 Exp $
 * $Revision: 1.24 $
 * $Date: 2005/08/21 16:59:11 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.junit.template.PsiMethodVelocityFactory;
import org.intellij.plugins.util.LogUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MethodLocator {

   private PsiMethodVelocityFactory psiMethodFactory;

   private static final Logger LOG = LogUtil.getLogger();

   public MethodLocator() { }

   public MethodLocator(PsiMethodVelocityFactory psiMethodFactory) {
      this.psiMethodFactory = psiMethodFactory;
   }

   public TestMethodDef[] findTestMethods(TestedMethodDef testedMethod, TestClassDef testClassDef) {
      TestMethodDef[] testMethods = getMethodsThatStartWithPrefix(testClassDef, getTestMethodName(testedMethod));
      List filteredMethods = new IsTestMethodFilter(testedMethod).filterMethods(testMethods);
      return (TestMethodDef[]) filteredMethods.toArray(new TestMethodDef[filteredMethods.size()]);
   }

   public TestMethodDef[] findTestMethods(TestClassDef testClassDef) {
      return getMethodsThatStartWithPrefix(testClassDef, JUnitHelper.TEST_METHOD_PREFIX);
   }

   public String getTestMethodName(PsiMethod renamedTestedMethod) {
      final String methodName = renamedTestedMethod.getName();
      return JUnitHelper.TEST_METHOD_PREFIX +
             methodName.substring(0, 1).toUpperCase() +
             methodName.substring(1);
   }

   class IsTestMethodFilter extends MethodFilter {
      private TestedMethodDef testedMethod;

      public IsTestMethodFilter(TestedMethodDef testedMethod) {
         this.testedMethod = testedMethod;
      }

      boolean accept(MethodDef method) {
         TestedClassDef testedClassDef = (TestedClassDef) testedMethod.getClassDef();
         MethodDef foundTestedMethod = findTestedMethod((TestMethodDef) method, testedClassDef);
         // check for identicle names
         if (foundTestedMethod != null) {
            if (foundTestedMethod.getName().equals(testedMethod.getName())) return true;
         }
         return foundTestedMethod == testedMethod;
      }
   }

   //todo remove duplication
   public String getTestMethodName(TestedMethodDef originMethod) {
      return getTestMethodName(originMethod.getPsiMethod());
   }

   private TestMethodDef[] getMethodsThatStartWithPrefix(TestClassDef testClassDef, String prefix) {
      TestMethodDef[] testMethods = testClassDef.getTestMethods();
      List filteredMethods = new MethodNameStartsWithPrefixFilter(prefix).filterMethods(testMethods);
      return (TestMethodDef[]) filteredMethods.toArray(new TestMethodDef[filteredMethods.size()]);
   }

   public TestedMethodDef findTestedMethod(TestMethodDef testMethod, TestedClassDef testedClassDef) {
      debug("searching for tested method of test method" + testMethod.getName());
      if (testMethod.getName().equals(JUnitHelper.TEST_METHOD_PREFIX)) return null;

      String testedMethodName = removeJUnitTestMethodPrefix(testMethod.getName());

      TestedMethodDef[] testedClassMethods = testedClassDef.getTestedClassMethods();

      TestedMethodDef[] methods = getMethodsThatHaveTheirNameAtBeginningOfString(testedClassMethods, testedMethodName);
      sortMethodsLongestNameFirst(methods);
      // todo return all? and have a select?
      return (TestedMethodDef) getFirstMethod(methods);
   }

   private MethodDef getFirstMethod(MethodDef[] methods) {
      if (methods != null && methods.length != 0) {
         return methods[0];
      } else {
         return null;
      }
   }

   private void sortMethodsLongestNameFirst(MethodDef[] methods) {
      Arrays.sort(methods, new Comparator() {

         public int compare(MethodDef o1,
                            MethodDef o2) {
            return o2.getName().length() - o1.getName().length();
         }

         public int compare(Object o1,
                            Object o2) {
            return compare((MethodDef) o1, (MethodDef) o2);
         }

         public boolean equals(Object o) {
            return super.equals(o);
         }
      });
   }

   private TestedMethodDef[] getMethodsThatHaveTheirNameAtBeginningOfString(TestedMethodDef[] methods, String testedMethodName) {
      List filteredMethods = new MethodNameAtStartOfStringFilter(testedMethodName).filterMethods(methods);
      return (TestedMethodDef[]) filteredMethods.toArray(new TestedMethodDef[filteredMethods.size()]);
   }

   private String removeJUnitTestMethodPrefix(String testMethodName) {
      String testedMethodName = testMethodName.substring(JUnitHelper.TEST_METHOD_PREFIX.length());
      return decapitalize(testedMethodName);
   }

   private String decapitalize(String s) {
      return s.substring(0, 1).toLowerCase() + s.substring(1);
   }

   private void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

}
