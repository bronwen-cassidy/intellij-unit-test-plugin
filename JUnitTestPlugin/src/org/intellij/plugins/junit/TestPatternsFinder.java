/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/TestPatternsFinder.java,v 1.6 2005/10/19 08:16:56 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/10/19 08:16:56 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import org.intellij.plugins.junit.config.ClassPatternConfig;
import org.intellij.plugins.util.PsiDirectoryUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 11-Aug-2005 06:33:33
 */
public class TestPatternsFinder implements PropertyChangeListener {

   private List testPatterns;
   private PsiDirectoryUtil psiDirectoryUtil;

   public TestPatternsFinder(ClassPatternConfig classPatternConfig, PsiDirectoryUtil psiDirectoryUtil) {
      this.testPatterns = classPatternConfig.getTestPatterns();
      this.psiDirectoryUtil = psiDirectoryUtil;
      classPatternConfig.addListener(this);
   }

   public String[] getTestClassPaths(String testedClassPath) {

      if(testedClassPath == null) return new String[0];

      List testClassPaths = new ArrayList();
      for (int i = 0; i < testPatterns.size(); i++) {
         ClassPattern classPattern = (ClassPattern) testPatterns.get(i);
         String testClassPath = classPattern.getTestClassPath(testedClassPath);
         if(testClassPath != null) {
            // do not add the path unless it is within project source roots
            if(psiDirectoryUtil.isWithinSourceRoots(testClassPath)) testClassPaths.add(testClassPath);
         }
      }
      return (String[]) testClassPaths.toArray(new String[testClassPaths.size()]);
   }

   public String[] getTestedClassPaths(String testClassPath) {
      if(testClassPath == null) return null;

      List testedClassPaths = new ArrayList();
      for (int i = 0; i < testPatterns.size(); i++) {
         ClassPattern classPattern = (ClassPattern) testPatterns.get(i);
         String testedClassPath = classPattern.getTestedClassPath(testClassPath);
         if(testedClassPath != null) {
            testedClassPaths.add(testedClassPath);
         }
      }
      return (testedClassPaths.isEmpty() ? null : (String[]) testedClassPaths.toArray(new String[testedClassPaths.size()]));
   }

   public int size() {
      return testPatterns.size();
   }

   private void reset(List testPatterns) {
      this.testPatterns.clear();
      this.testPatterns.addAll(testPatterns);
   }

   public boolean match(String filePath) {
      for (int i = 0; i < testPatterns.size(); i++) {
         ClassPattern classPattern = (ClassPattern) testPatterns.get(i);
         if(classPattern.matchTestClassPath(filePath)) return true;
      }
      return false;
   }

   public ClassPattern[] getTestPatterns() {
      return (ClassPattern[]) testPatterns.toArray(new ClassPattern[testPatterns.size()]);
   }

   public String findTestClassPath(String testedClassPath, String testClassPath) {
      ClassPattern[] testPatterns = getTestPatterns();
      for (int i = 0; i < testPatterns.length; i++) {
         ClassPattern testPattern = testPatterns[i];
         String newTestClassPath = testPattern.getTestClassPath(testedClassPath);
         if(testPattern.matchTestClassPath(testClassPath)) return newTestClassPath;
      }
      return null;
   }

   public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
      ClassPatternConfig newValue = (ClassPatternConfig) propertyChangeEvent.getNewValue();
      reset(newValue.getTestPatterns());
   }
}
