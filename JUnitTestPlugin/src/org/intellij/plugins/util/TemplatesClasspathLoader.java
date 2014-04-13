/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/TemplatesClasspathLoader.java,v 1.4 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.InputStream;

public class TemplatesClasspathLoader extends ClasspathResourceLoader {

    protected static final Logger LOG = LogUtil.getLogger();

    public static final String TEMPLATE_SUBDIR = "fileTemplates/";
    public static final String TEMPLATE_INCLUDE_SUBDIR = TEMPLATE_SUBDIR + "includes/";

    public synchronized InputStream getResourceStream(String templateName)
            throws ResourceNotFoundException {
        InputStream inputStream = loadTemplate(TEMPLATE_SUBDIR + templateName + ".ft", templateName);
        if (inputStream == null) {
            inputStream = loadTemplate(TEMPLATE_INCLUDE_SUBDIR + templateName + ".ft", templateName);
        }
        return inputStream;
    }

    private InputStream loadTemplate(String name, String templateName) throws ResourceNotFoundException {
        InputStream inputStream = super.getResourceStream(name);
        log(templateName, name, inputStream);
        return inputStream;
    }

    private void log(String templateName, String name, InputStream inputStream) {
        if (inputStream != null) {
            LOG.debug("Loading template '" + templateName + "' from classpath with path " + name);
        } else {
            LOG.debug("No template '" + templateName + "' found in classpath at " + name);
        }
    }
}
