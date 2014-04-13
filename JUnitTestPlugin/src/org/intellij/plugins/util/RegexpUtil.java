/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/RegexpUtil.java,v 1.7 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import org.apache.oro.text.PatternCache;
import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.perl.Perl5Util;

import java.util.regex.Pattern;

public class RegexpUtil {

    public static final String SLASH = "\\/";
    public static final String END_OF_STRING = "\\Z";
    public static final String DOT = "\\.";
    public static final String WORD_CHAR = "\\w";
    public static final String FILE_NAME_CHAR = "[a-zA-Z0-9_.%~-]";
    public static final String END_OF_WORD = "\\b";
    // Oro 1.2.8 should not espace next line
    public static final String BACKSLASH = "\\";

    public static String or(String s1, String s2) {
        return s1 + "|" + s2;
    }

    public static String group(String s) {
        return "(" + s + ")";
    }

    // Oro 1.2.8 should not espace $
    public static String group(int i) {
        return "\\$" + i;
    }

    public static String invisibleGroup(String s) {
        return "(?:" + s + ")";
    }

    public static String followedBy(String s) {
        return "(?=" + s + ")";
    }

    public static String zeroOrMore(String s) {
        return (isOneCharacterString(s) ? s : invisibleGroup(s)) + "*";
    }

    public static String zeroOrOne(String s) {
        return (isOneCharacterString(s) ? s : invisibleGroup(s)) + "?";
    }

    public static String substituteString(String match, String replacement) {
        return "s#" + match + "#" + replacement + "#";
    }

    public static String substituteString(String match, String replacement, char mode) {
        return "s#" + match + "#" + replacement + "#" + mode;
    }

    public static String matchString(String searchString) {
        return "#" + searchString + "#";
    }

    public static boolean isOneCharacterString(String s) {
        return s.length() == 1 || (s.length() == 2 && s.indexOf("\\") == 0);
    }

    public static String substituteFirst(String pattern, String replacement, String input) {
        Pattern regexpPattern = getPattern(pattern);
        return regexpPattern.matcher(input).replaceFirst(replacement);
    }

    private static String substituteAll(String pattern, String replacement, String input) {
        Pattern regexpPattern = getPattern(pattern);
        return regexpPattern.matcher(input).replaceAll(replacement);
    }

    private static Pattern getPattern(String pattern) {
        return Pattern.compile(pattern);
    }

    public static boolean match(String pattern, String input) {
        Pattern regexpPattern = getPattern(pattern);
        return regexpPattern.matcher(input).find();
    }

    public static String escapeString(String s) {
        return substituteAll("(\\\\|\\$)", "\\\\$1", s);

    }

    public static String backwardToForwardSlash(String s) {
        if (s == null) return "";
        return substituteAll(BACKSLASH + BACKSLASH, "/", s);
    }

    public static String forwardToBackwardSlash(String s) {
        return substituteAll("/", BACKSLASH + BACKSLASH, s);
    }

    public static Perl5Util getUtil() {
        return new Perl5Util(cache);
    }

    static PatternCache cache = new PatternCacheLRU(1000);

}
