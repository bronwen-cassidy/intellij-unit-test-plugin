/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/EditorHelper.java,v 1.3 2005/08/20 09:54:26 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/20 09:54:26 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiIdentifier;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.util.PluginPsiUtil;
import org.intellij.plugins.junit.adaptor.FileEditorAdaptor;

public class EditorHelper {
   
   private FileEditorAdaptor fileEditorAdaptor;
   private PluginPsiUtil pluginPsiUtil;

   public EditorHelper(FileEditorAdaptor fileEditorAdaptor, PluginPsiUtil pluginPsiUtil) {
      this.fileEditorAdaptor = fileEditorAdaptor;
      this.pluginPsiUtil = pluginPsiUtil;
   }

   public void openFile(PsiElement element, int offset) {
      openFile(element);
      moveCaretTo(element, offset);
   }

   private void openFile(PsiElement element) {
      PsiFile containingFile = element.getContainingFile();
      debug("openFile for " + element + " of " + containingFile);
      fileEditorAdaptor.openTextEditor(containingFile.getVirtualFile(), true);
   }

   public void moveCaretTo(PsiElement element, int offset) {
      PsiFile containingFile = element.getContainingFile();
      Editor editor = fileEditorAdaptor.getSelectedTextEditor();
      PsiElement elementAtCaret = pluginPsiUtil.getElementAtCaret(containingFile, editor);
      if (!pluginPsiUtil.isAncestor(elementAtCaret, element)) {
         editor.getCaretModel().moveToOffset(offset);
         editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
      }
   }

   public static int getLeftBraceOffset(PsiMethod method) {

       final PsiCodeBlock body = method.getBody();
       if(body == null) return NON_EXISTANT;
       final PsiJavaToken javaToken = body.getLBrace();
       if(javaToken == null) return NON_EXISTANT;
       final TextRange textRange = javaToken.getTextRange();
       return textRange.getEndOffset();
   }

   public static int getEndOfIdentifierOffset(PsiMethod method) {

       final PsiIdentifier identifier = method.getNameIdentifier();
       if(identifier == null) return NON_EXISTANT;
       return identifier.getTextRange().getEndOffset();
   }

   protected void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

   protected static final Logger LOG = LogUtil.getLogger();

    private static final int NON_EXISTANT = -1;
}