/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestMethodSelectPanel.java,v 1.7 2005/08/20 16:55:54 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/08/20 16:55:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.junit.*;
import org.intellij.plugins.ui.SelectPanel;
import org.intellij.plugins.ui.SelectPanelListModel;
import org.intellij.plugins.util.JPanelBuilder;
import org.intellij.plugins.util.PluginPsiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;

public class TestMethodSelectPanel extends SelectPanel {

    private MethodLocator methodLocator;
    private PluginPsiUtil pluginPsiUtil;
    private ActionHandler actionHandler;
    //DEBT: Add these to the plugin configuration
    public static boolean displayAllTests;
    public static boolean prettyTestNames;
    private JCheckBox allTestsCheckbox;
    private JCheckBox prettyTestsCheckbox;
    public static final String NEW_TEST_CASE_TITLE = "New test case";
    private Choice[] testMethodChoices;
    private Choice[] testClassChoices;
    private TestedClassDef testedClass;
    private String methodTitle;
    private String classTitle;

    public interface ActionHandler {

        void createTestMethod(TestClassDef testClass, TestMethodDef testedMethod);

        void goToTestMethod(TestMethodDef testMethodDef);
    }

    public TestMethodSelectPanel(MethodLocator methodLocator, PluginPsiUtil pluginPsiUtil) {
        super(new TestMethodComparator(), new TestMethodSelectPanelKeyAdapter());
        this.methodLocator = methodLocator;
        this.pluginPsiUtil = pluginPsiUtil;
    }

    public void show(TestMethodDef[] methodTestMethods,
                     TestMethodDef[] classTestMethods,
                     TestClassDef testClassDef,
                     TestedMethodDef testedMethodDef,
                     TestedClassDef testedClassDef,
                     ActionHandler actionHandler) {
        this.testedClass = testedClassDef;
        this.actionHandler = actionHandler;
        this.testMethodChoices = createChoices(methodTestMethods, testClassDef);
        this.testClassChoices = createChoices(classTestMethods, testClassDef);
        this.methodTitle = "Test methods for " + testedMethodDef.getName() + "()";
        this.classTitle = "Test methods for " + testedClass.getName();
        setDisplayPrettyTestNames(prettyTestNames);
        if (displayAllTests) {
            int index = findSelectedMethodIndex(classTestMethods);
            show(classTitle, testClassChoices[index + 1], testClassChoices);
        } else {
            int index = findSelectedMethodIndex(methodTestMethods);
            show(methodTitle, testMethodChoices[index + 1], testMethodChoices);
        }
        allTestsCheckbox.setSelected(displayAllTests);
        prettyTestsCheckbox.setSelected(prettyTestNames);

    }

    private Choice[] createChoices(TestMethodDef[] testMethods, TestClassDef testClass) {
        Choice[] choices = new Choice[testMethods.length + 1];
        choices[0] = createNewTestCaseChoice(testClass, testMethods);
        for (int i = 0; i < testMethods.length; i++) {
            choices[i + 1] = createNavigateToTestMethodChoice(testMethods[i]);
        }
        return choices;
    }

    private Choice createNewTestCaseChoice(final TestClassDef testClass, final TestMethodDef[] testMethods) {
        return new Choice(NEW_TEST_CASE_TITLE, testClass, new ChoiceListener() {
            public void itemChosen(Object choice) {
                TestMethodDef insertAfterMethod = testMethods.length > 0 ? testMethods[testMethods.length - 1] : null;
                actionHandler.createTestMethod(testClass, insertAfterMethod);
            }
        });
    }

    private Choice createNavigateToTestMethodChoice(final TestMethodDef method) {
        return new Choice(method.getName(), method, new ChoiceListener() {
            public void itemChosen(Object choice) {
                actionHandler.goToTestMethod(method);
            }
        });
    }

