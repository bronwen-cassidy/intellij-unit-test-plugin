package org.intellij.plugins.ui;

import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.junit.config.ListConfig;
import org.intellij.plugins.ui.common.RightClickSelectableTable;
import org.intellij.plugins.ui.common.SimpleScrollPane;
import org.intellij.plugins.junit.Validator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class EditableListPanel extends JPanel {

    protected ListConfig configuration;
    private String title;
    protected RightClickSelectableTable table;
    protected ArrayList/*<T>*/ items = new ArrayList/*<T>*/();
    private static final Logger LOG = Logger.getInstance(EditableListPanel.class.getName());
    private JLabel errorLabel;

    public EditableListPanel(ListConfig configuration) {
        this.configuration = configuration;
    }

    public EditableListPanel(String title) {
        this.title = title;
    }

    public EditableListPanel() {
    }

    protected void initUI() {
        setLayout(new GridBagLayout());
        JComponent listPanel = getListPanel();
        JButton[] buttons = getButtons();
        if (title != null) {
            add(new JLabel(title), new GridBagConstraints(0, 2, 2, 1, 1.0D, 0.0D, 17, 2, new Insets(0, 0, 4, 0), 0, 0));
        }
        add(listPanel, new GridBagConstraints(0, 3, 1, buttons.length, 1.0D, 1.0D, 17, 1, new Insets(0, 0, 0, 4), 0, 0));
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            add(button,
                    new GridBagConstraints(1, 3 + i,
                            1, 1,
                            0.0D, i != buttons.length - 1 ? 0 : 1,
                            11,
                            2,
                            new Insets(0, 0, 4, 0),
                            0, 0));
        }

    }

    protected abstract EditableListTableModel createModel();

    protected abstract Object[] newItems();

    protected JComponent getListPanel() {
        table = new RightClickSelectableTable(createModel());
        table.setPreferredScrollableViewportSize(new Dimension(300, table.getRowHeight() * 6));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        TableCellEditor editor = new FocusHandlingTextCellEditor();
        table.setDefaultEditor(String.class, editor);
        return new SimpleScrollPane(table);
    }


    protected JButton[] getButtons() {
        JButton addButton = new JButton("Add...");
        addButton.setMnemonic('A');
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addBeforeSelectionOrAtTheEnd();
            }
        });
        JButton removeButton = new JButton("Remove");
        removeButton.setMnemonic('R');
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                removeSelection();
            }

        });
        return new JButton[]{addButton, removeButton};
    }


    private void addBeforeSelectionOrAtTheEnd() {
        Object[] newItems = newItems();
        if (newItems == null || newItems.length == 0) return;
        int rowToInsertBefore = getRowToInsertBefore();
        insertItems(rowToInsertBefore, newItems);
        notifyOfInsertion(rowToInsertBefore, newItems);
    }

    private int getRowToInsertBefore() {
        int selectedRow = table.getSelectedRow() + 1;
        if (selectedRow < 0 || selectedRow > items.size()) {
            selectedRow = items.size();
        }
        return selectedRow;
    }

    private void insertItems(int rowToInsertBefore, Object[] newItems) {
        for (int i = 0; i < newItems.length; i++) {
            items.add(i + rowToInsertBefore, newItems[i]);
        }
    }

    private void notifyOfInsertion(int rowToInsertBefore, Object[] newItems) {
        AbstractTableModel model = (AbstractTableModel) table.getModel();
        int maxRow = rowToInsertBefore + newItems.length - 1;
        model.fireTableRowsInserted(rowToInsertBefore, maxRow);
        table.setRowSelectionInterval(rowToInsertBefore, maxRow);
    }

    private void removeSelection() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows == null || selectedRows.length == 0) return;

        Arrays.sort(selectedRows);
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int rowSelected = selectedRows[i];
            if (table.isEditing()) {
                TableCellEditor cellEditor = table.getCellEditor();
                if (cellEditor != null) {
                    cellEditor.stopCellEditing();
                }
            }
            items.remove(rowSelected);
            AbstractTableModel tableModel = (AbstractTableModel) table.getModel();
            tableModel.fireTableRowsDeleted(rowSelected, rowSelected);
        }
        int newSelection = selectedRows[0];
        if (newSelection < items.size()) {
            table.setRowSelectionInterval(newSelection, newSelection);
        }
    }

    private List getConfigList() {
        return getConfiguration().getItems();
    }

    public void apply() {
        if (validateItems(items)) {
            removeError();
            getConfiguration().setItems(items);
        }
    }

    private void removeError() {
        if (errorLabel != null) {
            remove(0);
            errorLabel = null;
            validate();
        }
    }

    private boolean validateItems(List items) {
        boolean valid = true;
        for (int i = 0; i < items.size(); i++) {
            Object object = items.get(i);
            if (object instanceof Validator) {
                if (!((Validator) object).isValid()) {
                    addError(object, i);
                    valid = false;
                }
            }
        }
        return valid;
    }

    private void addError(Object object, int index) {
        String messsage = object.toString() + " at row " + index + " has errors please validate";
        if (errorLabel != null) errorLabel.setText(messsage);
        else {
            errorLabel = new JLabel(messsage);
            errorLabel.setForeground(Color.RED);
            Font defaultFont = getFont();
            errorLabel.setFont(new Font(defaultFont.getFamily(), Font.ITALIC, defaultFont.getSize() + 1));
            add(errorLabel, new GridBagConstraints(0, 0, 2, 1, 1.0D, 0.0D, 17, 2, new Insets(0, 0, 4, 0), 0, 0), 0);
        }
        validate();
    }

    public void reset() {
        List paths = getConfigList();
        items.clear();
        Iterator iterator = paths.iterator();
        while (iterator.hasNext()) {
            Object copiedItem = copyItem(iterator.next());
            if (copiedItem != null) {
                items.add(copiedItem);
            }
        }
    }

    private Object copyItem(Object o) {
        Object copy;
        try {
            Method cloneMethod = Object.class.getDeclaredMethod("clone", new Class[0]);
            cloneMethod.setAccessible(true);
            copy = cloneMethod.invoke(o, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return copy;
    }


    public boolean isModified() {
        return !items.equals(getConfigList());
    }

    private ListConfig getConfiguration() {
        return configuration;
    }

    public ArrayList getItems() {
        return items;
    }

    public static TitledBorder createEtchedTitleBorder(String s) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), s);
    }

    protected void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public RightClickSelectableTable getTable() {
        return table;
    }

}
