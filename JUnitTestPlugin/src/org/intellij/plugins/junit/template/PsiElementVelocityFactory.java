/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/psi/PsiElementVelocityFactory.java,v 1.2 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import org.apache.velocity.VelocityContext;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.util.TemplateEngine;

public abstract class PsiElementVelocityFactory {

    protected static final Logger LOG = LogUtil.getLogger();
    protected PsiManager manager;
    protected CodeStyleManager codeStyleManager;
    protected TemplateEngine templateEngine;

    public PsiElementVelocityFactory(PsiManager manager,
                                     CodeStyleManager codeStyleManager,
                                     TemplateEngine templateEngine) {
        this.manager = manager;
        this.codeStyleManager = codeStyleManager;
        this.templateEngine = templateEngine;
    }

    public PsiElement createElement(PsiClass parent, PsiElement afterChild, String templateName, VelocityContext context) throws IncorrectOperationException {
        
        final PsiElementFactory factory = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory();
        PsiElement element;
        String text;
        try {
            text = templateEngine.processTemplate(templateName, context);
        } catch (Exception e) {
            return handleException("Template '" + templateName + "' failed. Please check your template", e);
        }
        try {
            element = createElement(factory, text);
        } catch (IncorrectOperationException e) {
            return handleException(
                    "Template '" + templateName + "' produced wrong code. Please check your template:\n" + text, e);
        }

        element = codeStyleManager.reformat(element);

        if (afterChild == null) {
            element = parent.add(element);
        } else {
            element = parent.addAfter(element, afterChild);
        }
        codeStyleManager.reformat(parent);
        return element;
    }

    protected abstract PsiElement createElement(PsiElementFactory factory, String text)
            throws IncorrectOperationException;

    private PsiMethod handleException(String message, Exception e) throws IncorrectOperationException {
        LOG.info(message);
        LOG.debug(e);
        throw new IncorrectOperationException(message + (e == null ? "" : ("\n====================\n\n" + e.getMessage())));
    }
}