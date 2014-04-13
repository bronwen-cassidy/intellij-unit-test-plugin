/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodRefactoringListener.java,v 1.9 2005/08/21 16:59:11 shadow12 Exp $
 * $Revision: 1.9 $
 * $Date: 2005/08/21 16:59:11 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.intellij.plugins.util.CommandUtil;
import org.intellij.plugins.junit.adaptor.RefactoringAdaptor;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 1, 2003
 * Time: 3:14:08 PM
 * To change this template use Options | File Templates.
 */
public class MethodRefactoringListener implements RefactoringElementListener {
   private TestMethodDef[] testMethods;
   private TestedMethodDef testedMethod;
   private RefactoringAdaptor refactoringAdaptor;
   private CommandUtil commandUtil;
   private MethodLocator locator;
   private boolean renameTestMethods;
   private String testMethodOldPrefix;

   public MethodRefactoringListener(TestMethodDef[] testMethods,
                                    TestedMethodDef testedMethodDef,
                                    String testMethodOldPrefix,
                                    boolean renameTestMethods,
                                    MethodLocator locator,
                                    RefactoringAdaptor refactoringAdaptor,
                                    CommandUtil commandUtil) {
      this(locator, refactoringAdaptor, commandUtil);
      reset(testMethods, testedMethodDef, testMethodOldPrefix, renameTestMethods);
   }

   public MethodRefactoringListener(MethodLocator locator, RefactoringAdaptor refactoringAdaptor, CommandUtil commandUtil) {
      this.locator = locator;
      this.refactoringAdaptor = refactoringAdaptor;
      this.commandUtil = commandUtil;
   }

   public void reset(TestMethodDef[] testMethods, TestedMethodDef testedMethod, String testMethodOldPrefix, boolean renameTestMethods) {
      this.testedMethod = testedMethod;
      this.testMethods = testMethods;
      this.testMethodOldPrefix = testMethodOldPrefix;
      this.renameTestMethods = renameTestMethods;
   }

   public void elementMoved(final PsiElement newElement) {
      // Not possible until we have a true instance move method. PLEASE PLEASE JetBrains!!!
   }


   public void elementRenamed(final PsiElement newElement) {
      if (renameTestMethods && testedMethod != null && testMethods != null) {
         try {
            final CommandUtil.Command cmd = new CommandUtil.Command() {
               public Object run() throws Exception {
                  handleMethodsRenamed(testMethods, (PsiMethod) newElement);
                  return null;
               }
            };
            commandUtil.runWriteAction(cmd);
         } catch (Exception e) {
            e.printStackTrace(); 
         }
      }
   }

   private void handleMethodsRenamed(TestMethodDef[] testMethods,
                                     PsiMethod renamedTestedMethod) {

      String[] newNames = new String[testMethods.length];
      PsiNamedElement[] elements = new PsiNamedElement[testMethods.length];
      for (int i = 0; i < testMethods.length; i++) {
         TestMethodDef testMethod = testMethods[i];
         debug("Renaming test method " + testMethod.getName());
         newNames[i] = createNewName(testMethod, locator.getTestMethodName(renamedTestedMethod), testMethodOldPrefix);
         elements[i] = testMethod.getPsiMethod();
      }
      refactoringAdaptor.rename(elements, newNames);
   }

   private String createNewName(TestMethodDef testMethod,
                                String testMethodNewPrefix,
                                String testMethodOldPrefix) {
      return testMethodNewPrefix + testMethod.getName().substring(testMethodOldPrefix.length());
   }


   public String getTestMethodOldName() { return testMethodOldPrefix; }

   public TestMethodDef[] getTestMethods() { return testMethods; }

   private void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

   private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.MethodRefactoringListener");
}
