/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/adaptor/FileEditorAdaptor.java,v 1.5 2005/09/03 17:35:06 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/09/03 17:35:06 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.adaptor;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jul-2005 14:42:08
 */
public interface FileEditorAdaptor {

   void openTextEditor(VirtualFile virtualFile, boolean focusEditor);

   Editor getSelectedTextEditor();

   boolean isJavaFile(PsiFile psiFile);
}
