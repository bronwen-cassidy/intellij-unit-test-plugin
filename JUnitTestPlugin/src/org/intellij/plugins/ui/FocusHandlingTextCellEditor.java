/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/FocusHandlingTextCellEditor.java,v 1.2 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.EventObject;

public class FocusHandlingTextCellEditor extends AbstractCellEditor implements TableCellEditor {

    protected EditorHandler handler;
    private JTextField textField;
    private EscapeKeyEventDispatcher dispatcher;

    public FocusHandlingTextCellEditor() {
        handler = new StringEditorHandler(this);
        textField = new JTextField();
        textField.addActionListener(handler);
        textField.addFocusListener(handler);
        textField.addKeyListener(handler);
        dispatcher = new EscapeKeyEventDispatcher(handler);
    }

    public JTextField getTextField() {
        return textField;
    }

    public Object getCellEditorValue() {
        return handler.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject eventobject) {
        return handler.isCellEditable(eventobject);
    }

    public boolean shouldSelectCell(EventObject eventobject) {
        return handler.shouldSelectCell(eventobject);
    }

    public boolean stopCellEditing() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
        return handler.stopCellEditing();
    }

    public void cancelCellEditing() {
        handler.cancelCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        handler.setEditorObjectValue(value);
//      getTextField().setBackground(table.getSelectionBackground());
        textField.setMargin(new Insets(0, 0, 0, 0));
        textField.setBorder(new LineBorder(Color.BLACK));
        textField.setBackground(Color.WHITE);
        textField.setOpaque(true);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
        return textField;
    }

    static void fireEditingStopped(FocusHandlingTextCellEditor editor) {
        editor.fireEditingStopped();
    }

    static void fireEditingCanceled(FocusHandlingTextCellEditor editor) {
        editor.fireEditingCanceled();
    }

    private class EscapeKeyEventDispatcher implements KeyEventDispatcher {

        EditorHandler handler;

        public EscapeKeyEventDispatcher(EditorHandler handler) {
            this.handler = handler;
        }

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                handler.cancelCellEditing();
                return true;
            }
            return false;
        }
    }
}