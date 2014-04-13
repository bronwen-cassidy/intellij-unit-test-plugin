/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/ClassMethodListActionHandler.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
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
import org.intellij.plugins.util.PluginPsiUtil;

public class ClassMethodListActionHandler extends BaseActionHandler {

    UntestedMethodListProcessor processor;

    public ClassMethodListActionHandler(PluginPsiUtil pluginPsiUtil, UntestedMethodListProcessor processor) {
        super(pluginPsiUtil);
        this.processor = processor;
    }

    protected void actionPerformed(PsiClass currentClass, PsiMethod originMethod, PsiElement originatingElement) {
        if (isEnabled()) processor.execute(currentClass, originMethod, originatingElement);
    }
}