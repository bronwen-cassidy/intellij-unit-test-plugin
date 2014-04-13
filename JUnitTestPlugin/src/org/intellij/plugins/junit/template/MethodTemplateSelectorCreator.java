/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/MethodTemplateSelectorCreator.java,v 1.1 2005/07/31 08:45:15 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/07/31 08:45:15 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import org.intellij.plugins.junit.config.MethodTemplateConfig;
import org.intellij.plugins.ui.MessageHandler;
import org.intellij.plugins.util.CommandUtil;
import com.intellij.openapi.project.Project;

public class MethodTemplateSelectorCreator extends TemplateSelectorCreator {

   public MethodTemplateSelectorCreator(Project project,
                                        MethodTemplateConfig templateConfig,
                                        CommandUtil commandUtil,
                                        MessageHandler messageHandler) {
      super(project, templateConfig, commandUtil, messageHandler);
   }
}