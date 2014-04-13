/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassPattern.java,v 1.13 2005/09/26 19:07:22 shadow12 Exp $
 * $Revision: 1.13 $
 * $Date: 2005/09/26 19:07:22 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.intellij.plugins.junit.config.ClassPatternDesc;
import org.intellij.plugins.util.RegexpUtil;

public class ClassPattern extends RegexpUtil {

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.TestPattern");

    public static final String END_OF_CLASS = END_OF_WORD + followedBy(or(DOT + "java", END_OF_STRING));
// Oro 1.2.8 2 next lines should not be escaped
    public static final String CLASS = escapeString(group(zeroOrMore(WORD_CHAR)));
    public static final String DIRECTORY = zeroOrOne(
            escapeString(group(zeroOrMore(or(or(FILE_NAME_CHAR, SLASH), DOT)) + SLASH)));
    public static final String CLASS_TOKEN = "$CLASS$";
    public static final String CLASS_TOKEN_BODY = "CLASS";
    public static final String CLASS_REGEXP_TOKEN = escapeString(CLASS_TOKEN);
    public static final String DIRECTORY_TOKEN = "$DIRECTORY$";
    public static final String DIRECTORY_TOKEN_BODY = "DIRECTORY";
    public static final String DIRECTORY_REGEX_TOKEN = escapeString(DIRECTORY_TOKEN);
    public static final String DIRECTORY_TOKEN_MATCH = DIRECTORY_REGEX_TOKEN + escapeString("/");
    public static final String DEPRECATED_PACKAGE_TOKEN = escapeString("$PACKAGE$");

    public static final ClassPattern DEFAULT_PATTERN = new ClassPattern();

    Pattern testedClassPattern;
    Pattern testClassPattern;

    public ClassPattern() {
        this("$CLASS$", "test/$CLASS$Test");
    }

    public ClassPattern(String testedClassPattern, String testClassPattern) {
        reset(testedClassPattern, testClassPattern);
    }

    public void reset(String testedClassPattern, String testClassPattern) throws IllegalArgumentException {
        validatePatterns(testedClassPattern, testClassPattern);
        this.testedClassPattern = new Pattern(testedClassPattern);
        this.testClassPattern = new Pattern(testClassPattern);
        debug(this.toString());
    }

    public void reset(ClassPatternDesc desc) throws IllegalArgumentException {
        reset(desc.testedClassPattern, desc.testClassPattern);
    }

    public static void validatePatterns(String testedClassPattern, String testClassPattern) throws IllegalArgumentException {
        boolean hasTestedClassDirectoryToken = testedClassPattern.indexOf(DIRECTORY_TOKEN) >= 0;
        boolean hasTestClassDirectoryToken = testClassPattern.indexOf(DIRECTORY_TOKEN) >= 0;
        if (hasTestClassDirectoryToken != hasTestedClassDirectoryToken) {
            throw new IllegalArgumentException(
                    "Both test class and tested class patterns may have the token $DIRECTORY$ or none of them");
        }
    }

    public static boolean isValid(String testedClassPattern, String testClassPattern) {
        boolean valid;
        try {
            validatePatterns(testedClassPattern, testClassPattern);
            boolean testClassPatternValid = testClassPattern.indexOf(CLASS_TOKEN_BODY) == -1 || testClassPattern.indexOf(CLASS_TOKEN) != -1;
            boolean testedClassPatternValid = testedClassPattern.indexOf(CLASS_TOKEN_BODY) == -1 || testedClassPattern.indexOf(CLASS_TOKEN) != -1;

            boolean testDirPatternValid = testClassPattern.indexOf(DIRECTORY_TOKEN_BODY) == -1 || testClassPattern.indexOf(DIRECTORY_TOKEN) != -1;
            boolean testedDirPatternValid = testedClassPattern.indexOf(DIRECTORY_TOKEN_BODY) == -1 || testedClassPattern.indexOf(DIRECTORY_TOKEN) != -1;
            valid = testClassPatternValid && testedClassPatternValid && testDirPatternValid && testedDirPatternValid;
        } catch (IllegalArgumentException e) {
            valid = false;
        }
        return valid;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("tested", testedClassPattern).
                append("test", testClassPattern).toString();
    }

    public String getTestedClassPath(String testClassPath) {
        if (!matchTestClassPath(testClassPath)) return null;
        String testedClassPath = substituteFirst(testClassPattern.match, testedClassPattern.replacement, backwardToForwardSlash(testClassPath));
        debug("getTestedClassPath(" + testClassPath + ")=" + testedClassPath);
        return testedClassPath;
    }

    public String getTestClassPath(String testedClassPath) {
        if (!matchTestedClassPath(testedClassPath)) return null;
        String testClassPath = substituteFirst(testedClassPattern.match,
                testClassPattern.replacement,
                backwardToForwardSlash(testedClassPath));
        debug("getMovedClassPath(" + testedClassPath + ")=" + testClassPath);
        return testClassPath;
    }

    public boolean matchTestedClassPath(String testedClassFQName) {
        String normalizedClassFQName = backwardToForwardSlash(testedClassFQName);
        boolean matched = match(testedClassPattern.match, normalizedClassFQName);
        if (!matched) debug(normalizedClassFQName + " does not match " + testedClassPattern.match);

        return matched;
    }

    public boolean matchTestClassPath(String testClassFQName) {
        String normalizedClassFQName = backwardToForwardSlash(testClassFQName);
        boolean matched = match(testClassPattern.match, normalizedClassFQName);
        if (!matched) debug(normalizedClassFQName + " does not match " + testClassPattern.match);

        return matched;
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public static String migrate(String pattern) {
        return pattern.replaceAll(DEPRECATED_PACKAGE_TOKEN, DIRECTORY_REGEX_TOKEN);
    }

    class Pattern {

        public Pattern(String pattern) {
            pattern = backwardToForwardSlash(pattern);
            this.display = pattern;
            this.match = createMatchPattern(pattern);
            this.replacement = createReplacementPattern(pattern);

        }

        private String createReplacementPattern(String pattern) {
            int group = 1;
            if (match(DIRECTORY_TOKEN_MATCH, pattern)) {
                pattern = substituteFirst(DIRECTORY_TOKEN_MATCH, group(group++), pattern);
            }
            return substituteFirst(CLASS_REGEXP_TOKEN, group(group), pattern);
        }

        private String createMatchPattern(String pattern) {
            return substituteFirst(DIRECTORY_TOKEN_MATCH, DIRECTORY, substituteFirst(CLASS_REGEXP_TOKEN, CLASS, pattern)) +
                    END_OF_CLASS;
        }

        public String toString() {
            return new ToStringBuilder(this).
                    append("display", display).
                    append("match", match).
                    append("replacement", replacement).
                    toString();
        }

        public String display;
        public String match;
        public String replacement;
    }

}