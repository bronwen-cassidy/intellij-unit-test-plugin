/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/GoToTestedClassProcessor.java,v 1.4 2005/08/20 16:55:54 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/08/20 16:55:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import org.intellij.plugins.junit.*;
import org.intellij.plugins.ui.MessageHandler;

public class GoToTestedClassProcessor extends JUnitTestCaseProcessor {

    private MethodLocator methodLocator;
    private ClassLocator classLocator;
    private MessageHandler messageHandler;
    private EditorHelper editorHelper;


    public GoToTestedClassProcessor(MethodLocator methodLocator,
                                    MessageHandler messageHandler,
                                    ClassLocator classLocator,
                                    EditorHelper editorHelper) {
        this.methodLocator = methodLocator;
        this.messageHandler = messageHandler;
        this.classLocator = classLocator;
        this.editorHelper = editorHelper;
    }

    public void execute() {

        if (!isTestClass(originClass)) return;

        TestedClassDef testedClassDef = classLocator.findTestedClassDef(originClass);
        debug("testedClass=" + testedClassDef);
        if (!testedClassDef.hasCreatedClass()) return;

        PsiClass testedClass = testedClassDef.getPsiClass();
        editorHelper.openFile(testedClass, testedClass.getTextOffset());
        if (originMethod == null) return;
        if (isTestMethod(originMethod.getName())) return;

        // FIXME Handles overloaded tested methods

        TestMethodDef testMethodDef = new TestMethodDef(originMethod);
        MethodDef testedMethod = methodLocator.findTestedMethod(testMethodDef, testedClassDef);
        if (testedMethod == null) return;
        debug("testedMethod=" + testedMethod);

        editorHelper.openFile(testedMethod.getPsiMethod(), editorHelper.getLeftBraceOffset(testedMethod.getPsiMethod()));
    }

    public boolean isTestClass(PsiClass originClass) {
        if (!classLocator.isTestClassConform(originClass)) {
            messageHandler.showMessageDialog(
                    "Current test class " +
                            classLocator.getTestClassPath(originClass) +
                            " does not conform to the defined test organization",
                    "Test Class not Conform",
                    Messages.getErrorIcon());
            return false;
        }
        return true;
    }

    private boolean isTestMethod(String testMethodName) {
        return !testMethodName.startsWith("test");
    }

}
