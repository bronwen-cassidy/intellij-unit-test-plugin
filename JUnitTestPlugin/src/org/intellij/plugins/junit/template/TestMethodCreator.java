/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/TestMethodCreator.java,v 1.4 2005/08/20 09:54:26 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/08/20 09:54:26 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;
import org.intellij.plugins.junit.ClassLocator;
import org.intellij.plugins.junit.MethodLocator;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.junit.TestedMethodDef;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.ElementCreationListener;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.TemplateInstantiationCommand;
import org.intellij.plugins.util.PluginPsiUtil;

public class TestMethodCreator {

   private MethodLocator methodLocator;
   private ClassLocator classLocator;
   private final TemplateSelectorCreator templateSelectorCreator;
   private PluginPsiUtil pluginPsiUtil;
   private PsiMethodVelocityFactory psiMethodFactory;


   public TestMethodCreator(ClassLocator classLocator,
                            MethodLocator methodLocator,
                            MethodTemplateSelectorCreator psiElementTemplateInstantiator,
                            PsiMethodVelocityFactory psiMethodFactory,
                            PluginPsiUtil pluginPsiUtil) {
      this.classLocator = classLocator;
      this.templateSelectorCreator = psiElementTemplateInstantiator;
      this.pluginPsiUtil = pluginPsiUtil;
      this.methodLocator = methodLocator;
      this.psiMethodFactory = psiMethodFactory;
   }

   public void createMethod(PsiMethod testedMethod,
                            PsiClass testClass,
                            PsiMethod afterMethod,
                            ElementCreationListener creationListener) {
      if (!pluginPsiUtil.isFileWritable(testClass)) return;
      TestedClassDef testedClassDef = classLocator.getTestedClassDef(testedMethod.getContainingClass());
      TestClassDef testClassDef = testedClassDef.getTestClassDef(testClass);
      TemplateInstantiationCommand creationCommand = new CreateTestMethodCommand(testedMethod, testClassDef, afterMethod);
      templateSelectorCreator.selectTemplateAndCreate(creationCommand, creationListener);
   }

   //DEBT remove methodLocator dependency once we have a real domain model
   private String getTestMethodName(PsiMethod testedMethod) {
      // fixme should use a proper testedMethodDef!!
      return methodLocator.getTestMethodName(new TestedMethodDef(testedMethod));
   }

   private PsiMethod createTestMethodFromTemplate(TestClassDef testClass,
                                                  PsiMethod afterMethod,
                                                  PsiMethod testedMethod,
                                                  String templateName) throws IncorrectOperationException {
      MethodTemplateContextBuilder methodBuilder = new MethodTemplateContextBuilder(testClass, testedMethod);
      return (PsiMethod) psiMethodFactory.createElement(testClass.getPsiClass(),
                                                        afterMethod,
                                                        templateName,
                                                        methodBuilder.getContext());
   }

   class CreateTestMethodCommand extends TemplateInstantiationCommand {
      PsiMethod testedMethod;
      TestClassDef testClass;
      PsiMethod afterMethod;

      public CreateTestMethodCommand(PsiMethod testedMethod,
                                     TestClassDef testClass,
                                     PsiMethod afterMethod) {
         super("Create Test Method " + getTestMethodName(testedMethod) + "()");
         this.testedMethod = testedMethod;
         this.testClass = testClass;
         this.afterMethod = afterMethod;
      }

      public Object run() throws Exception {
         return createTestMethodFromTemplate(testClass, afterMethod, testedMethod, templateName);
      }
   }

}
