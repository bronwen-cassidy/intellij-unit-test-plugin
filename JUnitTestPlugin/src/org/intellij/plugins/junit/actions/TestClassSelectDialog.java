/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestClassSelectDialog.java,v 1.1 2005/08/19 08:44:44 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/08/19 08:44:44 $
 *
 *
}Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.junit.actions.TestClassSelectPanel.TestClassActionHandler;
import org.intellij.plugins.ui.SelectDialog;
import org.intellij.plugins.util.PluginPsiUtil;


public class TestClassSelectDialog extends SelectDialog {

    public TestClassSelectDialog(Project project, PluginPsiUtil pluginPsiUtil) {
        super(project, new TestClassSelectPanel(pluginPsiUtil));
    }

    public void show(TestedClassDef testedClassDef, TestClassActionHandler testClassActionHandler) {
        ((TestClassSelectPanel) panel).show(testedClassDef, testClassActionHandler);
        finishShow();
    }
}

