/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodDefFactory.java,v 1.2 2005/08/19 08:44:44 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/08/19 08:44:44 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.util.LogUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MethodDefFactory {
   private static final Logger LOG = LogUtil.getLogger();

   public void populateTestedMethods(TestedClassDef testedClassDef) {
      PsiMethod[] methods = testedClassDef.getPsiClass().getMethods();
      for (int i = 0; i < methods.length; i++) {
         PsiMethod method = methods[i];
         TestedMethodDef testedMethodDef = new TestedMethodDef(method);
         testedClassDef.addTestedMethod(testedMethodDef);
      }
   }

   public void populateTestMethods(TestClassDef testClassDef) {
      PsiMethod[] methods = testClassDef.getPsiClass().getMethods();
      for (int i = 0; i < methods.length; i++) {
         PsiMethod method = methods[i];
         if (method.getName().startsWith(JUnitHelper.TEST_METHOD_PREFIX)) {
            TestMethodDef testMethodDef = new TestMethodDef(method);
            testClassDef.addTestMethod(testMethodDef);
         }
      }
   }

   public void associateTestMethodsWithTestedMethods(TestClassDef testClassDef) {
      for (int i = 0; i < testClassDef.getTestMethods().length; i++) {
         TestMethodDef testMethodDef = testClassDef.getTestMethods()[i];
         TestedMethodDef testedMethod = findTestedMethod(testMethodDef, testClassDef.getTestedClass());
         if (testedMethod != null)
            testedMethod.addTestMethod(testMethodDef);
      }
   }

   private TestedMethodDef findTestedMethod(TestMethodDef testMethod, TestedClassDef testedClassDef) {
      debug("searching for tested method of test method" + testMethod.getName());
      if (testMethod.getName().equals(JUnitHelper.TEST_METHOD_PREFIX)) return null;

      String testedMethodName = removeJUnitTestMethodPrefix(testMethod.getName());
      List methods = getMethodsThatHaveTheirNameAtBeginningOfString(testedClassDef.getTestedClassMethods(), testedMethodName);
      sortMethodsLongestNameFirst(methods);
      return (TestedMethodDef) getFirstMethod(methods);
   }

   private MethodDef getFirstMethod(List methods) {
      if (methods != null && methods.size() != 0) {
         return (MethodDef) methods.get(0);
      } else {
         return null;
      }
   }

   private void sortMethodsLongestNameFirst(List methods) {
      Collections.sort(methods, new Comparator/*<MethodDef>*/() {

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

   private List getMethodsThatHaveTheirNameAtBeginningOfString(TestedMethodDef[] methods, final String testedMethodName) {
      MethodDefFilter methodThatStartsWithFilter = new MethodDefFilter() {
         boolean accept(MethodDef method) {
            String other = testedMethodName.substring(0, 1).toUpperCase() + testedMethodName.substring(1);
            return testedMethodName.startsWith(method.getName()) || other.startsWith(method.getName());
         }
      };
      return methodThatStartsWithFilter.filterMethods(methods);
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

   class IsTestMethodFilter extends MethodDefFilter {
      private TestedMethodDef testedMethod;

      public IsTestMethodFilter(TestedMethodDef testedMethod) {
         this.testedMethod = testedMethod;
      }

      boolean accept(MethodDef method) {
         TestedClassDef testedClassDef = (TestedClassDef) testedMethod.getClassDef();
         TestedMethodDef foundTestedMethod = findTestedMethod((TestMethodDef) method, testedClassDef);
         // check for identicle names
         if (foundTestedMethod != null) {
            if (foundTestedMethod.getName().equals(testedMethod.getName())) return true;
         }
         return foundTestedMethod == testedMethod;
      }
   }
}