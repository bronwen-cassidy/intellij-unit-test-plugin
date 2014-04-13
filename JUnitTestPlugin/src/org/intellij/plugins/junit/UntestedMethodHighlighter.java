/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/UntestedMethodHighlighter.java,v 1.37 2005/09/24 09:20:13 shadow12 Exp $
 * $Revision: 1.37 $
 * $Date: 2005/09/24 09:20:13 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.intellij.plugins.util.OpenApiFacade;
import org.intellij.plugins.junit.actions.GoToTestCaseAction;
import org.intellij.plugins.util.CommandUtil;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.junit.adaptor.FileEditorAdaptor;
import org.intellij.plugins.junit.adaptor.IdeaFacade;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

public class UntestedMethodHighlighter extends JUnitTestPluginActivity implements FileEditorManagerListener {

    private static final Logger LOG = LogUtil.getLogger();

    private ClassUntestedMethodsLocator classUntestedMethodsLocator;
    private CommandUtil commandUtil;
    private PsiTreeChangeListener psiTreeChangeListener;
    private Editor selectedEditor;
    private Timer timer;
    private FileEditorAdaptor fileEditorAdaptor;

    public UntestedMethodHighlighter(Project project,
                                     ClassLocator classLocator,
                                     ClassUntestedMethodsLocator classUntestedMethodsLocator,
                                     MethodLocator methodLocator,
                                     IdeaFacade ideaFacade,
                                     CommandUtil commandUtil,
                                     FileEditorAdaptor fileEditorAdaptor) {

        super(project, classLocator, methodLocator, ideaFacade);
        this.classUntestedMethodsLocator = classUntestedMethodsLocator;
        this.commandUtil = commandUtil;
        this.fileEditorAdaptor = fileEditorAdaptor;
    }

    public void updateTreeFromPsiTreeChange(PsiTreeChangeEvent event) {
        updateEditorElementAddedOrRemoved(event);
    }

    private void updateEditorElementAddedOrRemoved(final PsiTreeChangeEvent event) {
        PsiFile file = event.getFile();
        if (selectedEditor != null && isJavaFile(file)) {
            debug(">>>>>>>>>>>> Untested Method Highlighter line 71 getting the element at caret!! ");

            PsiElement elementAtCaret = file.findElementAt(selectedEditor.getCaretModel().getOffset());
            debug(">>>>>>>>>>>> Untested Method Highlighter line 74 fond the element at caret!! ");
            if (elementAtCaret != null) {
                startTimer();
            }
        }
    }

    public void selectionChanged(FileEditorManagerEvent event) {
        selectedEditor = event.getManager().getSelectedTextEditor();
        if (selectedEditor != null && started) {
            markUntestedMethodsInEditor(selectedEditor);
        } else {
            removeUntestedMethodHighlighters(selectedEditor);
        }
    }

    public void fileOpened(FileEditorManager source, VirtualFile file) {
    }

    public void fileClosed(FileEditorManager source, VirtualFile file) {
    }

    public void markUntestedMethodsInEditor(Editor editor) {
        if (editor == null) return;
        PsiFile file = OpenApiFacade.getPsiDocumentManager(project).getPsiFile(editor.getDocument());

        if (!(isJavaFile(file))) return;

        PsiJavaFile psiJavaFile = (PsiJavaFile) file;

        PsiClass[] psiClasses = psiJavaFile.getClasses();
        for (int i = 0; i < psiClasses.length; i++) {
            PsiClass psiClass = psiClasses[i];
            if (isTestable(psiClass)) {
                markUntestedMethods(psiClass, editor);
            }
        }
    }

    private boolean isTestable(PsiClass psiClass) {
        return (classLocator.isClassTestable(psiClass) && !isTestClass(psiClass));
    }

    private boolean isJavaFile(PsiFile psiJavaFile) {
        return fileEditorAdaptor.isJavaFile(psiJavaFile);
    }

