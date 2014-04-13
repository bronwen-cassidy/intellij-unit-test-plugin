/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/actions/ActionHandler.java,v 1.4 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.intellij.plugins.util.DataContextUtil;
import org.intellij.plugins.util.LogUtil;

import java.awt.event.InputEvent;


public abstract class ActionHandler {

    protected AnActionEvent event;
    protected static final Logger LOG = LogUtil.getLogger();

    public abstract void update();

    public abstract void actionPerformed();

    public void setEvent(AnActionEvent event) {
        this.event = event;
    }

    public AnActionEvent getEvent() {
        return event;
    }

    public String getPlace() {
        return event.getPlace();
    }

    public InputEvent getInputEvent() {
        return event.getInputEvent();
    }

    public DataContext getDataContext() {
        return event.getDataContext();
    }

    public int getModifiers() {
        return event.getModifiers();
    }

    public Presentation getPresentation() {
        return event.getPresentation();
    }

    public Project getProject() {
        return DataContextUtil.getProject(event);
    }

    public Editor getEditor() {
        return DataContextUtil.getEditor(event);
    }

    public VirtualFile getVirtualFile() {
        return DataContextUtil.getVirtualFile(event);
    }

    public VirtualFile[] getVirtualFileArray() {
        return DataContextUtil.getVirtualFileArray(event);
    }

    public PsiElement getPsiElement() {
        return DataContextUtil.getPsiElement(event);
    }

    public PsiFile getPsiFile() {
        return DataContextUtil.getPsiFile(event);
    }

    protected static void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }
}
