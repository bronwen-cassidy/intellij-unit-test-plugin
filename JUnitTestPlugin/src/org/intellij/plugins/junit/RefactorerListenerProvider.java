/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/RefactorerListenerProvider.java,v 1.7 2005/08/21 16:59:11 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/08/21 16:59:11 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import org.intellij.plugins.util.OpenApiFacade;
import org.intellij.plugins.beans.PluginBeanFactory;
import org.intellij.plugins.junit.adaptor.IdeaFacade;

public class RefactorerListenerProvider extends JUnitTestPluginActivity implements RefactoringElementListenerProvider {

   private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.RefactorerListenerProvider");

   boolean renameTestClass = false;
   boolean moveTestClass = false;
   boolean renameTestMethods = false;
   boolean renameCorrectingReferences = false;
   boolean moveCorrectReferences = false;


   public RefactorerListenerProvider(Project project, ClassLocator classLocator, MethodLocator methodLocator, IdeaFacade ideaFacade) {
      super(project, classLocator, methodLocator, ideaFacade);
      renameCorrectingReferences = true;
      moveCorrectReferences = true;
   }

   public void setOptions(boolean renameTestClass, boolean moveTestClass, boolean renameTestMethods) {
      this.renameTestClass = renameTestClass;
      this.moveTestClass = moveTestClass;
      this.renameTestMethods = renameTestMethods;
   }

   protected void register() {
      OpenApiFacade.getRefactoringListenerManager(project).addListenerProvider(this);
   }

   protected void unregister() {
      OpenApiFacade.getRefactoringListenerManager(project).removeListenerProvider(this);
   }

   public RefactoringElementListener getListener(PsiElement enclosingElement) {
      debug("getListener(" + enclosingElement + ")");
      if (PsiClass.class.isAssignableFrom(enclosingElement.getClass())) {
         TestedClassDef testedClassDef = classLocator.getTestedClassDef((PsiClass) enclosingElement);
         if (testedClassDef.hasExistingTestClasses()) {
            return newRefactoringListener(testedClassDef);
         }
      } else if (PsiMethod.class.isAssignableFrom(enclosingElement.getClass())) {
         if (renameTestMethods) {
            PsiMethod methodToBeRenamed = (PsiMethod) enclosingElement;
            TestedClassDef testedClassDef = classLocator.getTestedClassDef(methodToBeRenamed.getContainingClass());
            TestedMethodDef testedMethodDef = testedClassDef.findMethod(methodToBeRenamed);
            String testMethodOldPrefix = methodLocator.getTestMethodName(methodToBeRenamed);
            TestMethodDef[] testMethodDefs = testedMethodDef.getTestMethods();
            if (testMethodDefs != null && testMethodDefs.length > 0) {
               return newRefactoringListener(testMethodDefs, testedMethodDef, testMethodOldPrefix);
            }
         }
      }

      return null;
   }

   // TODO: Should look at the signature of the tested method in each test method to take care of overloaded methods
   private MethodRefactoringListener newRefactoringListener(TestMethodDef[] testMethods, TestedMethodDef methodToBeRenamed, String testMethodOldPrefix) {
      MethodRefactoringListener listener = (MethodRefactoringListener)
            PluginBeanFactory.getBean(project, MethodRefactoringListener.class);
      listener.reset(testMethods, methodToBeRenamed, testMethodOldPrefix, true);
      return listener;
   }

   private ClassRefactoringListener newRefactoringListener(TestedClassDef testClass) {
      ClassRefactoringListener listener = (ClassRefactoringListener) PluginBeanFactory.getBean(project, ClassRefactoringListener.class);
      listener.reset(testClass, moveTestClass, renameTestClass);
      return listener;
   }

   public boolean isRenameTestClass() {
      return renameTestClass;
   }

   public boolean isMoveTestClass() {
      return moveTestClass;
   }

   void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

   public boolean isRenameTestMethods() {
      return renameTestMethods;
   }
}