    private int findSelectedMethodIndex(TestMethodDef[] methods) {
        if (methods.length > 0 && methods[0].getPsiMethod() != null) {
            PsiElement childElement = pluginPsiUtil.getElementAtCaretInFile(methods[0].getPsiMethod());
            if (childElement != null) {
                for (int i = 0; i < methods.length; i++) {
                    PsiMethod method = methods[i].getPsiMethod();
                    if (pluginPsiUtil.isAncestor(childElement, method)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public void toggleAllTests() {
        allTestsCheckbox.setSelected(!allTestsCheckbox.isSelected());
        setDisplayAllTests(allTestsCheckbox.isSelected());
    }

    public void togglePrettyTests() {
        prettyTestsCheckbox.setSelected(!prettyTestsCheckbox.isSelected());
        setDisplayPrettyTestNames(prettyTestsCheckbox.isSelected());
    }

    public JComponent createTitleBar() {
        JPanelBuilder builder = new JPanelBuilder();
        builder.setOverrideInsets(new Insets(0, 0, 0, 0));

        builder.add(super.createTitleBar());
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);

        builder.add(sep);
        allTestsCheckbox = createCheckbox();
        allTestsCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDisplayAllTests(allTestsCheckbox.isSelected());
            }
        });
        builder.add(allTestsCheckbox, createLabel("All tests", 'A'));

        prettyTestsCheckbox = createCheckbox();
        prettyTestsCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDisplayPrettyTestNames(prettyTestsCheckbox.isSelected());
            }
        });
        builder.add(prettyTestsCheckbox, createLabel("Pretty up test names", 'P'));
        JPanel panel = builder.getPanel();
        panel.setBackground(TITLE_COLOR);
        return panel;
    }

    private void setDisplayAllTests(boolean allTests) {
        displayAllTests = allTests;
        Object selectedChoice = getSelectedValue();
        Choice[] newChoices = allTests ? testClassChoices : testMethodChoices;
        //noinspection unchecked
        ((SelectPanelListModel) getList().getModel()).setAll(newChoices);
        maintainSelection(selectedChoice, newChoices);
        titleLabel.setText(allTests ? classTitle : methodTitle);
        resize();
    }

    private void maintainSelection(Object selectedChoice, Choice[] newChoices) {
        Choice newSelection = null;
        if (selectedChoice != null) {
            for (int i = 0; i < newChoices.length; i++) {
                Choice choice = newChoices[i];
                if (choice.value == selectedChoice) {
                    newSelection = choice;
                    break;
                }
            }
        }
        getList().setSelectedValue(newSelection, true);
    }

    private void setDisplayPrettyTestNames(boolean prettyNames) {
        prettyTestNames = prettyNames;
        changeChoiceText(testMethodChoices, prettyNames, false);
        changeChoiceText(testClassChoices, prettyNames, true);
        ((SelectPanelListModel) getList().getModel()).contentsChanged();
        resize();
    }

    private void changeChoiceText(Choice[] choices, boolean prettyNames, boolean allTests) {
        NamePrettifier prettifier = new NamePrettifier(methodLocator);
        for (int i = 1; i < choices.length; i++) {
            Choice choice = choices[i];
            TestMethodDef method = (TestMethodDef) choice.value;
            String prettyName;
            if (prettyNames) {
                prettyName = prettifier.prettifyTestMethod(testedClass, allTests, method);
            } else {
                prettyName = method.getName();
            }
            choice.text = prettyName;
        }
    }

    private static JCheckBox createCheckbox() {
        JCheckBox prettyTestsCheckbox = new JCheckBox();
        prettyTestsCheckbox.setFocusable(false);
        prettyTestsCheckbox.setBackground(TITLE_COLOR);
        return prettyTestsCheckbox;
    }

    private static JLabel createLabel(String text, char mnemonicChar) {
        JLabel lc = new JLabel(text);
        lc.setDisplayedMnemonic(mnemonicChar);
        lc.setBackground(TITLE_COLOR);
        return lc;
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public static class TestMethodComparator implements Comparator/*<SelectPanel.Choice>*/ {

        public int compare(/*SelectPanel.Choice*/Object choice, /*SelectPanel.Choice*/Object choice1) {
            Choice c1 = (Choice) choice;
            Choice c2 = (Choice) choice1;
            if (NEW_TEST_CASE_TITLE.equals(c1.text)) return -1;
            if (NEW_TEST_CASE_TITLE.equals(c2.text)) return 1;
            return c1.text.compareToIgnoreCase(c2.text);
        }
    }
}
