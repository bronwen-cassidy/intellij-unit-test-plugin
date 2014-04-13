/*
 * $Header: /cvsroot/junitideas/PluginUtil/src-demetra/org/intellij/openapi/plugins/adaptors/DemetraFileEditor.java,v 1.1 2006/03/02 19:40:53 shadow12 Exp ${}
 * $Revision: 1.1 ${}
 * $Date: 02-Mar-2006
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit.adaptor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.lang.StdLanguages;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jul-2005 14:45:45
 */
public class SelenaFileEditor implements FileEditorAdaptor {


   private Project project;
   private FileEditorManager fileEditorManager;

   public SelenaFileEditor(Project project, FileEditorManager fileEditorManager) {
      this.project = project;
      this.fileEditorManager = fileEditorManager;
   }

   public void openTextEditor(VirtualFile virtualFile, boolean focusEditor) {
      fileEditorManager.openTextEditor(new OpenFileDescriptor(project, virtualFile), focusEditor);
   }

   public Editor getSelectedTextEditor() {
      return fileEditorManager.getSelectedTextEditor();
   }

   public boolean isJavaFile(PsiFile psiFile) {
      if(psiFile == null) return false;
      if(!PsiJavaFile.class.isAssignableFrom(psiFile.getClass())) return false;
      if(!(psiFile.getLanguage().equals(StdLanguages.JAVA))) return false;
      return psiFile.getFileType().equals(StdFileTypes.JAVA);
   }
}