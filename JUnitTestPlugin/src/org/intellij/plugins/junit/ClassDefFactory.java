/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassDefFactory.java,v 1.15 2006/02/27 16:37:51 shadow12 Exp $
 * $Revision: 1.15 $
 * $Date: 2006/02/27 16:37:51 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import org.intellij.plugins.junit.config.ClassPatternConfig;
import org.intellij.plugins.util.FilePathUtil;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.util.PsiClassManager;
import org.intellij.plugins.util.PsiDirectoryUtil;

// todo class caching?

public class ClassDefFactory {

    protected static final Logger LOG = LogUtil.getLogger();

    private PsiClassManager psiClassManager;
    private PsiDirectoryUtil psiDirectoryUtil;
    private JavaCodeStyleManager codeStyleManager;
    private MethodDefFactory methodFactory;

    private TestPatternsFinder testPatternsFinder;

    public ClassDefFactory(PsiClassManager psiClassManager,
                           PsiDirectoryUtil psiDirectoryUtil,
                           JavaCodeStyleManager codeStyleManager,
                           ClassPatternConfig classPatternConfig,
                           MethodDefFactory methodFactory) {
        this.psiClassManager = psiClassManager;
        this.psiDirectoryUtil = psiDirectoryUtil;        
        this.codeStyleManager = codeStyleManager;
        this.methodFactory = methodFactory;
        this.testPatternsFinder = new TestPatternsFinder(classPatternConfig, psiDirectoryUtil);
    }

    public TestedClassDef getTestedClassDef(PsiClass testedClass) {

        String[] testClassPaths = getTestClassPaths(testedClass);
        TestedClassDef testedClassDef = createTestedClassDef(testedClass);
        createTestClassDefsAndAddToTestedClass(testClassPaths, testedClassDef);
        return testedClassDef;
    }

    private TestedClassDef createTestedClassDef(PsiClass testedClass) {

        TestedClassDef testedClassDef = new TestedClassDef(testedClass);
        methodFactory.populateTestedMethods(testedClassDef);
        return testedClassDef;
    }

    private void createTestClassDefsAndAddToTestedClass(String[] testClassPaths, TestedClassDef testedClassDef) {
        for (int i = 0; i < testClassPaths.length; i++) {
            String testClassPath = testClassPaths[i];

            TestClassDef testClassDef;

            String defaultFieldName = getDefaultFieldName(testedClassDef.getPsiClass());
            PsiClass testClass = psiClassManager.findPrimaryClass(testClassPath);

            if (testClass == null) {

                PsiDirectory root = psiDirectoryUtil.findRootThatContainsPath(testClassPath);
                if (root == null) continue;

                String packagePath = FilePathUtil.extractParentPathFromPath(testClassPath);
                String path = root.getVirtualFile().getPath();
                String packageName = "";
                // this is for in the mad moments people do not define a package!
                if (packagePath.length() != path.length()) {
                    packageName = FilePathUtil.replaceFileSeparator(packagePath.substring(path.length() + 1), '.');
                }
                String fileName = FilePathUtil.extractNameFromPath(testClassPath);
                String className = PsiClassManager.getPrimaryClassNameFromJavaFileName(fileName);
                testClassDef = new TestClassDef(root, packageName, className, defaultFieldName);

            } else {
                testClassDef = createTestClassDef(testClass, defaultFieldName);
            }

            testedClassDef.addTestClass(testClassDef);
            methodFactory.associateTestMethodsWithTestedMethods(testClassDef);
        }
    }

    private TestClassDef createTestClassDef(PsiClass testClass, String defaultFieldName) {
        TestClassDef testClassDef = new TestClassDef(testClass, defaultFieldName);
        methodFactory.populateTestMethods(testClassDef);

        return testClassDef;
    }

    private String getDefaultFieldName(PsiClass testedClass) {
        SuggestedNameInfo suggestedNameInfo = codeStyleManager.suggestVariableName(VariableKind.FIELD,
                testedClass.getName(),
                null,
                null);
        if (suggestedNameInfo.names != null && suggestedNameInfo.names.length > 0) {
            return suggestedNameInfo.names[0];
        }
        return "object";
    }

    public String getTestClassPath(PsiClass testedClass) {
        String[] testClassPaths = testPatternsFinder.getTestClassPaths(getClassFilePath(testedClass));
        return testClassPaths.length != 0 ? testClassPaths[0] : null;
    }

    public String[] getTestClassPaths(PsiClass testedClass) {
        return testPatternsFinder.getTestClassPaths(getClassFilePath(testedClass));
    }

    public String getTestedClassPath(PsiClass testClass) {
        String testClassFilePath = getClassFilePath(testClass);
        LOG.debug("searching for tested class with test class path: " + testClassFilePath);
        String[] testedClassPaths = testPatternsFinder.getTestedClassPaths(testClassFilePath);
        if (testedClassPaths == null || testedClassPaths.length == 0) return null;
        for (int i = 0; i < testedClassPaths.length; i++) {
            String testedClassPath = testedClassPaths[i];
            if (psiClassManager.findPrimaryClass(testedClassPath) != null) return testedClassPath;
        }
        return null;
    }

    private String getClassFilePath(PsiClass psiClass) {
        if (psiClass == null) return null;
        if(psiClass.getContainingFile() == null) return null;

        VirtualFile virtualFile = psiClass.getContainingFile().getVirtualFile();
        if(virtualFile == null) return null;
        else return virtualFile.getPath();
    }

    public boolean hasSinglePattern() {
        return testPatternsFinder.size() == 1;
    }

    public String matchTestClassPath(PsiClass testedClass, TestClassDef testClassDef) {
        return testPatternsFinder.findTestClassPath(getClassFilePath(testedClass), testClassDef.getFilePath());
    }
}