/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestMethodProcessor.java,v 1.4 2005/08/20 16:55:54 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/08/20 16:55:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.intellij.plugins.junit.MethodLocator;
import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestMethodDef;
import org.intellij.plugins.junit.config.MethodTemplateConfig;
import org.intellij.plugins.junit.template.TemplateSelectorCreator.ElementCreationListener;
import org.intellij.plugins.junit.template.TestMethodCreator;

public abstract class TestMethodProcessor extends JUnitTestCaseProcessor {

    protected MethodTemplateConfig methodTemplateConfig;
    protected MethodLocator methodLocator;
    protected TestMethodCreator testMethodCreator;
    protected EditorHelper editorHelper;

    public TestMethodProcessor(MethodTemplateConfig methodTemplateConfig,
                               MethodLocator methodLocator,
                               TestMethodCreator testMethodCreator,
                               EditorHelper editorHelper) {
        this.methodTemplateConfig = methodTemplateConfig;
        this.methodLocator = methodLocator;
        this.testMethodCreator = testMethodCreator;
        this.editorHelper = editorHelper;
    }

    public void selectMethodTemplateAndCreate(final TestClassDef testClassDef, final PsiMethod method) {
        debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> testClassDef = " + testClassDef);
        testMethodCreator.createMethod(originMethod, testClassDef.getPsiClass(), method, new ElementCreationListener() {
            public void elementCreated(Object newTestMethod) {
                PsiMethod testMethod = (PsiMethod) newTestMethod;
                TestMethodDef testMethodDef = new TestMethodDef(testMethod);
                testClassDef.addTestMethod(testMethodDef);
                goToThatTestMethod(testMethod, MethodOffsetType.NAME_END);
            }
        });
    }

    protected void goToThatTestMethod(PsiMethod testMethod, MethodOffsetType offsetType) {
        if (testMethod == null) return;
        PsiClass testClass = testMethod.getContainingClass();
        debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> testClass=" + testClass);
        editorHelper.openFile(testClass, testClass.getTextOffset());

        debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> testMethod=" + testMethod);
        editorHelper.moveCaretTo(testMethod, offsetType.getOffset(testMethod));
    }

}
