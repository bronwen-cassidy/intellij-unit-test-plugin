/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/ToggleTestCaseTestedClassActionHandler.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.junit.JUnitHelper;
import org.intellij.plugins.util.PluginPsiUtil;

public class ToggleTestCaseTestedClassActionHandler extends BaseActionHandler {

   private GoToTestedClassProcessor goToTestedClassProcessor;
   private GoToUnitTestProcessor goToUnitTestProcessor;

   public ToggleTestCaseTestedClassActionHandler(PluginPsiUtil pluginPsiUtil,
                                                 GoToTestedClassProcessor goToTestedClassProcessor,
                                                 GoToUnitTestProcessor goToUnitTestProcessor) {
      super(pluginPsiUtil);
      this.goToTestedClassProcessor = goToTestedClassProcessor;
      this.goToUnitTestProcessor = goToUnitTestProcessor;
   }

   protected void actionPerformed(PsiClass currentClass, PsiMethod originMethod, PsiElement originatingElement) {
      if(isEnabled()) getProcessor(currentClass).execute(currentClass, originMethod, originatingElement);
   }

   private JUnitTestCaseProcessor getProcessor(PsiClass currentClass) {
      JUnitTestCaseProcessor processor;
      if (JUnitHelper.isUnitTest(currentClass)) {
         processor = goToTestedClassProcessor;
      } else {
         processor = goToUnitTestProcessor;
      }
      return processor;
   }

   public void update() {
      super.update();
      if (getPresentation().isEnabled()) {
         event.getPresentation().setText(getActionText());
      }
   }

   private String getActionText() {
      String actionText = "";
      if (event.getPlace().equals(ActionPlaces.PROJECT_VIEW_POPUP) ||
          event.getPlace().equals(ActionPlaces.STRUCTURE_VIEW_POPUP)) {
         actionText = "Go to ";
      }
      if (JUnitHelper.isUnitTest(getCurrentClass())) {
         actionText += "Tested Class";
      } else {
         actionText += "Test Class";
      }
      return actionText;
   }


}

