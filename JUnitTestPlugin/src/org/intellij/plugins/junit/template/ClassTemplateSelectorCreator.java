/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/ClassTemplateSelectorCreator.java,v 1.1 2005/07/31 08:45:15 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/07/31 08:45:15 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.junit.config.ClassTemplateConfig;
import org.intellij.plugins.ui.MessageHandler;
import org.intellij.plugins.util.CommandUtil;

public class ClassTemplateSelectorCreator extends TemplateSelectorCreator {
   public ClassTemplateSelectorCreator(Project project,
                                       ClassTemplateConfig templateConfig,
                                       CommandUtil commandUtil,
                                       MessageHandler messageHandler) {
      super(project, templateConfig, commandUtil, messageHandler);
   }
}