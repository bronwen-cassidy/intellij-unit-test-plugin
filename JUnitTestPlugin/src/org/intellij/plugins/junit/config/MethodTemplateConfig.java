/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/MethodTemplateConfig.java,v 1.5 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.project.Project;

import java.util.Arrays;
import java.util.List;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * $Id: MethodTemplateConfig.java,v 1.5 2005/07/30 18:27:54 shadow12 Exp $
 */
public class MethodTemplateConfig extends TemplateConfig {

   public static final List/*<String>*/ DEFAULT_TEMPLATE_NAMES = Arrays.asList(
      "Default Test Method.java",
      "Test Method throws Exception.java");

   public MethodTemplateConfig(Project project) {
      super(project, "Test method");
   }

   public static MethodTemplateConfig getInstance(Project project) {
      return project.getComponent(MethodTemplateConfig.class);
   }

   protected List/*<String>*/ getDefaultTemplateNames() {
      return DEFAULT_TEMPLATE_NAMES;
   }

   protected void initOnProjectOpened() { }

   public String getComponentName() {
      return "JUnitMethodTemplateConfiguration";
   }
}
