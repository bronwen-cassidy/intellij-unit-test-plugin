package org.intellij.plugins.ui.common;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RightClickSelectableTable extends JTable {

    public RightClickSelectableTable() {
        this(new DefaultTableModel());
    }

    public RightClickSelectableTable(TableModel tablemodel) {
        super(tablemodel);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                RightClickSelectableTable.this.mousePressed(event);
            }
        });
    }

    private void mousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            int[] selectedRows = getSelectedRows();
            if (selectedRows.length < 2) {
                int i = rowAtPoint(event.getPoint());
                if (i != -1) {
                    getSelectionModel().setSelectionInterval(i, i);
                }
            }
        }
    }
}
