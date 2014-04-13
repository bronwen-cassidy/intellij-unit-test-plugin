/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestClassProcessor.java,v 1.1 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import org.intellij.plugins.junit.ClassLocator;
import org.intellij.plugins.junit.config.ClassTemplateConfig;
import org.intellij.plugins.junit.template.TestClassCreator;

public abstract class TestClassProcessor extends JUnitTestCaseProcessor {

   protected ClassTemplateConfig classTemplateConfig;
   protected ClassLocator classLocator;
   protected TestClassCreator testClassCreator;
   protected EditorHelper editorHelper;

   public TestClassProcessor(ClassTemplateConfig classTemplateConfig,
                             ClassLocator classLocator,
                             TestClassCreator testClassCreator) {
      this.classTemplateConfig = classTemplateConfig;
      this.classLocator = classLocator;
      this.testClassCreator = testClassCreator;
   }

   /*public void selectClassTemplateAndCreate(final String testedClassName) {
      testClassCreator.createClass(testedClassName, new ElementCreationListener() {
         public void elementCreated(PsiElement newTestClass) {

         }
      });
   }*/
}
