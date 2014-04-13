/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/BaseActionHandler.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.actions.ActionHandler;
import org.intellij.plugins.util.PluginPsiUtil;
import org.intellij.plugins.junit.config.JUnitTestConfiguration;

public abstract class BaseActionHandler extends ActionHandler {

    protected PluginPsiUtil pluginPsiUtil;

    public BaseActionHandler(PluginPsiUtil pluginPsiUtil) {
        this.pluginPsiUtil = pluginPsiUtil;
    }

    public void actionPerformed() {
        PsiClass currentClass = getCurrentClass();
        if (currentClass == null) return;

        PsiElement originatingElement = pluginPsiUtil.getCurrentElement(event);
        PsiMethod originMethod = pluginPsiUtil.findTopLevelClassMethod(originatingElement);

        actionPerformed(currentClass, originMethod, originatingElement);
    }

    protected abstract void actionPerformed(PsiClass currentClass,
                                            PsiMethod originMethod,
                                            PsiElement originatingElement);

    protected PsiClass getCurrentClass() {
        PsiClass psiClass = null;
        final PsiElement psiElement = pluginPsiUtil.getCurrentElement(event);
        if (psiElement != null) {
            psiClass = pluginPsiUtil.findTopLevelEnclosingClass(psiElement);
        }
        return psiClass;
    }

    public void update() {
        PsiClass currentClass = getCurrentClass();
        debug("ToggleTestCaseTestedClassAction.getCurrentClass() currentClass = " + currentClass);
        getPresentation().setEnabled(currentClass != null);
    }

    public boolean isEnabled() {
        return JUnitTestConfiguration.getInstance(getProject()).IS_UNIT_TEST_ENABLED;
    }
}