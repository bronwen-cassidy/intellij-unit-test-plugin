/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/EditorHandler.java,v 1.4 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.EventObject;

public class EditorHandler implements ActionListener, ItemListener, FocusListener, KeyListener, Serializable {

   protected EditorHandler(FocusHandlingTextCellEditor editor) {
      this.editor = editor;
   }

   public Object getCellEditorValue() {
      return value;
   }

   public void setEditorObjectValue(Object obj) {
      value = obj;
   }

   public boolean isCellEditable(EventObject eventobject) {
      return true;
   }

   public boolean shouldSelectCell(EventObject eventobject) {
      return true;
   }

   public boolean stopCellEditing() {
      FocusHandlingTextCellEditor.fireEditingStopped(editor);
      return true;
   }

   public void cancelCellEditing() {
      FocusHandlingTextCellEditor.fireEditingCanceled(editor);
   }

   public void actionPerformed(ActionEvent actionevent) {
      editor.stopCellEditing();
   }

   public void itemStateChanged(ItemEvent itemevent) {
      editor.stopCellEditing();
   }

   public void focusGained(FocusEvent e) {
   }

   public void focusLost(FocusEvent e) {
      editor.stopCellEditing();
   }

   public void keyPressed(KeyEvent e) { }

   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
         editor.cancelCellEditing();
         e.consume();
      }
   }

   public void keyTyped(KeyEvent e) { }

   protected Object value;
   protected FocusHandlingTextCellEditor editor;
}
