/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/TemplateEngine.java,v 1.24 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.24 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.application.PathManager;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.log.SimpleLog4JLogSystem;

import java.io.File;
import java.io.StringWriter;

public class TemplateEngine {
   private boolean inited = false;
   private VelocityEngine velocity;

   public static final String IDEA_FILE_TEMPLATE_DIR = "fileTemplates";
   public static final String IDEA_TEMPLATE_INCLUDES_DIR = "includes";

   public TemplateEngine() {
      velocity = new VelocityEngine();
   }

   // TODO: spring load the engine. See spring org.springframework.ui.velocity.VelocityEngineFactory
   private void initVelocityProcess() {
      if (inited) return;
      try {
         ExtendedProperties extendedProperties = new ExtendedProperties();
         extendedProperties.addProperty(VelocityEngine.RESOURCE_LOADER, "file,class");
         extendedProperties.addProperty("file.resource.loader.class",
                                        org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
         extendedProperties.addProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
         extendedProperties.addProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, getFileTemplatePath());
         extendedProperties.addProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
         extendedProperties.addProperty("file.resource.loader.modificationCheckInterval", "2");
         extendedProperties.addProperty("class.resource.loader.class", TemplatesClasspathLoader.class.getName());
         extendedProperties.addProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
                                        SimpleLog4JLogSystem.class.getName());
         extendedProperties.addProperty("runtime.log.logsystem.log4j.category", TemplateEngine.class.getName());
         velocity.setExtendedProperties(extendedProperties);

         velocity.init();
         inited = true;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static File getFileTemplatesDir() {
      return new File(PathManager.getConfigPath(), IDEA_FILE_TEMPLATE_DIR);
   }

   private String getFileTemplatePath() {
      File templatesDir = getFileTemplatesDir();
      File includesDir = new File(templatesDir, IDEA_TEMPLATE_INCLUDES_DIR);
      return templatesDir.getAbsolutePath() + "," + includesDir.getAbsolutePath();
   }

   public String processTemplate(String templateName, VelocityContext context) throws Exception {
      initVelocityProcess();
      StringWriter writer = new StringWriter();
      velocity.mergeTemplate(templateName, context, writer);
      return StringUtils.fixLineBreaks(writer.toString());
   }

}
