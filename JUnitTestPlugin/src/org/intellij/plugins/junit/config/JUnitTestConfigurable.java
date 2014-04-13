/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/JUnitTestConfigurable.java,v 1.19 2005/11/19 12:00:17 shadow12 Exp $
 * $Revision: 1.19 $
 * $Date: 2005/11/19 12:00:17 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.util.OpenApiFacade;
import org.intellij.plugins.junit.config.MainConfigEditor;
import org.intellij.plugins.junit.Icons;
import org.intellij.plugins.junit.Version;
import org.intellij.plugins.util.JPanelBuilder;
import org.intellij.plugins.util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JUnitTestConfigurable extends MainConfigEditor {

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.config.JUnitTestConfigurable");

    private JCheckBox isAutoTestRefactoringEnabled;
    private JCheckBox isAutoTestClassRenameEnabled;
    private JCheckBox isAutoTestClassMoveEnabled;
    private JCheckBox isAutoTestMethodsRenameEnabled;
    private JCheckBox isUntestedMethodIconsEnabled;

    private JCheckBox isUnitTestPluginEnabled;

    private JCheckBox isIconsOnSetters;
    private JCheckBox isIconsOnGetters;

    private TemplateConfigEditor classTemplateConfigEditor;
    private TemplateConfigEditor methodTemplateConfigEditor;
    private ClassPatternConfigEditor classPatternConfigEditor;

    private static boolean isActionsRegistered = false;
    public static final String CLASS_TEMPLATE_HELP = "          <table border=\"1\" cellspacing=\"1\" cellpadding=\"0\">\n" +
            "     <tr><th  bgcolor=\"#99CCCC\"> <strong>Name</strong> </th><th  bgcolor=\"#99CCCC\"> <strong>Description</strong> </th></tr>\n" +
            "     <tr><td  bgcolor=\"#FFFFCC\"> TESTED_CLASS_VARIABLE_NAME </td><td  bgcolor=\"#FFFFCC\"> fixture variable name of the class under test </td></tr>\n" +
            "\n" +
            "     <tr><td  bgcolor=\"#FFFFFF\"> TESTED_CLASS_NAME </td><td  bgcolor=\"#FFFFFF\"> name of class under test </td></tr>\n" +
            "     <tr><td  bgcolor=\"#FFFFCC\"> TESTED_CLASS_PACKAGE_NAME </td><td  bgcolor=\"#FFFFCC\"> package of class under test </td></tr>\n" +
            "     </table>";

    public static final String METHOD_TEMPLATE_HELP =
            "     <table border=\"1\" cellspacing=\"1\" cellpadding=\"0\">\n" +
                    "     <tr><th  bgcolor=\"#99CCCC\"> <strong>Name</strong> </th><th  bgcolor=\"#99CCCC\"> <strong>Description</strong> </th></tr>\n" +
                    "     <tr><td  colspan=\"2\" bgcolor=\"#FFFFCC\"> <em>All Class template variables above</em> </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFFF\"> METHOD_NAME </td><td  bgcolor=\"#FFFFFF\"> name of test method </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFCC\"> TESTED_METHOD </td><td  bgcolor=\"#FFFFCC\"> Method under test (See structure below) </td></tr>\n" +
                    "     </table>\n" +
                    "     <p>\n" +
                    "     <table border=\"1\" cellspacing=\"1\" cellpadding=\"0\" >\n" +
                    "     <tr><th  colspan=\"3\" bgcolor=\"#99CCCC\"> <strong>Types</strong> </th></tr>\n" +
                    "     <tr><th  bgcolor=\"#99CCCC\"> <strong>Type name</strong> </th><th  bgcolor=\"#99CCCC\"> <strong>Field name</strong> </th><th  bgcolor=\"#99CCCC\"> <strong>Field type</strong> </th></tr>\n" +
                    "     <tr><td  colspan=\"3\" bgcolor=\"#FFFFCC\"> Method </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFFF\"> &nbsp;  </td><td  bgcolor=\"#FFFFFF\"> Name </td><td  bgcolor=\"#FFFFFF\"> string </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFCC\"> &nbsp;  </td><td  bgcolor=\"#FFFFCC\"> Return </td><td  bgcolor=\"#FFFFCC\"> string </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFFF\"> &nbsp;  </td><td  bgcolor=\"#FFFFFF\"> Parameters </td><td  bgcolor=\"#FFFFFF\"> list of Parameter </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFCC\"> &nbsp;  </td><td  bgcolor=\"#FFFFCC\"> Exceptions </td><td  bgcolor=\"#FFFFCC\"> list of string </td></tr>\n" +
                    "     <tr><td  colspan=\"3\" bgcolor=\"#FFFFFF\"> Parameter </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFCC\"> &nbsp;  </td><td  bgcolor=\"#FFFFCC\"> Type </td><td  bgcolor=\"#FFFFCC\"> string </td></tr>\n" +
                    "     <tr><td  bgcolor=\"#FFFFFF\"> &nbsp;  </td><td  bgcolor=\"#FFFFFF\"> Name </td><td  bgcolor=\"#FFFFFF\"> string </td></tr>\n" +
                    "     </table>\n" +
                    "";

    public static final String PATTERNS_ERROR_STRING = "<table border=\"1\" cellspacing=\"1\" cellpadding=\"0\">\n" +
            "<tr><td colspan=\"2\"><font color=\"red\">ERROR: Invalid Patterns please check you have entered them correctly</font>" +
            "</td></tr></table>";
    private JPanel classPatternPanelPlaceHolder = new JPanel();

    public static JUnitTestConfigurable getInstance(Project project) {
        return (JUnitTestConfigurable) project.getComponent(JUnitTestConfigurable.class);
    }

    public JUnitTestConfigurable(Project project) {
        super(project, JUnitTestConfiguration.class, Version.class);
        this.classTemplateConfigEditor = new TemplateConfigEditor(project, ClassTemplateConfig.class, "Test class", CLASS_TEMPLATE_HELP);
        this.methodTemplateConfigEditor = new TemplateConfigEditor(project, MethodTemplateConfig.class, "Test method", METHOD_TEMPLATE_HELP);
        setClassPatternConfigEditor(new ClassPatternConfigEditor(new ClassPatternPanel(ClassPatternConfig.getInstance(project))));
    }

    public void setClassPatternConfigEditor(ClassPatternConfigEditor classPatternConfigEditor) {
        this.classPatternConfigEditor = classPatternConfigEditor;
        this.classPatternPanelPlaceHolder.removeAll();
        this.classPatternPanelPlaceHolder.add(classPatternConfigEditor.createEditorPanel());
    }

    protected JComponent createMainEditorPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patterns", createPatternPanel());
        tabbedPane.addTab("Templates", createTemplatePanel());
        tabbedPane.addTab("Options", createOptionsPanel());
        return tabbedPane;
    }

    // todo make this a panel ?
    protected JComponent createDisablePanel() {
        isUnitTestPluginEnabled = createUnitTestEnabledCheckbox();
        return isUnitTestPluginEnabled;
    }

    private JCheckBox createUnitTestEnabledCheckbox() {
        JCheckBox result = new JCheckBox("Enable unitTest");
        result.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        return result;
    }

    private JComponent createPatternPanel() {
        return classPatternPanelPlaceHolder;
    }

    private JPanel createOptionsPanel() {
        JPanelBuilder builder = new JPanelBuilder();
        builder.add(createRefactoringOptionsPanel());
        builder.add(createMethodIconsOptionsPanel());
        return builder.getPanel();
    }

    private JPanel createTemplatePanel() {
        JPanelBuilder builder = new JPanelBuilder();
        builder.add(createTestClassTemplatePanel());
        builder.add(createTestMethodTemplatePanel());
        builder.add(createEditMethodTemplatePanel());
        return builder.getPanel();
    }

    private JComponent createEditMethodTemplatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        JComponent button = getEditFileTemplatesButton();
        if (button == null) {
            panel.add(new JLabel("To add/remove/edit any templates, please go to the Settings dialog, File Templates"));
        } else {
            panel.add(new JLabel("To add/remove/edit templates press"), c);
            c.gridx = 1;
            panel.add(button, c);
        }
        return panel;
    }

    private JComponent getEditFileTemplatesButton() {
        ActionManager actionManager = OpenApiFacade.getActionManager();
        if (actionManager == null || getEditFileTemplatesAction() == null) return null;

        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new AnAction("Edit File Templates") {
            public void actionPerformed(AnActionEvent e) {
                getEditFileTemplatesAction().actionPerformed(e);
                classTemplateConfigEditor.updateAvailableTemplates();
                methodTemplateConfigEditor.updateAvailableTemplates();
            }

            public void update(AnActionEvent e) {
                e.getPresentation().setText("Edit File Templates");
                e.getPresentation().setVisible(true);
                e.getPresentation().setEnabled(true);
                e.getPresentation().setIcon(Icons.EDIT_TEMPLATE_ACTION);
            }
        });
        return actionManager.createActionToolbar("JUnit Test Configuration", group, true).getComponent();
    }

    private AnAction getEditFileTemplatesAction() {
        return OpenApiFacade.getActionManager().getAction("EditFileTemplates");
    }


    private JPanel createTestClassTemplatePanel() {
        JPanelBuilder builder = new JPanelBuilder();
        builder.add(classTemplateConfigEditor.createEditorPanel());
        return builder.getTitledPanel("Test Class Generation");
    }

    private JPanel createTestMethodTemplatePanel() {
        JPanelBuilder builder = new JPanelBuilder();
        builder.add(methodTemplateConfigEditor.createEditorPanel());
        return builder.getTitledPanel("Test Method Generation");
    }

    private JPanel createRefactoringOptionsPanel() {
        JPanelBuilder builder = new JPanelBuilder();
        isAutoTestRefactoringEnabled = createAutoTestRefactoringCheckBox();
        builder.add(isAutoTestRefactoringEnabled);
        isAutoTestClassRenameEnabled = new JCheckBox("Rename test classes");
        builder.add(isAutoTestClassRenameEnabled, true);
        isAutoTestClassMoveEnabled = new JCheckBox("Move test classes");
        builder.add(isAutoTestClassMoveEnabled, true);
        isAutoTestMethodsRenameEnabled = new JCheckBox("Rename test methods");
        builder.add(isAutoTestMethodsRenameEnabled, true);
        return builder.getTitledPanel("Automatic Test Refactoring");
    }

    private JCheckBox createAutoTestRefactoringCheckBox() {
        JCheckBox jCheckBox = new JCheckBox("Refactor test elements as associated tested elements are");
        jCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetAutoTestRefactoringFieldsEnabled();
            }
        });
        return jCheckBox;
    }

    private void resetAutoTestRefactoringFieldsEnabled() {
        isAutoTestClassRenameEnabled.setEnabled(isAutoTestRefactoringEnabled.isSelected());
        isAutoTestClassMoveEnabled.setEnabled(isAutoTestRefactoringEnabled.isSelected());
        isAutoTestMethodsRenameEnabled.setEnabled(isAutoTestRefactoringEnabled.isSelected());
    }

    private JPanel createMethodIconsOptionsPanel() {
        JPanelBuilder builder = new JPanelBuilder();
        isUntestedMethodIconsEnabled = createUntestedMethodIconsCheckbox();
        builder.add(isUntestedMethodIconsEnabled);
        isIconsOnSetters = new JCheckBox("Include icons for setters");
        builder.add(isIconsOnSetters, true);
        isIconsOnGetters = new JCheckBox("Include icons for getters");
        builder.add(isIconsOnGetters, true);
        return builder.getTitledPanel("Untested Methods Icons");
    }


    private void resetAddIconsToUntestedMethodsFieldsEnabled() {
        isIconsOnGetters.setEnabled(isUntestedMethodIconsEnabled.isSelected());
        isIconsOnSetters.setEnabled(isUntestedMethodIconsEnabled.isSelected());
    }


    private JCheckBox createUntestedMethodIconsCheckbox() {
        JCheckBox jCheckBox = new JCheckBox("Annotate untested methods with icon in gutter");
        jCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetAddIconsToUntestedMethodsFieldsEnabled();
            }
        });
        return jCheckBox;
    }

    public String getDisplayName() {
        return "JUnit";
    }

    public String getHelpTopic() {
        return null;
    }

    public Icon getIcon() {
        return Icons.JUNIT_TEST_PLUGIN;
    }

    public void reset() {
        super.reset();
        classTemplateConfigEditor.reset();
        methodTemplateConfigEditor.reset();
        classPatternConfigEditor.reset();
    }

    public void apply() throws ConfigurationException {
        super.apply();
        classTemplateConfigEditor.apply();
        methodTemplateConfigEditor.apply();
        classPatternConfigEditor.apply();
    }

    public JUnitTestConfiguration getConfig() {
        return (JUnitTestConfiguration) getConfiguration();
    }

    protected void copyFromConfig() {
        isAutoTestRefactoringEnabled.setSelected(getConfig().IS_AUTO_TEST_CLASS_REFACTORING_ENABLED);
        isAutoTestClassRenameEnabled.setSelected(getConfig().IS_AUTO_TEST_CLASS_RENAME_ENABLED);
        isAutoTestClassMoveEnabled.setSelected(getConfig().IS_AUTO_TEST_CLASS_MOVE_ENABLED);
        isAutoTestMethodsRenameEnabled.setSelected(getConfig().IS_AUTO_TEST_METHODS_RENAME_ENABLED);
        resetAutoTestRefactoringFieldsEnabled();
        isUntestedMethodIconsEnabled.setSelected(getConfig().IS_UNTESTED_METHOD_ICON_ENABLED);
        isIconsOnGetters.setSelected(getConfig().IS_GET_METHOD_ICONS_ENABLED);
        isIconsOnSetters.setSelected(getConfig().IS_SET_METHOD_ICONS_ENABLED);
        resetAddIconsToUntestedMethodsFieldsEnabled();
        isUnitTestPluginEnabled.setSelected(getConfig().IS_UNIT_TEST_ENABLED);
    }

    protected void copyToConfig() {
        getConfig().IS_AUTO_TEST_CLASS_REFACTORING_ENABLED = isAutoTestRefactoringEnabled.isSelected();
        getConfig().IS_AUTO_TEST_CLASS_RENAME_ENABLED = isAutoTestClassRenameEnabled.isSelected();
        getConfig().IS_AUTO_TEST_CLASS_MOVE_ENABLED = isAutoTestClassMoveEnabled.isSelected();
        getConfig().IS_AUTO_TEST_METHODS_RENAME_ENABLED = isAutoTestMethodsRenameEnabled.isSelected();
        getConfig().IS_UNTESTED_METHOD_ICON_ENABLED = isUntestedMethodIconsEnabled.isSelected();
        getConfig().IS_SET_METHOD_ICONS_ENABLED = isIconsOnSetters.isSelected();
        getConfig().IS_GET_METHOD_ICONS_ENABLED = isIconsOnGetters.isSelected();
        getConfig().IS_UNIT_TEST_ENABLED = isUnitTestPluginEnabled.isSelected();
    }

    public boolean isModified() {
        final boolean equals =
                getConfig().IS_AUTO_TEST_CLASS_REFACTORING_ENABLED == isAutoTestRefactoringEnabled.isSelected() &&
                        getConfig().IS_AUTO_TEST_CLASS_RENAME_ENABLED == isAutoTestClassRenameEnabled.isSelected() &&
                        getConfig().IS_AUTO_TEST_CLASS_MOVE_ENABLED == isAutoTestClassMoveEnabled.isSelected() &&
                        getConfig().IS_AUTO_TEST_METHODS_RENAME_ENABLED == isAutoTestMethodsRenameEnabled.isSelected() &&
                        getConfig().IS_UNTESTED_METHOD_ICON_ENABLED == isUntestedMethodIconsEnabled.isSelected() &&
                        getConfig().IS_SET_METHOD_ICONS_ENABLED == isIconsOnSetters.isSelected() &&
                        getConfig().IS_GET_METHOD_ICONS_ENABLED == isIconsOnGetters.isSelected() &&
                        getConfig().IS_UNIT_TEST_ENABLED == isUnitTestPluginEnabled.isSelected();
        return !equals ||
                classTemplateConfigEditor.isModified() ||
                methodTemplateConfigEditor.isModified() ||
                classPatternConfigEditor.isModified();
    }

    public void projectOpened() {
        super.projectOpened();
        this.classTemplateConfigEditor.projectOpened();
        this.methodTemplateConfigEditor.projectOpened();
        registerActions();
    }

    public String getComponentName() {
        return "JUnitTestConfigurable";
    }

    private void registerActions() {
        if (isActionsRegistered) return;

        debug("registering actions from " + LogUtil.getCallerMethod() + " of " + getProjectName());

        DefaultActionGroup goToMenu = getGoToEditorPopupSubMenu();
        if (goToMenu != null) {
            AnAction toggleTextAction = OpenApiFacade.getActionManager().getAction(
                    "JUnitTestPlugin.ToggleTestCaseTestedClassAction");
            if (toggleTextAction != null) {
                goToMenu.add(toggleTextAction);
                isActionsRegistered = true;
                debug("actions registration completed");
            }
        }
    }

    private DefaultActionGroup getGoToEditorPopupSubMenu() {
        DefaultActionGroup goToMenu = null;
        ActionManager actionManager = OpenApiFacade.getActionManager();
        if (actionManager == null) return null;
        ActionGroup editorPopupGroup = (ActionGroup) actionManager.getAction("EditorPopupMenu");
        if (editorPopupGroup == null) {
            debug("Could not find menu EditorPopupMenu");
        } else {
            AnAction[] children = editorPopupGroup.getChildren(null);
            for (int i = 0; i < children.length; i++) {
                AnAction child = children[i];
                if ("Go To".equalsIgnoreCase(child.getTemplatePresentation().getText())) {
                    if (!(child instanceof DefaultActionGroup)) {
                        break;
                    }
                    debug("found Go to menu in EditorPopupMenu");
                    goToMenu = (DefaultActionGroup) child;
                }
            }
        }
        return goToMenu;
    }

    public void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }
}
