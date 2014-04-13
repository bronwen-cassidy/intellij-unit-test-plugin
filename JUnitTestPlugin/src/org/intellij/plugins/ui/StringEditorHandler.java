/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/StringEditorHandler.java,v 1.4 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;


class StringEditorHandler extends EditorHandler {

   StringEditorHandler(FocusHandlingTextCellEditor editor) {
      super(editor);
   }

   public void setEditorObjectValue(Object obj) {
      editor.getTextField().setText(obj == null ? "" : obj.toString());
   }

   public Object getCellEditorValue() {
      return editor.getTextField().getText();
   }

}
