/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/ToggleTestCaseTestedClassAction.java,v 1.18 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.18 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.intellij.plugins.util.DataContextUtil;
import org.intellij.plugins.actions.HandledAction;
import org.intellij.plugins.junit.JUnitHelper;

public class ToggleTestCaseTestedClassAction extends HandledAction {

   protected Class getHandlerClass() {
      return ToggleTestCaseTestedClassActionHandler.class;
   }

   public void actionPerformed(AnActionEvent event) {
      if (!JUnitHelper.checkProjectCorrectlySetup(DataContextUtil.getProject(event))) return;
      super.actionPerformed(event);
   }


}
