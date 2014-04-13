/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/NamePrettifier.java,v 1.4 2005/08/20 09:54:26 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/08/20 09:54:26 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import org.intellij.plugins.junit.MethodDef;
import org.intellij.plugins.junit.MethodLocator;
import org.intellij.plugins.junit.TestMethodDef;
import org.intellij.plugins.junit.TestedClassDef;

import java.util.regex.Pattern;

public class NamePrettifier {

    String suffix = "Test";
    String prefix = "Test";
    private MethodLocator locator;

    public NamePrettifier(MethodLocator locator) {
        this.locator = locator;
    }

    public String prettifyTestClass(String className) {

        String title = className;
        if (suffix != null && title.endsWith(suffix)) {
            title = title.substring(0, title.lastIndexOf(suffix));
        }
        if (prefix != null && title.startsWith(prefix)) {
            title = title.substring(prefix.length());
        }
        return title;
    }

    public void setTestSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    class Transform {

        Pattern pattern;
        String replacement;

        public Transform(String pattern, String replacement) {
            this.pattern = Pattern.compile(pattern);
            this.replacement = replacement;
        }

        public String apply(String text) {
            return pattern.matcher(text).replaceAll(replacement);
        }
    }

    Transform[] decamelizationTransforms = new Transform[]{
            new Transform("([a-z])A((A[a-z])|[B-Z])", "$1 a $2"),
            new Transform("([a-z])([A-Z])", "$1 $2"),
            new Transform("([a-z])([0-9])", "$1 $2"),
            new Transform("([0-9])([a-zA-Z])", "$1 $2"),
            new Transform("([A-Z])([A-Z][a-z])", "$1 $2"),
            new Transform("_([A-Z0-9])", " $1"),
            new Transform("([_])", " "),
    };

    public String prettifyTestMethod(String testMethod, String prefix) {
        String prettyName = removePrefix(testMethod, prefix);
        prettyName = decamelize(prettyName);
        prettyName = lowerCaseWordsExceptFirst(prettyName);
        return prettyName;
    }

    private String decamelize(String prettyName) {
        for (int i = 0; i < decamelizationTransforms.length; i++) {
            prettyName = decamelizationTransforms[i].apply(prettyName);
        }
        return prettyName;
    }

    private String removePrefix(String testMethod, String prefix) {
        String prettyName = testMethod;
        if (testMethod.startsWith(prefix)) {
            prettyName = testMethod.substring(prefix.length());
        }
        return prettyName;
    }

    private String lowerCaseWordsExceptFirst(String sentence) {
        String[] words = sentence.split(" ");
        sentence = words[0];
        for (int i = 1; i < words.length; i++) {
            sentence += " " + uncapitalizeWord(words[i]);
        }
        return sentence;
    }

    private String uncapitalizeWord(String word) {
        if (isAccronym(word)) return word;
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    private boolean isAccronym(String word) {
        return word.length() > 1 && Character.isUpperCase(word.charAt(1));
    }

    public String prettifyTestMethod(TestedClassDef testedClassDef, boolean allTests, TestMethodDef testMethodDef) {
        String prefix = "test";
        return prettifyTestMethod(testMethodDef.getName(), prefix);
    }
}
