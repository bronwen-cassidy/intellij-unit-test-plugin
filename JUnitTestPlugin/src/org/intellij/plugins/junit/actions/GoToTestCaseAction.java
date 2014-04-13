/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/GoToTestCaseAction.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.actions.ActionHandler;
import org.intellij.plugins.actions.HandledAction;

public class GoToTestCaseAction extends HandledAction {

    private PsiMethod untestedMethod;

    public GoToTestCaseAction(PsiMethod untestedMethod) {
        super();
        this.untestedMethod = untestedMethod;
    }

    protected Class getHandlerClass() {
        return GoToTestCaseActionHandler.class;
    }

    protected ActionHandler getHandler(AnActionEvent event) {
        GoToTestCaseActionHandler handler = (GoToTestCaseActionHandler) super.getHandler(event);
        handler.setUntestedMethod(untestedMethod);
        return handler;
    }
}
