/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/openapi/OpenApiFacade.java,v 1.15 2006/02/27 17:03:27 shadow12 Exp $
 * $Revision: 1.15 $
 * $Date: 2006/02/27 17:03:27 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.listeners.RefactoringListenerManager;

public class OpenApiFacade {

    public static ActionManager actionManager;
    public static PsiManager psiManager;
    public static VirtualFileManager virtualFileManager;
    public static RefactoringListenerManager refactoringListenerManager;
    public static PsiDocumentManager psiDocumentManager;

    private static void checkProject(Project project) {
        if (project == null) throw new NullPointerException("project null");
    }

    public static ActionManager getActionManager() {
        try {
            return ActionManager.getInstance();
        } catch (NullPointerException e) {
            return actionManager;
        }
    }

    public static PsiManager getPsiManager(Project project) {
        if (psiManager != null) {
            checkProject(project);
            return psiManager;
        }
        return PsiManager.getInstance(project);
    }

    public static VirtualFileManager getVirtualFileManager() {
        if (virtualFileManager != null) {
            return virtualFileManager;
        }
        return VirtualFileManager.getInstance();

    }

    public static RefactoringListenerManager getRefactoringListenerManager(Project project) {
        if (refactoringListenerManager != null) {
            return refactoringListenerManager;
        }
        return RefactoringListenerManager.getInstance(project);
    }

    public static FileEditorManager getFileEditorManager(Project project) {
        return FileEditorManager.getInstance(project);
    }

    public static PsiDocumentManager getPsiDocumentManager(Project project) {
        if (psiDocumentManager != null) {
            return OpenApiFacade.psiDocumentManager;
        }
        return PsiDocumentManager.getInstance(project);
    }

    public static Document getDocument(Project project) {
        final Editor textEditor = getFileEditorManager(project).getSelectedTextEditor();
        return textEditor != null ? textEditor.getDocument() : null;
    }
}
