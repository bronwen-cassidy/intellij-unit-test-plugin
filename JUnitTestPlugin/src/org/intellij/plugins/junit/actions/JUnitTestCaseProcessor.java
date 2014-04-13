/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/JUnitTestCaseProcessor.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.util.LogUtil;

public abstract class JUnitTestCaseProcessor {

    protected static final Logger LOG = LogUtil.getLogger();

    protected PsiElement originatingElement;
    protected PsiClass originClass;
    protected PsiMethod originMethod;

    public void execute(PsiClass originClass, PsiMethod originMethod, PsiElement originatingElement) {

        this.originatingElement = originatingElement;
        this.originClass = originClass;
        this.originMethod = originMethod;
        debug("originClass = " + originClass);
        debug("method = " + originMethod);
        execute();
    }

    protected abstract void execute();

    protected void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }
}
