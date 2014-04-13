/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/psi/PsiClassVelocityFactory.java,v 1.2 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import org.intellij.plugins.util.TemplateEngine;
import org.intellij.plugins.junit.template.PsiElementVelocityFactory;

public class PsiClassVelocityFactory extends PsiElementVelocityFactory {

   public PsiClassVelocityFactory(PsiManager manager,
                                  CodeStyleManager codeStyleManager,
                                  TemplateEngine templateEngine) {
      super(manager, codeStyleManager, templateEngine);
   }

   protected PsiElement createElement(PsiElementFactory factory, String text) throws IncorrectOperationException {
      return factory.createClassFromText(text, null);
   }

}