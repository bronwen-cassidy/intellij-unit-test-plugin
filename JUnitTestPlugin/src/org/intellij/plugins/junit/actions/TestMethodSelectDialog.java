/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestMethodSelectDialog.java,v 1.5 2005/08/20 09:54:26 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/08/20 09:54:26 $
 *
 *
}Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.junit.MethodLocator;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestMethodDef;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.junit.TestedMethodDef;
import org.intellij.plugins.junit.actions.TestMethodSelectPanel.ActionHandler;
import org.intellij.plugins.ui.SelectDialog;
import org.intellij.plugins.util.PluginPsiUtil;


public class TestMethodSelectDialog extends SelectDialog {

    public TestMethodSelectDialog(Project project, MethodLocator methodLocator, PluginPsiUtil pluginPsiUtil) {
        super(project, new TestMethodSelectPanel(methodLocator, pluginPsiUtil));
    }

    public void show(TestMethodDef[] testMethods,  TestMethodDef[] classTestMethods, TestClassDef testClassDef,
                     TestedMethodDef testedMethodDef, TestedClassDef testedClassDef, ActionHandler actionHandler) {

        final TestMethodSelectPanel sPanel = (TestMethodSelectPanel) panel;
        sPanel.show(testMethods, classTestMethods,testClassDef,testedMethodDef,testedClassDef,actionHandler);
        finishShow();
    }
}

