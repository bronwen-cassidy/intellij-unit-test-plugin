/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/TestClassCreator.java,v 1.3 2005/08/20 19:20:43 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/20 19:20:43 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import org.intellij.plugins.junit.ClassLocator;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.ElementCreationListener;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.TemplateInstantiationCommand;

//DEBT Do we really need this class?
public class TestClassCreator {

   private ClassLocator classLocator;
   private final TemplateSelectorCreator templateSelectorCreator;


   public TestClassCreator(ClassLocator classLocator, ClassTemplateSelectorCreator psiElementTemplateInstantiator) {
      this.templateSelectorCreator = psiElementTemplateInstantiator;
      this.classLocator = classLocator;
   }

   public void createClass(TestedClassDef testedClassDef, TestClassDef testClassDef, ElementCreationListener creationListener) {
      TemplateInstantiationCommand creationCommand = new CreateClassMethodCommand(testedClassDef, testClassDef);
      templateSelectorCreator.selectTemplateAndCreate(creationCommand, creationListener);
   }

   private TestClassDef createClassFromTemplate(TestedClassDef testedClassDef, TestClassDef testClass, String templateName) throws Exception {
      return classLocator.createTestClass(testedClassDef, testClass, templateName);
   }

   class CreateClassMethodCommand extends TemplateInstantiationCommand {
      TestClassDef testClass;
      TestedClassDef testedClass;

      public CreateClassMethodCommand(TestedClassDef testedClassDef, TestClassDef testClass) {
         super("Create Test Method " + "Create Test Class " + testClass);
         this.testClass = testClass;
         this.testedClass = testedClassDef;
      }

      public Object run() throws Exception {
         return createClassFromTemplate(testedClass, testClass, templateName);
      }
   }
}
