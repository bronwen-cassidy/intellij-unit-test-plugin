/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/GoToTestCaseActionHandler.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
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

public class GoToTestCaseActionHandler extends BaseActionHandler {

    GoToUnitTestProcessor processor;
    PsiMethod untestedMethod;

    public GoToTestCaseActionHandler(GoToUnitTestProcessor processor) {
        super(null);
        this.processor = processor;
    }

    public void setUntestedMethod(PsiMethod untestedMethod) {
        this.untestedMethod = untestedMethod;
    }

    public void update() {
    }


    protected void actionPerformed(PsiClass currentClass, PsiMethod originMethod, PsiElement originatingElement) {
        actionPerformed();
    }

    public void actionPerformed() {
        if(isEnabled())processor.execute(untestedMethod.getContainingClass(), untestedMethod, untestedMethod);
    }
}