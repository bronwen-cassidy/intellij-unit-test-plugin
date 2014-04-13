/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/GoToUnitTestProcessor.java,v 1.6 2005/08/20 19:17:03 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/08/20 19:17:03 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import org.intellij.plugins.junit.ClassLocator;
import org.intellij.plugins.junit.MethodLocator;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestMethodDef;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.junit.TestedMethodDef;
import org.intellij.plugins.junit.config.MethodTemplateConfig;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.ElementCreationListener;
import org.intellij.plugins.junit.template.TestClassCreator;
import org.intellij.plugins.junit.template.TestMethodCreator;
import org.intellij.plugins.ui.MessageHandler;

public class GoToUnitTestProcessor extends TestMethodProcessor {

    private ClassLocator classLocator;
    private TestClassCreator testClassCreator;
    private TestMethodSelectDialog methodSelectDialog;
    private MessageHandler messageHandler;
    private TestedClassDef testedClassDef;
    private TestClassSelectDialog testClassSelectDialog;

    public GoToUnitTestProcessor(MethodTemplateConfig methodTemplateConfig,
                                 MethodLocator methodLocator,
                                 TestMethodCreator testMethodCreator,
                                 ClassLocator classLocator,
                                 TestClassCreator testClassCreator,
                                 TestMethodSelectDialog methodSelectDialog,
                                 TestClassSelectDialog testClassSelectDialog,
                                 MessageHandler messageHandler,
                                 EditorHelper editorHelper) {
        super(methodTemplateConfig, methodLocator, testMethodCreator, editorHelper);
        this.classLocator = classLocator;
        this.testClassCreator = testClassCreator;
        this.methodSelectDialog = methodSelectDialog;
        this.messageHandler = messageHandler;
        this.testClassSelectDialog = testClassSelectDialog;
    }

    public void execute() {
        if (!isOriginClassConform()) return;
        testedClassDef = classLocator.getTestedClassDef(originClass);
        if (classLocator.hasOnlyOnePattern()) {
            executeSingle();
        } else {
            showTestClassSelections();
        }
    }

    private void executeSingle() {
        if (testedClassDef.isTestClassCreated()) {
            openTestAndGoToTestMethod(testedClassDef.getTestClassDef());
        } else {
            selectClassTemplateAndCreate(testedClassDef.getTestClassDef());
        }
    }

    public void selectClassTemplateAndCreate(TestClassDef testClass) {
        testClassCreator.createClass(testedClassDef, testClass, new ElementCreationListener() {
            public void elementCreated(Object createdElement) {
                TestClassDef testClassDef = (TestClassDef) createdElement;
                testedClassDef.addTestClass(testClassDef);
                openTestAndGoToTestMethod(testClassDef);
            }
        });
    }

    private void showTestClassSelections() {
        testClassSelectDialog.show(testedClassDef, new TestClassSelectPanel.TestClassActionHandler() {
            public void createOrGoToTestClass(TestClassDef testClassDef) {
                if (testClassDef.hasTestClass()) {
                    openTestAndGoToTestMethod(testClassDef);
                } else {
                    selectClassTemplateAndCreate(testClassDef);
                }
            }
        });
    }

    public boolean isOriginClassConform() {
        if (!classLocator.isTestedClassConform(originClass)) {
            messageHandler.showMessageDialog("Current class " +
                    classLocator.getTestedClassPath(originClass) +
                    " does not comform to the defined test organization",
                    "Tested Class not Conform",
                    Messages.getErrorIcon());
            return false;
        }
        return true;
    }

    protected void openTestAndGoToTestMethod(TestClassDef testClassDef) {
        if (testClassDef == null) return;
        PsiClass testClass = testClassDef.getPsiClass();
        editorHelper.openFile(testClass, testClass.getTextOffset());
        if (originMethod == null) return;
        goToTestMethod(testClassDef);
    }


    protected void goToTestMethod(TestClassDef testClassDef) {

        TestedMethodDef testedMethodDef = testedClassDef.findMethod(originMethod);
        TestMethodDef[] testedTestMethods = methodLocator.findTestMethods(testedMethodDef, testClassDef);
        if (testedTestMethods.length == 0) {
            selectMethodTemplateAndCreate(testClassDef, null);
        } else {
            selectMethodAndGoTo(testedTestMethods, testClassDef, testedMethodDef);
        }
    }

    private void selectMethodAndGoTo(TestMethodDef[] testedTestMethods, final TestClassDef testClassDef, TestedMethodDef testedMethodDef) {
        final TestMethodDef[] testMethodDefs = testClassDef.getTestMethods();
        methodSelectDialog.show(testedTestMethods, testMethodDefs, testClassDef, testedMethodDef, testedClassDef, new TestMethodSelectPanel.ActionHandler() {
            public void createTestMethod(TestClassDef testClass, TestMethodDef testMethod) {
                selectMethodTemplateAndCreate(testClassDef, (testMethod == null ? null : testMethod.getPsiMethod()));
            }

            public void goToTestMethod(TestMethodDef testMethodDef) {
                goToThatTestMethod(testMethodDef.getPsiMethod(), MethodOffsetType.BODY_START);
            }
        });
    }
}