    public void markUntestedMethods(final PsiClass testedClass,
                                    final Editor editor) {
        try {
            commandUtil.runOnMainThreadAsynchonously(new Runnable() {
                public void run() {
                    doMarkUntestedMethods(testedClass, editor);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addGutterIcon(RangeHighlighter rangeHighlighter, final PsiMethod method) {
        rangeHighlighter.setGutterIconRenderer(new GutterIconRenderer() {
            public Icon getIcon() {
                return Icons.METHOD_NOT_TESTED;
            }

            public String getTooltipText() {
                return "Create JUnit Test for method: " + method.getName();
            }

            public boolean isNavigateAction() {
                return true;
            }

            public AnAction getClickAction() {
                return new GoToTestCaseAction(method);
            }
        });
    }

    protected void register() {
        OpenApiFacade.getFileEditorManager(project).addFileEditorManagerListener(this);
        initPsiTreeListener();
    }

    public void mark() {
        Editor[] allEditors = getEditors();
        for (int i = 0; i < allEditors.length; i++) {
            Editor editor = allEditors[i];
            markUntestedMethodsInEditor(editor);
        }
    }

    protected void unregister() {
        OpenApiFacade.getFileEditorManager(project).removeFileEditorManagerListener(this);
        OpenApiFacade.getPsiManager(project).removePsiTreeChangeListener(psiTreeChangeListener);
    }

    public void unmark() {
        Editor[] allEditors = getEditors();
        for (int i = 0; i < allEditors.length; i++) {
            removeUntestedMethodHighlighters(allEditors[i]);
        }
    }

    private Editor[] getEditors() {
        return EditorFactory.getInstance().getAllEditors();
    }

    protected boolean isTestClass(PsiClass psiClass) {
        return JUnitHelper.isUnitTest(psiClass);
    }

    protected void markMethod(PsiMethod untestedMethod, Document document) {
        int line = getMethodStartLineNumber(untestedMethod, document);
        MarkupModel markupModel = document.getMarkupModel(project);
        RangeHighlighter rangeHighlighter = markupModel.addLineHighlighter(line, HighlighterLayer.FIRST, null);
        addGutterIcon(rangeHighlighter, untestedMethod);
    }

    private void initPsiTreeListener() {
        LOG.debug("PsiTreeChangeAdapter creation");
        psiTreeChangeListener = new PsiTreeChangeAdapter() {

            public void childrenChanged(PsiTreeChangeEvent event) {
                updateTreeFromPsiTreeChange(event);
            }

            public void childReplaced(PsiTreeChangeEvent event) {
                updateTreeFromPsiTreeChange(event);
            }
        };
        PsiManager manager = OpenApiFacade.getPsiManager(project);
        manager.addPsiTreeChangeListener(psiTreeChangeListener);
    }

    private void doMarkUntestedMethods(PsiClass testedClass, Editor editor) {
        Set untestedMethods = classUntestedMethodsLocator.getUntestedMethods(testedClass);
        removeUntestedMethodHighlighters(editor);
        for (Iterator iterator = untestedMethods.iterator(); iterator.hasNext();) {
            MethodDef untestedMethod = (MethodDef) iterator.next();
            markMethod(untestedMethod.getPsiMethod(), editor.getDocument());
        }
    }

    private void removeUntestedMethodHighlighters(Editor editor) {
        if (editor == null) return;
        MarkupModel markupModel = editor.getDocument().getMarkupModel(project);
        RangeHighlighter[] rangeHighlighters = markupModel.getAllHighlighters();
        for (int i = 0; i < rangeHighlighters.length; i++) {
            RangeHighlighter rangeHighlighter = rangeHighlighters[i];
            if (isMethodUntestedHighlighter(rangeHighlighter)) {
                markupModel.removeHighlighter(rangeHighlighter);
            }
        }
    }

    private boolean isMethodUntestedHighlighter(RangeHighlighter rangeHighlighter) {
        return rangeHighlighter.getGutterIconRenderer() != null
                &&
                rangeHighlighter.getGutterIconRenderer().getIcon() != null
                && rangeHighlighter.getGutterIconRenderer().getIcon().equals(Icons.METHOD_NOT_TESTED);
    }

    private int getMethodStartLineNumber(PsiMethod method, Document document) {
        int offset = method.getNameIdentifier().getTextOffset();
        return document.getLineNumber(offset);
    }

    private void startTimer() {
        stopTimer();
        debug(">>>>>>>>>>>> Untested Method Highlighter starting the timer!! line 245 ");
        timer = new Timer(4000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                debug(">>>>>>>>>>>>>>>>>>>> Started marking untested methods");
                stopTimer();
                PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
                if (documentManager.hasUncommitedDocuments()) {
                    documentManager.commitAllDocuments();
                }
                markUntestedMethodsInEditor(selectedEditor);
                debug(">>>>>>>>>>>>>>>>> Finished marking untested methods");
            }
        });
        timer.setRepeats(false);
        timer.setCoalesce(true);
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void debug(String message) {
        //if (LOG.isDebugEnabled()) {
        LOG.debug(message);
        //}
    }
}
