/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/config/ConfigEditor.java,v 1.10 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.10 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.config;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.ui.common.HtmlLabel;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.junit.config.Config;

import javax.swing.*;

/**
 * User: Jacques
 * Date: Aug 5, 2003
 * Time: 9:47:15 AM
 */
public abstract class ConfigEditor implements ProjectComponent, Configurable {

    private static final Logger LOG = LogUtil.getLogger();
    protected Project project;
    private Class configurationClass;

    protected JComponent component;
    private Config configuration;

    public ConfigEditor(Project project, Class configurationClass) {
        this.project = project;
        this.configurationClass = configurationClass;
    }

    public void disposeUIResources() {
        component = null;
    }

    public JComponent createComponent() {
        logMethod();
        if (component == null) {
            component = createEditorPanel();
        }
        return component;
    }

    public abstract JComponent createEditorPanel();

    public JLabel createHtmlLabel(String title, String htmlHelp) {
        return new HtmlLabel(title, htmlHelp);
    }

    public String getHelpTopic() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public void projectOpened() {
        logMethod();
    }

    private void logMethod() {
        debug(LogUtil.getCallerMethod() + " for project " + getProjectName());
    }

    public void projectClosed() {
        logMethod();
    }

    public void initComponent() {
        logMethod();
    }

    public void reset() {
        copyFromConfig();
    }

    public void apply() throws ConfigurationException {
        copyToConfig();
        getConfiguration().configChanged();
    }

    protected abstract void copyFromConfig();

    protected abstract void copyToConfig();

    protected String getProjectName() {
        String projectName;
        if (isDefaultProject()) {
            projectName = "<default>";
        } else {
            projectName = project.getBaseDir().getName();
        }
        return projectName;
    }

    private boolean isDefaultProject() {
        return project == null || project.getBaseDir() == null; //project.getProjectFile() == null;
    }

    public void disposeComponent() {
    }

    public void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public Config getConfiguration() {
        if (configuration == null) {
            configuration = (Config) project.getComponent(configurationClass);
        }
        return configuration;
    }

}
