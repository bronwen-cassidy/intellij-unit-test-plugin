/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/config/MainConfigEditor.java,v 1.4 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.util.PathUtil;
import org.intellij.plugins.config.ConfigEditor;
import org.intellij.plugins.junit.PluginVersion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * User: Jacques
 * Date: Aug 26, 2003
 * Time: 12:21:50 AM
 */
public abstract class MainConfigEditor extends ConfigEditor {

    private Class versionClass;

    public MainConfigEditor(Project project, Class configurationClass, Class versionClass) {
        super(project, configurationClass);
        this.versionClass = versionClass;
    }

    public JComponent createEditorPanel() {

        component = new JPanel(new BorderLayout());
        // component add North an disbale button
        component.add(createDisablePanel(), BorderLayout.NORTH);
        component.add(createMainEditorPanel(), BorderLayout.CENTER);

        JPanel versionPanel = createVersionPanel();
        component.add(versionPanel, BorderLayout.SOUTH);

        return component;
    }

    protected abstract JComponent createMainEditorPanel();

    protected abstract JComponent createDisablePanel();

    private JPanel createVersionPanel() {
        JPanel versionPanel = new JPanel(new BorderLayout());
        versionPanel.add(new JLabel("Version " + getPluginVersion()), BorderLayout.EAST);
        versionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        return versionPanel;
    }

    private String getPluginVersion() {
        Package thisPackage = Package.getPackage(new PathUtil('.').getParentPathFromPath(getClass().getName()));
        if (thisPackage == null ||
                thisPackage.getSpecificationVersion() == null ||
                thisPackage.getSpecificationVersion().equals(""))
            return getVersion();
        else
            return thisPackage.getSpecificationVersion();

    }

    private String getVersion() {
        try {
            return ((PluginVersion) versionClass.newInstance()).getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown";
    }
}
