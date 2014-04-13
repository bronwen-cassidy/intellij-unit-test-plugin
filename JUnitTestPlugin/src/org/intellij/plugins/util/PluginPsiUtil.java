/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/PluginPsiUtil.java,v 1.23 2006/02/27 17:03:28 shadow12 Exp $
 * $Revision: 1.23 $
 * $Date: 2006/02/27 17:03:28 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.intellij.plugins.util.DataContextUtil;
import org.intellij.plugins.util.OpenApiFacade;

public class PluginPsiUtil {

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.util.PluginPsiUtil");

    private PsiClassManager psiClassManager;
    private FileEditorManager fileEditorManager;

    public PluginPsiUtil(PsiClassManager psiClassManager, FileEditorManager fileEditorManager) {
        this.psiClassManager = psiClassManager;
        this.fileEditorManager = fileEditorManager;
    }


    public PsiClass findTopLevelClassOfEnclosingFile(final PsiElement element) {
        PsiClass psiClass;
        psiClass = findTopLevelEnclosingClass(element);
        if (psiClass == null) {
            final PsiFile containingFile = element.getContainingFile();
            if (containingFile != null) {
                final VirtualFile virtualFile = containingFile.getVirtualFile();
                if (virtualFile != null) {
                    psiClassManager.findPrimaryClass(virtualFile.getPath());
                }
            }
        }
        return psiClass;
    }

    public static void logPsiElementHierarchy(String context,
                                              PsiElement element) {
        if (element == null) {
            debug("element=null");
            return;
        }
        PsiFile containingFile = element.getContainingFile();
        debug(context + ": elt=" + element + (containingFile == null ? "" : (" containingFile=" + containingFile)));
        context = context.replaceAll(".", " ");
        logPsiElementHierarchy(context, element.getParent());
    }

    public PsiClass findTopLevelEnclosingClass(final PsiElement element) {
        PsiClass psiClass;
        if (element instanceof PsiClass) {
            psiClass = (PsiClass) element;
        } else {
            psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        }
        if (psiClass instanceof PsiAnonymousClass) {
            psiClass = findTopLevelEnclosingClass(psiClass.getParent());
        }
        return psiClass;
    }

    public PsiMethod findTopLevelClassMethod(PsiElement element) {
        PsiMethod method = ((element instanceof PsiMethod) ?
                (PsiMethod) element : PsiTreeUtil.getParentOfType(element, PsiMethod.class));
        if (method != null && method.getContainingClass() instanceof PsiAnonymousClass) {
            return findTopLevelClassMethod(method.getParent());
        }
        return method;
    }

    public PsiElement getCurrentElement(AnActionEvent event) {
        Editor editor = DataContextUtil.getEditor(event);
        PsiFile psiFile = DataContextUtil.getPsiFile(event);
        if (psiFile != null && editor != null) {
            return getElementAtCaret(psiFile, editor);
        } else {
            return DataContextUtil.getPsiElement(event);
        }
    }

    public PsiElement getElementAtCaret(Project project) {
        Editor selectedEditor = OpenApiFacade.getFileEditorManager(project).getSelectedTextEditor();
        PsiFile psiFile = getSelectedPsiFile(project);
        return getElementAtCaret(psiFile, selectedEditor);
    }

    public PsiElement getElementAtCaretInFile(PsiElement element) {
        PsiFile file = element.getContainingFile();
        Editor editor = getTextEditor(file);
        return getElementAtCaret(file, editor);
    }

    public Editor getTextEditor(PsiFile file) {
        if (file == null) return null;
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return null;
        FileEditor[] editors = fileEditorManager.getEditors(virtualFile);

        for (int i = 0; i < editors.length; i++) {
            if (editors[i] instanceof TextEditor) {
                return ((TextEditor) editors[i]).getEditor();
            }
        }
        return null;
    }

    public PsiFile getSelectedPsiFile(Project project) {
        Document selectedDocument = getSelectedDocument(project);
        if (selectedDocument == null) return null;
        return PsiDocumentManager.getInstance(project).getPsiFile(selectedDocument);
    }

    private Document getSelectedDocument(Project project) {
        return OpenApiFacade.getDocument(project);
    }

    public PsiElement getElementAtCaret(PsiFile psiFile, Editor editor) {
        return psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    public String getFilePath(PsiClass c) {
        PsiFile classFile = (PsiFile) findTopLevelEnclosingClass(c).getParent();
        final VirtualFile file = classFile.getVirtualFile();
        return file != null ? file.getPath() : null;
    }

    // used to extract the directory from the src roots for the select class
    public String getResolvedFilePath(PsiClass c) {
        return resolveClassPath(getFilePath(c));
    }

    // DEBT to think about perhaps this class needs to be an adaptor?
    public String resolveClassPath(String path) {
        ProjectRootManager projectRootManager = ProjectRootManager.getInstance(psiClassManager.project);
        if (projectRootManager != null) {
            VirtualFile[] contentSourceRoots = projectRootManager.getContentSourceRoots();
            for (int i = 0; i < contentSourceRoots.length; i++) {
                VirtualFile contentSourceRoot = contentSourceRoots[i];
                String name = contentSourceRoot.getName();
                String sourceRootPath = contentSourceRoot.getPath();
                if (path.indexOf(sourceRootPath) != -1 && path.indexOf(name) != -1) {
                    return path.substring(sourceRootPath.length() - name.length());
                }
            }
        }
        return null;
    }

    /**
     * Is the given element under the parent, ancestor element.
     *
     * @param element  the child or sub-element (i.e a method is a sub-element of the class)
     * @param ancestor the parent or enclosing element
     * @return true if the element is a sub element, false if the element is null or not a sub element.
     */
    public boolean isAncestor(PsiElement element, PsiElement ancestor) {
        return element != null && PsiTreeUtil.isAncestor(ancestor, element, false);
    }


    public boolean isFileWritable(PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file != null && !file.isWritable()) {
            OpenApiFacade.getVirtualFileManager().fireReadOnlyModificationAttempt(file);
            if (!file.isWritable()) {
                Messages.showMessageDialog("Cannot modify a read-only file " + file.getPath(),
                        "File Read-only",
                        Messages.getErrorIcon());
                return false;
            }
        }
        return true;
    }

    public static PsiDirectory getClassDirectory(PsiClass theClass) {
        return theClass.getContainingFile().getContainingDirectory();
    }

    private static void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public static boolean isInheritor(PsiClass childClass,
                                      PsiClass parentClass) {

        //PsiClass objectClass = childClass.getManager().findClass("java.lang.Object", childClass.getResolveScope());
        PsiClass objectClass = JavaPsiFacade.getInstance(childClass.getProject()).findClass("java.lang.Object", childClass.getResolveScope());
        while (!childClass.equals(objectClass)) {
            if (childClass.getSuperClass() == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("isInheritor(" + childClass.getName() + ") superclass is null");
                }
                return false;
            } else if (parentClass.equals(childClass.getSuperClass())) {
                return true;
            }
            childClass = childClass.getSuperClass();
            return isInheritor(childClass, parentClass);
        }
        return false;
    }

}
