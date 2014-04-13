/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassRefactoringListener.java,v 1.10 2005/08/29 09:26:36 shadow12 Exp $
 * $Revision: 1.10 $
 * $Date: 2005/08/29 09:26:36 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.util.IncorrectOperationException;
import org.intellij.plugins.util.CommandUtil;
import org.intellij.plugins.util.PathUtil;
import org.intellij.plugins.util.PsiDirectoryUtil;
import org.intellij.plugins.junit.adaptor.RefactoringAdaptor;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 1, 2003
 * Time: 3:30:15 PM
 * To change this template use Options | File Templates.
 */
public class ClassRefactoringListener implements RefactoringElementListener {
   private TestedClassDef testedClassDef;
   private CommandUtil commandUtil;
   private ClassLocator locator;
   private RefactoringAdaptor refactorer;
   private boolean moveTestClass;
   private boolean renameTestClass;
   private PsiDocumentManager documentManager;
   private PsiDirectoryUtil psiDirectoryUtil;

   public ClassRefactoringListener(TestedClassDef testClass,
                                   boolean moveTestClass,
                                   boolean renameTestClass,
                                   ClassLocator locator,
                                   RefactoringAdaptor refactorer,
                                   CommandUtil util,
                                   PsiDocumentManager documentManager,
                                   PsiDirectoryUtil psiDirectoryUtil) {
      debug("new ClassRefactoringListener(" + testClass + ")");
      this.testedClassDef = testClass;
      this.moveTestClass = moveTestClass;
      this.renameTestClass = renameTestClass;
      this.commandUtil = util;
      this.locator = locator;
      this.refactorer = refactorer;
      this.documentManager = documentManager;
      this.psiDirectoryUtil = psiDirectoryUtil;
   }

   public ClassRefactoringListener(ClassLocator locator,
                                   RefactoringAdaptor refactorer,
                                   CommandUtil commandUtil,
                                   PsiDocumentManager documentManager,
                                   PsiDirectoryUtil psiDirectoryUtil) {
      this.locator = locator;
      this.refactorer = refactorer;
      this.commandUtil = commandUtil;
      this.documentManager = documentManager;
      this.psiDirectoryUtil = psiDirectoryUtil;
   }

   public void reset(TestedClassDef testClassDefs, boolean moveTestClass, boolean renameTestClass) {
      this.testedClassDef = testClassDefs;
      this.moveTestClass = moveTestClass;
      this.renameTestClass = renameTestClass;
   }

   public void elementMoved(final PsiElement newElement) {
      if (testedClassDef != null && moveTestClass) {
         try {
            final CommandUtil.Command cmd = new CommandUtil.Command() {
               public Object run() throws Exception {
                  handlesClassMoved(testedClassDef.getExistingTests(), (PsiClass) newElement);
                  return null;
               }
            };
            commandUtil.runWriteAction(cmd);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }

   private void handlesClassMoved(TestClassDef[] testClasses, PsiClass movedTestedClass) {
      for (int i = 0; i < testClasses.length; i++) {
         TestClassDef testClassDef = testClasses[i];
         debug("handleClassMoved(" + testClassDef + "," + movedTestedClass);
         String matchedClasspath = locator.matchTestClass(movedTestedClass, testClassDef);
         if (matchedClasspath != null) {
            String parentPath = new PathUtil('/').getParentPathFromPath(matchedClasspath);
            try {
               PsiDirectory dir = psiDirectoryUtil.findOrCreateDirectory(parentPath);
               testClassDef.setRoot(dir);
            } catch (IncorrectOperationException e) {
               LOG.error("Problem creating " + parentPath, e);
               continue;
            }
         }
      }
      refactorer.moveClasses(testClasses);
   }

   public void elementRenamed(final PsiElement newElement) {
      if (testedClassDef != null && renameTestClass) {
         try {
            final CommandUtil.Command cmd = new CommandUtil.Command() {
               public Object run() throws Exception {
                  handleClassesRenamed(testedClassDef.getExistingTests(), (PsiClass) newElement);
                  return null;
               }
            };
            commandUtil.runWriteAction(cmd);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

   }

   public void handleClassesRenamed(TestClassDef[] testClasses, PsiClass renamedTestedClass) {

      String[] newClassNames = new String[testClasses.length];
      PsiNamedElement[] renameElements = new PsiNamedElement[testClasses.length];

      for (int i = 0; i < testClasses.length; i++) {
         TestClassDef testClassDef = testClasses[i];
         debug("handleClassMoved(" + testClassDef + "," + renamedTestedClass);
         String fileName = new PathUtil('/').getNameFromPath(locator.matchTestClass(renamedTestedClass, testClassDef));
         newClassNames[i] = fileName.substring(0, fileName.length() - ".java".length());
         renameElements[i] = testClassDef.getPsiClass();
      }
      safeRenameClasses(renameElements, newClassNames);
   }

   private void safeRenameClasses(PsiNamedElement[] renameElements, String[] newClassNames) {
      try {
         documentManager.commitAllDocuments();
         refactorer.rename(renameElements, newClassNames);
      } finally {
      }
   }

   public TestedClassDef getTestedClassDef() {
      return testedClassDef;
   }

   private void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

   private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.ClassRefactoringListener");
}
