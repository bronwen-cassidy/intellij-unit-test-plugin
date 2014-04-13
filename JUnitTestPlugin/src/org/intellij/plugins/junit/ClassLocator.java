/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassLocator.java,v 1.24 2005/08/21 16:59:11 shadow12 Exp $
 * $Revision: 1.24 $
 * $Date: 2005/08/21 16:59:11 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.apache.velocity.VelocityContext;
import org.intellij.plugins.junit.template.ClassTemplateContextBuilder;
import org.intellij.plugins.util.*;

public class ClassLocator {

   protected static final Logger LOG = LogUtil.getLogger();

   private PsiClassManager psiClassManager;
   private TemplateEngine templateEngine;
   private ClassTemplateContextBuilder contextBuilder;
   private PsiDirectoryUtil psiDirectoryUtil;
   private ClassDefFactory classDefFactory;

   public ClassLocator(PsiClassManager psiClassManager,
                       PsiDirectoryUtil psiDirectoryUtil,
                       TemplateEngine templateEngine,
                       ClassTemplateContextBuilder contextBuilder,
                       ClassDefFactory classDefFactory) {
      this.psiClassManager = psiClassManager;
      this.psiDirectoryUtil = psiDirectoryUtil;
      this.templateEngine = templateEngine;
      this.contextBuilder = contextBuilder;
      this.classDefFactory = classDefFactory;
   }

   public boolean isTestClassConform(PsiClass testClass) {
      return getTestedClassPath(testClass) != null;
   }

   // todo will be removed when the refactoring is in place
   public PsiClass findTestClass(PsiClass testedClass) {
      String testClassPath = getTestClassPath(testedClass);
      if (testClassPath == null) return null;
      return psiClassManager.findPrimaryClass(testClassPath);
   }


   public TestClassDef createTestClass(TestedClassDef testedClassDef, TestClassDef testClassDef, String templateName) throws Exception {
      if (testedClassDef == null || testedClassDef.getPsiClass() == null) return null;
      VelocityContext context = contextBuilder.getContext(testClassDef);
      PsiClass psiClass = createClassFromTemplate(testClassDef, templateName, context);
      testClassDef.setPsiClass(psiClass);
      testedClassDef.addTestClass(testClassDef);
      //FIXME Must populate testClassDef. Maybe the classDefFactory should cache ClassDefs and be recreated for every actions
      return testClassDef;
   }

   private PsiClass createClassFromTemplate(ClassDef classDef, String templateName, VelocityContext context)
         throws Exception {
      PsiDirectory parentDir = psiDirectoryUtil.findOrCreateDirectory(classDef.getRoot(), classDef.getPackageName());
      LOG.debug("creating class in " + parentDir.getVirtualFile().getPath());
      String classResult = templateEngine.processTemplate(templateName, context);
      return psiClassManager.createClass(parentDir, classResult, true);
   }

   public String getTestClassPath(PsiClass testedClass) {
      return classDefFactory.getTestClassPath(testedClass);
   }

   public String[] getTestClassPaths(PsiClass testedClass) {
      return classDefFactory.getTestClassPaths(testedClass);
   }

   public boolean isClassTestable(PsiClass psiClass) {
      if (psiClass.isInterface()) {
         LOG.debug(psiClass.getName() + " is an interface");
         return false;
      }
      boolean isATestClass = isTestClassConform(psiClass);
      if (isATestClass) {
         LOG.debug(psiClass.getName() + " is a test class");
         return false;
      }
      return true;
   }

   public boolean isTestedClassConform(PsiClass testedClass) {
      return getTestClassPath(testedClass) != null;
   }

   private PsiClass findTestedClass(PsiClass testClass) {
      String testedClassPath = getTestedClassPath(testClass);
      LOG.debug("searching for tested class " + testedClassPath);
      return psiClassManager.findPrimaryClass(testedClassPath);
   }

   public String getTestedClassPath(PsiClass testClass) {
      return classDefFactory.getTestedClassPath(testClass);
   }

   /**
    * Gets the testClassDef for the given testedClass
    *
    * @param testedClass
    * @return TestedClassDef
    */
   public TestedClassDef getTestedClassDef(PsiClass testedClass) {
      return classDefFactory.getTestedClassDef(testedClass);
   }

   /**
    * Finds a testedClassDef given the test class
    *
    * @param testClass
    * @return TestedClassDef
    */
   public TestedClassDef findTestedClassDef(PsiClass testClass) {
      return classDefFactory.getTestedClassDef(findTestedClass(testClass));
   }

   public boolean hasOnlyOnePattern() {
      return classDefFactory.hasSinglePattern();
   }

   public String matchTestClass(PsiClass testedClass, TestClassDef testClass) {
      return classDefFactory.matchTestClassPath(testedClass, testClass);
   }
}
