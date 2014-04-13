/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/openapi/DataContextUtil.java,v 1.6 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class DataContextUtil {

    public static final String PSI_ELEMENT = "psi.Element";
    public static final String PSI_FILE = "psi.File";
    public static final String PROJECT = "project";
    public static final String EDITOR = "editor";
    public static final String VIRTUAL_FILE = "virtualFile";
    public static final String VIRTUAL_FILE_ARRAY = "virtualFileArray";

    public static Project getProject(AnActionEvent event) {
        return (Project) event.getDataContext().getData(PROJECT);
    }

    public static PsiElement getPsiElement(AnActionEvent event) {
        return (PsiElement) event.getDataContext().getData(PSI_ELEMENT);
    }

    public static Editor getEditor(AnActionEvent event) {
        return (Editor) event.getDataContext().getData(EDITOR);
    }

    public static PsiFile getPsiFile(AnActionEvent event) {
        return (PsiFile) event.getDataContext().getData(PSI_FILE);
    }

    public static VirtualFile getVirtualFile(AnActionEvent event) {
        return (VirtualFile) event.getDataContext().getData(VIRTUAL_FILE);
    }

    public static VirtualFile[] getVirtualFileArray(AnActionEvent event) {
        VirtualFile[] virtualFiles = (VirtualFile[]) event.getDataContext().getData(VIRTUAL_FILE_ARRAY);
        if (virtualFiles == null || virtualFiles.length == 0) {
            VirtualFile virtualFile = getVirtualFile(event);
            if (virtualFile != null) {
                virtualFiles = new VirtualFile[]{virtualFile};
            }
        }
        return virtualFiles;
    }
}
