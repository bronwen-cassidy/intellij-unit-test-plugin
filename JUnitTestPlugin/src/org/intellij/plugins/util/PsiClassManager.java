/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/PsiClassManager.java,v 1.27 2005/10/19 08:19:05 shadow12 Exp $
 * $Revision: 1.27 $
 * $Date: 2005/10/19 08:19:05 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;

public class PsiClassManager {

    Project project;
    PsiDirectoryUtil psiDirectoryUtil;
    TemplateEngine templateEngine;
    PsiManager psiManager;
    CodeStyleManager codeStyleManager;
    LocalFileSystem localFileSystem;

    public PsiClassManager(Project project,
                           PsiManager psiManager,
                           PsiDirectoryUtil psiDirectoryUtil,
                           CodeStyleManager codeStyleManager,
                           LocalFileSystem localFileSystem,
                           TemplateEngine templateEngine) {
        this.project = project;
        this.templateEngine = templateEngine;
        this.psiManager = psiManager;
        this.psiDirectoryUtil = psiDirectoryUtil;
        this.localFileSystem = localFileSystem;
        this.codeStyleManager = codeStyleManager;
    }

    public PsiClass findPrimaryClass(String filePath) {
        debug("findPrimaryClass filePath=" + filePath);
        if (filePath == null) return null;
        VirtualFile vFile = localFileSystem.findFileByPath(filePath);
        if (vFile == null) return null;
        PsiFile psiFile = psiManager.findFile(vFile);
        return findPrimaryClass(psiFile);
    }

    public PsiClass findPrimaryClass(PsiFile psiFile) {
        if (psiFile == null || !(psiFile instanceof PsiJavaFile)) return null;
        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        PsiClass[] classes = javaFile.getClasses();
        String filePrimaryClassName = getPrimaryClassNameFromJavaFileName(javaFile.getName());
        for (int i = 0; i < classes.length; i++) {
            PsiClass aClass = classes[i];
            if (filePrimaryClassName.equals(aClass.getName())) {
                return aClass;
            }
        }
        return null;
    }

    public static String getPrimaryClassNameFromJavaFileName(String name) {
        return name.substring(0, name.length() - ".java".length());
    }

    public PsiClass createClass(String parentPath, String classSource, boolean reformat)
            throws IncorrectOperationException {
        PsiDirectory parentDir = psiDirectoryUtil.findOrCreateDirectory(parentPath);
        return createClass(parentDir, classSource, reformat);
    }

    public PsiClass createClass(PsiDirectory parentDir, String classSource, boolean reformat) throws IncorrectOperationException {

        String JAVA_FILE_EXTENSION = "java";
        final PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("myclass." + JAVA_FILE_EXTENSION, classSource);
        PsiJavaFile file = (PsiJavaFile) psiFile;
        PsiClass[] classes = file.getClasses();
//        if (classes.length == 0) {
//            throw new IncorrectOperationException("This source did not produce Java class nor interface!");
//        }
        PsiClass psiClass = classes[0];
        if (reformat) {
            codeStyleManager.reformat(file);
        }
        String className = psiClass.getName();
        if(className == null) throw new IncorrectOperationException("className cannot be null");

        String fileName = className + "." + JAVA_FILE_EXTENSION;
        parentDir.checkCreateFile(className);        
        /*if (psiClass.isInterface()) {
            parentDir.checkCreateInterface(className);
        } else {
            parentDir.checkCreateClass(className);
        }*/
        file = (PsiJavaFile) file.setName(fileName);
        file = (PsiJavaFile) parentDir.add(file);
        return file.getClasses()[0];
    }

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public PsiManager getPsiManager() {
        return psiManager;
    }

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.util.PsiClassManager");

}
