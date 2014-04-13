/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassPatternTable.java,v 1.2 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.util.LogUtil;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class ClassPatternTable extends JPanel {

   private static final Logger LOG = LogUtil.getLogger(ClassPatternTable.class);
   private JTable table;

   public ClassPatternTable() {
//      setLayout(new GridBagLayout());
      setBackground(Color.WHITE);
      setLayout(new BorderLayout(0, 0));

      String[][] properties = new String[][]{{"$CLASS$", "$CLASS$Test"}, {"$CLASS$", "$CLASS$TestCase"}};
      Object[][] tableData = new Object[properties.length][2];
      Object[] columnTitle = new String[]{"Tested class", "Test class"};

      for (int i = 0; i < properties.length; i++) {

         Object[] rowData = tableData[i];
         rowData[0] = properties[i][0];
         rowData[1] = properties[i][1];

      }
      add(createTable(tableData, columnTitle));
//      setVisible(true);
   }

   public JTable getTable() {
      return table;
   }

   private JScrollPane createTable(Object[][] tableData, Object[] columnTitle) {
      table = new JTable(tableData, columnTitle) {
         public boolean isCellEditable(int row, int column) {
            return true;
         }

//            public JToolTip createToolTip()
//            {
//                PropertySheetToolTip.getInstance().setComponent(this);
//                return PropertySheetToolTip.getInstance();
//            }

         public String getToolTipText(MouseEvent event) {
            int col = columnAtPoint(event.getPoint());
            int row = rowAtPoint(event.getPoint());

            String tip = (String) getValueAt(row, col);

            Graphics2D g2 = (Graphics2D) getGraphics();
            Rectangle2D tipRect = getFont().getStringBounds(tip, g2.getFontRenderContext());
            g2.dispose();

            Rectangle visibleCell = getVisibleRect().intersection(getCellRect(row, col, false));

            if (tipRect.getWidth() + 1 < visibleCell.getWidth()) {
               tip = null;   // Cell content is completely visible, so no tip is required
            }
            return tip;
         }

         private static final boolean INCLUDE_INTERCELL_SPACING = true;

         public Point getToolTipLocation(MouseEvent event) {
            int col = columnAtPoint(event.getPoint());
            int row = rowAtPoint(event.getPoint());
            return getCellRect(row, col, INCLUDE_INTERCELL_SPACING).getLocation();
         }
      };
//      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

//      packColumn(table, 0, 2);
//      packColumn(table, 1, 2);

      return new JScrollPane(table);
   }

   private void packColumn(JTable table, int colIndex, int margin) {
      int width = Math.max(getColumnHeaderWidth(table, colIndex),
                           getColumnCellWidth(table, colIndex));

      width += 2 * margin;

      final TableColumn column = table.getColumnModel().getColumn(colIndex);
      column.setPreferredWidth(width);
      column.setMaxWidth(width);
   }

   private int getColumnCellWidth(JTable table, int colIndex) {
      int width = 0;
      for (int r = 0; r < table.getRowCount(); r++) {
         TableCellRenderer renderer = table.getCellRenderer(r, colIndex);
         Component comp = renderer.getTableCellRendererComponent(table,
                                                                 table.getValueAt(r, colIndex),
                                                                 false,
                                                                 false,
                                                                 r,
                                                                 colIndex);
         width = Math.max(width, comp.getPreferredSize().width);
      }
      return width;
   }

   private int getColumnHeaderWidth(JTable table, int colIndex) {
      TableColumn col = table.getColumnModel().getColumn(colIndex);
      TableCellRenderer renderer = col.getHeaderRenderer();
      if (renderer == null) {
         renderer = table.getTableHeader().getDefaultRenderer();
      }
      Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
      return comp.getPreferredSize().width;
   }


   private static void debug(String message) {
      if (LOG.isDebugEnabled()) {
         LOG.debug(message);
      }
   }

   public static void main(String[] args) {
      JComponent c = new ClassPatternTable();
      JFrame w = new JFrame("test");
      w.setSize(400, 400);
      w.getContentPane().add(c);
      w.pack();
      w.show();
   }
}