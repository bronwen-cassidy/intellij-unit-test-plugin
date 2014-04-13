/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/EditableListTableModel.java,v 1.2 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class EditableListTableModel extends AbstractTableModel {
   private ColumnDesc[] columns;
   private Class rowClass;

   public static class ColumnDesc {
      public String name;
      public String property;
      public boolean editable;

      public ColumnDesc(String name, String property, boolean editable) {
         this.name = name;
         this.property = property;
         this.editable = editable;
      }

      public ColumnDesc(String name) {
         this(name, null, false);
      }
   }

   private EditableListPanel panel;

   public EditableListTableModel(EditableListPanel panel, String[] columnNames) {
      this(panel, null, createColumnDescsFromColumnNames(columnNames));
   }

   private static ColumnDesc[] createColumnDescsFromColumnNames(String[] names) {
      ColumnDesc[] descs = new ColumnDesc[names.length];
      for (int i = 0; i < descs.length; i++) {
         descs[i] = new ColumnDesc(names[i]);
      }
      return descs;
   }

   public EditableListTableModel(EditableListPanel panel, Class rowClass, ColumnDesc[] columns) {
      this.panel = panel;
      this.rowClass = rowClass;
      this.columns = columns;
   }

   public int getColumnCount() {
      return columns.length;
   }

   public String getColumnName(int columnIndex) {
      return columns[columnIndex].name;
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      return new BeanWrapperImpl(getRows().get(rowIndex)).getPropertyValue(columns[columnIndex].property);
   }

   public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      PropertyValue propValue = new PropertyValue(columns[columnIndex].property, aValue);
      new BeanWrapperImpl(getRows().get(rowIndex)).setPropertyValue(propValue);
   }

   public Class getColumnClass(int columnIndex) {
      return new BeanWrapperImpl(rowClass).getPropertyType(columns[columnIndex].property);
   }

   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return columns[columnIndex].editable;
   }

   public int getRowCount() {
      return getRows().size();
   }

   protected ArrayList getRows() {
      return panel.getItems();
   }


}