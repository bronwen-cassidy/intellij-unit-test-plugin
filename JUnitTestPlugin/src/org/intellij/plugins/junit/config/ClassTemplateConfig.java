/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassTemplateConfig.java,v 1.5 2005/07/30 18:27:54 shadow12 Exp $
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
 * $Id: ClassTemplateConfig.java,v 1.5 2005/07/30 18:27:54 shadow12 Exp $
 */
public class ClassTemplateConfig extends TemplateConfig {

   public static final List/*<String>*/ DEFAULT_TEMPLATE_NAMES = Arrays.asList(
         new String[]{"Default Test Class.java"});

   public static ClassTemplateConfig getInstance(Project project) {
      return project.getComponent(ClassTemplateConfig.class);
   }

   public ClassTemplateConfig(Project project) {
      super(project, "Test class");
   }

   protected void initOnProjectOpened() { }

   public String getComponentName() {
      return "JUnitClassTemplateConfiguration";
   }

   protected List/*<String>*/ getDefaultTemplateNames() {
      return DEFAULT_TEMPLATE_NAMES;
   }
}
