/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/JUnitHelper.java,v 1.3 2005/08/27 20:26:53 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/27 20:26:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.util.PluginPsiUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class JUnitHelper {

    protected static final Logger LOG = LogUtil.getLogger();
    public static final String TEST_METHOD_PREFIX = "test";

    public static boolean checkProjectCorrectlySetup(Project project) {
        if (!isJUnitInClassPath(project)) {
            Messages.showMessageDialog(project,
                    "JUnit is not in the class path. Please add it first",
                    "JUnit Test Plugin Prerequisite Not Met",
                    Messages.getErrorIcon());
            return false;
        }
        return true;
    }

    private static boolean isJUnitInClassPath(Project project) {
        return getTestCasePsiClass(project, null) != null;
    }

    private static PsiClass getTestCasePsiClass(Project project, PsiClass aClass) {
        if (project == null) {
            LOG.debug("Project NULL");
            return null;
        }
        Map map = new TreeMap();
        map.put("4all", GlobalSearchScope.allScope(project));
        map.put("3project", GlobalSearchScope.projectScope(project));
        if (aClass != null) {
            map.put("2resolve", aClass.getResolveScope());
        }

        for (Iterator it = map.keySet().iterator(); it.hasNext();) {
            String scopeName = (String) it.next();
            GlobalSearchScope scope = (GlobalSearchScope) map.get(scopeName);
            //PsiClass result = PsiManager.getInstance(project).findClass("junit.framework.TestCase", scope);
            final PsiClass result = JavaPsiFacade.getInstance(project).findClass("junit.framework.TestCase", scope);
            if (result != null) {
                LOG.debug("Found TestCase with " + scopeName.substring(1) + " scope");
                return result;
            }
        }
        return null;
    }

    public static boolean isUnitTest(PsiClass aClass) {
        if (aClass == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(new Exception("isUnitTest(): class null"));
            }
            return false;
        }
        if (aClass.getManager() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(new Exception("isUnitTest(): class manager null"));
            }
            return false;
        }
        PsiClass junitTestCaseClass = getTestCasePsiClass(aClass.getManager().getProject(), aClass);
        if (junitTestCaseClass == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(new Exception("Could not find junit.framework.TestCase"));
            }
            return false;
        }
        return PluginPsiUtil.isInheritor(aClass, junitTestCaseClass);
    }
}