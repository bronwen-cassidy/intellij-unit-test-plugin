/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 09-Mar-2006
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 09-Mar-2006 16:30:57
 * @version 0.1
 */

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.intellij.testFramework.LightCodeInsightTestCase;
import org.intellij.plugins.beans.PluginContext;
import org.intellij.plugins.junit.config.ClassPatternConfig;
import org.intellij.plugins.junit.config.ClassPatternDesc;

import java.util.ArrayList;
import java.util.List;

public class ClassDefFactoryTest extends LightCodeInsightTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        Project project = getProject();
        context = new PluginContext(project, "/META-INF/plugin.xml", "/META-INF/testPluginContext.xml");
        // setup test classpaths
        ClassPatternConfig patternConfig = (ClassPatternConfig) context.getBean(ClassPatternConfig.class);
        List testItems = new ArrayList();
        testItems.add(new ClassPatternDesc("src/$CLASS$", "src/tests/$CLASS$Test"));
        patternConfig.setItems(testItems);
    }

    public void testGetTestedClassDef() throws Exception {
        final String testClass = "/src/org/Lecturer.java";
        createClass(testClass);
        classDefFactory = (ClassDefFactory) context.getBean(ClassDefFactory.class);
        assertNotNull(classDefFactory);
        TestedClassDef classDef = classDefFactory.getTestedClassDef(getPsiClass());
        assertEquals("org", classDef.getPackageName());
    }

    public void testGetTestedClassDefTested() throws Exception {
        final String testedClass = "/src/org/Lecturer.java";
        createClass(testedClass);
        // get the test class
        final String testClass = "/tests/org/LecturerTest.java";
        createClass(testClass);
        classDefFactory = (ClassDefFactory) context.getBean(ClassDefFactory.class);
        TestedClassDef classDef = classDefFactory.getTestedClassDef(getPsiClass());
        assertNotNull(classDef.getTestClassDef());
    }

    public void testJavaLangObjectSuperMethod() throws Exception {
        final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(getProject()).getElementFactory();
        final PsiClass aClass = elementFactory.createClassFromText("public String toString() {return null;}", null);
        final PsiMethod method = aClass.getMethods()[0];
        final PsiMethod[] superMethods = method.findSuperMethods();
        assertNotNull(superMethods);
        assertEquals(1, superMethods.length);
        assertEquals("java.lang.Object", superMethods[0].getContainingClass().getQualifiedName());
    }

    protected void createClass(final String testClass) {
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            public void run() {
                try {
                    configureByFile(testClass);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    protected PsiClass getPsiClass() {
        PsiElement psiElement = myFile.getOriginalElement();
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiElement;
        return psiJavaFile.getClasses()[0];
    }


    private ClassDefFactory classDefFactory;
    protected PluginContext context;
}