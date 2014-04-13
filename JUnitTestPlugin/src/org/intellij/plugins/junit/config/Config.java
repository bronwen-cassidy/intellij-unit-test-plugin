/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/config/Config.java,v 1.9 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.9 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.intellij.plugins.util.LogUtil;
import org.jdom.Element;

/**
 * User: Jacques
 * Date: Aug 5, 2003
 * Time: 10:59:50 AM
 */
public abstract class Config implements JDOMExternalizable, ProjectComponent {

    private static final Logger LOG = LogUtil.getLogger();
    protected Project project;
    private StartupManager startupManager;

    public Config(Project project) {
        this.project = project;
    }

    public void projectClosed() {
    }

    public void projectOpened() {
        debug(LogUtil.getCallerMethod() + " for " + (project == null ? "null" : "" + project.getBaseDir())); //project.getProjectFile();
        initOnProjectOpened();
        getStartupManager().registerPostStartupActivity(new Runnable() {
            public void run() {
                initConfig();
            }
        });
    }

    private StartupManager getStartupManager() {
        if (startupManager == null)
            startupManager = StartupManager.getInstance(project);
        return startupManager;
    }

    public void setStartupManager(StartupManager startupManager) {
        this.startupManager = startupManager;
    }

    private void initConfig() {
        try {
            configChanged();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void configChanged() throws ConfigurationException {
        if (isDefaultProject()) return;
        updateInternalState();
    }

    protected abstract void initOnProjectOpened();

    protected abstract void updateInternalState() throws ConfigurationException;

    public void disposeComponent() {
    }

    public void initComponent() {
        debug(LogUtil.getCallerMethod() + " for " + (project == null ? "null" : "" + project.getBaseDir())); //project.getProjectFile();
    }

    public void readExternal(Element element) throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }

    //XmlSerializer
    public void writeExternal(Element element) throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }


    protected void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public boolean isDefaultProject() {
        return project == null || project.getBaseDir() == null;  //project.getProjectFile();
    }
}
