/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassPatternDesc.java,v 1.3 2005/08/19 08:44:44 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/19 08:44:44 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.intellij.plugins.junit.ClassPattern;
import org.intellij.plugins.junit.Validator;

public class ClassPatternDesc implements Cloneable, Validator {

    public String testClassPattern;
    public String testedClassPattern;

    public static final String ROOT_ELEMENT = "classPattern";
    public static final String TESTED_CLASS_ATTRIBUTE = "testedClass";
    public static final String TEST_CLASS_ATTRIBUTE = "testClass";

    public ClassPatternDesc() {
        this("$CLASS$", "test/$CLASS$Test");
    }

    public ClassPatternDesc(String testedClassPattern, String testClassPattern) {
        this.testedClassPattern = testedClassPattern;
        this.testClassPattern = testClassPattern;
    }

    public void readXML(Element element) throws InvalidDataException {
        testedClassPattern = element.getAttribute(TESTED_CLASS_ATTRIBUTE).getValue();
        testClassPattern = element.getAttribute(TEST_CLASS_ATTRIBUTE).getValue();
    }

    public Element writeXML() throws WriteExternalException {
        if(!isValid()) return null;
        Element root = new Element(ROOT_ELEMENT);
        root.setAttribute(TESTED_CLASS_ATTRIBUTE, testedClassPattern);
        root.setAttribute(TEST_CLASS_ATTRIBUTE, testClassPattern);
        return root;
    }

    public String getTestClassPattern() {
        return testClassPattern;
    }

    public void setTestClassPattern(String testClassPattern) {
        this.testClassPattern = testClassPattern;
    }

    public String getTestedClassPattern() {
        return testedClassPattern;
    }

    public void setTestedClassPattern(String testedClassPattern) {
        this.testedClassPattern = testedClassPattern;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassPatternDesc)) return false;

        final ClassPatternDesc desc = (ClassPatternDesc) o;

        if (testClassPattern != null ? !testClassPattern.equals(desc.testClassPattern) : desc.testClassPattern != null) return false;
        if (testedClassPattern != null ? !testedClassPattern.equals(desc.testedClassPattern) : desc.testedClassPattern !=
                null) {
            return false;
        }

        return true;
    }


    public boolean isValid() {
        try {
            return ClassPattern.isValid(testedClassPattern, testClassPattern);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public int hashCode() {
        int result;
        result = (testClassPattern != null ? testClassPattern.hashCode() : 0);
        result = 29 * result + (testedClassPattern != null ? testedClassPattern.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ClassPatternDesc(" + testedClassPattern + "<->" + testClassPattern + ")";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ClassPattern getClassPattern() {
        return new ClassPattern(testedClassPattern, testClassPattern);
    }
}