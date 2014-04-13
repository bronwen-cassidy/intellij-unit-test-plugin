/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/SelectPanel.java,v 1.5 2005/10/19 08:19:05 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/10/19 08:19:05 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.VerticalFlowLayout;
import org.intellij.plugins.util.LogUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Comparator;
import java.util.List;


public class SelectPanel/*<T>*/ extends JPanel {

    private static final Logger LOG = LogUtil.getLogger();

    protected String title;
    private boolean done;
    private boolean ok = false;
    private long start;
    private JList jList;
    private ChoiceListener/*<T>*/ runAfterOk;
    private ListCellRenderer cellRenderer;
    private SelectPanelKeyAdapter keyAdapter;
    protected Closeable parent;
    public static final Color TITLE_COLOR = new Color(214, 214, 214);
    protected JLabel titleLabel;

    public static class Choice /*<T>*/ {

        public String text;
        public /*T*/ Object value;
        public ChoiceListener/*<T>*/ listener;
        public char shortcut;

        public Choice(String text, /*T*/Object value) {
            this.text = text;
            this.value = value;
        }

        public Choice(String text, /*T*/Object value, ChoiceListener/*<T>*/ listener) {
            this.text = text;
            this.value = value;
            this.listener = listener;
        }

        public void chosen(ChoiceListener defaultListener) {
            if (listener == null) {
                defaultListener.itemChosen(value);
            } else {
                listener.itemChosen(value);
            }
        }

        public String toString() {
            return "Choice(" + shortcut + "," + text + "," + value + ")";
        }
    }

    public interface ChoiceListener/*<T>*/ {

        void itemChosen(/*T*/Object choice);
    }

    public SelectPanel() {
        this(SelectPanelListModel.NOP_COMPARATOR, new SelectPanelKeyAdapter());
    }

    public SelectPanel(Comparator comparator, SelectPanelKeyAdapter keyAdapter) {
        jList = new JList(new SelectPanelListModel(comparator));
        cellRenderer = new ChoiceCellRenderer();
        setKeyAdapter(keyAdapter);
    }


    public SelectPanel(String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk,
                       ListCellRenderer listCellRenderer) {
        this();
        init(title, choices, defaultChoice, runAfterOk, listCellRenderer);
    }

    public SelectPanel(String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk) {
        this(title, defaultChoice, choices, runAfterOk, new ChoiceCellRenderer());
    }

    public SelectPanel(String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices) {
        this(title, defaultChoice, choices, null);
    }


    public SelectPanel(String title,
                       String selectedChoice,
                       String[] choices,
                       ChoiceListener/*<T>*/ runAfterOk) {
        this();
        init(title, choices, selectedChoice, runAfterOk);
    }

    private void init(String title, String[] choices, String selectedChoice, ChoiceListener runAfterOk) {
        Choice[] newChoices = new Choice[choices.length];
        Choice defaultNewChoice = null;
        for (int i = 0; i < choices.length; i++) {
            String choice = choices[i];
            newChoices[i] = new Choice(choice, choice);
            if (choice.equals(selectedChoice)) defaultNewChoice = newChoices[i];
        }
        init(title, newChoices, defaultNewChoice, runAfterOk);
    }

    protected void init(String title,
                        Choice[] choices,
                        Choice/*<T>*/ selectedChoice,
                        ChoiceListener/*<T>*/ runAfterOk,
                        ListCellRenderer listCellRenderer) {
        init();
        this.title = title;
        titleLabel.setText(title);
        this.runAfterOk = runAfterOk;
        ((SelectPanelListModel) this.jList.getModel()).addAll(choices);
        this.cellRenderer = listCellRenderer;
        jList.setSelectedValue(selectedChoice, true);
    }

    private void init(String title,
                      Choice[] choices, Choice/*<T>*/ selectedChoice,
                      ChoiceListener/*<T>*/ runAfterOk) {
        init(title, choices, selectedChoice, runAfterOk, new ChoiceCellRenderer());
    }

    protected void init() {
        setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, true));
        setBackground(Color.RED);
        add(createTitleBar());
        add(jList);
        jList.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                dispose();
            }
        });
        jList.setBorder(new EmptyBorder(4, 4, 4, 4));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                // Since there's no API to get the current mouse-cursor position,
                // it is not possible to move the dialog away from the mouse. If there
                // is a mouse event up to 10ms after dialog.show(), the cursor is moved
                // to the top left corner of the dialog to avoid preselecting an entry
                // at the current position. I guess that still needs some tweaking...
                final long l = System.currentTimeMillis();
                if (l - start <= 10) {
                    try {
                        final Point location = getLocation();
                        new Robot().mouseMove((int) location.getX(), (int) location.getY());
                    } catch (AWTException e1) {
                        LOG.error("Cannot move mouse", e1);
                    }
                }
            }

            public void mousePressed(MouseEvent event) {
                event.consume();
                selected();
            }

            public void mouseReleased(MouseEvent event) {
                event.consume();
                selected();
            }
        });
        jList.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                setSelectedIndex(jList.locationToIndex(e.getPoint()));
            }
        });
        assert keyAdapter != null;
        jList.addKeyListener(keyAdapter);
        assert cellRenderer != null;
        jList.setCellRenderer(cellRenderer);
    }

    protected JComponent createTitleBar() {
        titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
        p.add(titleLabel);
        return p;
    }

    public void show(String title, Choice/*<T>*/ selectedChoice, Choice[] choices) {
        show(title, choices, selectedChoice, runAfterOk);
    }

    public void show(String title,
                     Choice[] choices,
                     Choice selectedChoice,
                     ChoiceListener runAfterOk) {
        init(title, choices, selectedChoice, runAfterOk);
        show();
    }

    public void show(String title,
                     Choice[] choices,
                     Choice selectedChoice,
                     ChoiceListener runAfterOk,
                     ListCellRenderer renderer) {
        init(title, choices, selectedChoice, runAfterOk, renderer);
        show();
    }

    public void show(String title, String[] choices, String selectedChoice, ChoiceListener runAfterOk) {
        init(title, choices, selectedChoice, runAfterOk);
        show();
    }


    public void show() {
        start = System.currentTimeMillis();
        super.show();
    }

    public void dispose() {
        if (!isCancelled()) {
            // hmm, dispose() seems to be called twice
            if (!done) {
                done = true;
                Choice selectedChoice = getSelectedChoice();
                if (selectedChoice != null)
                    selectedChoice.chosen(runAfterOk);
            }
        }
        parent.close();
    }

    public boolean isCancelled() {
        return !ok;
    }

    public boolean isOk() {
        return ok;
    }

    public /*T*/Object getSelectedValue() {
        return getSelectedChoice() == null ? null : getSelectedChoice().value;
    }

    private Choice/*<T>*/ getSelectedChoice() {
        return (Choice /*<T>*/) jList.getSelectedValue();
    }

    public List/*<Choice<T>>*/ getChoices() {
        return ((SelectPanelListModel) getList().getModel()).getItems();
    }

    public JList getList() {
        return jList;
    }

    public String getTitle() {
        return title;
    }

    public void setParent(Closeable parent) {
        this.parent = parent;
    }

    public void setModel(ListModel model) {
        jList.setModel(model);
    }

    public void setKeyAdapter(SelectPanelKeyAdapter adapter) {
        this.keyAdapter = adapter;
        this.keyAdapter.setPanel(this);
    }

    public void setSelectedIndex(int newSelectedIndex) {
        jList.setSelectedIndex(newSelectedIndex);
    }

    public int getSelectedIndex() {
        return jList.getSelectedIndex();
    }

    public int getListSize() {
        return jList.getModel().getSize();
    }

    public void selected() {
        ok = true;
        dispose();
    }

    public void resize() {
        Component component = this;
        while (component != null && !(component instanceof JDialog)) {
            component = component.getParent();
        }
        if (component != null) {
            component.setSize(getPreferredSize());
            component.validate();
        }
    }

    private static class ChoiceCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            Choice choice = (Choice) value;
            String shortcut = "  ";
            if (choice.shortcut != 0) {
                shortcut = "" + choice.shortcut + " ";
            }
            String text = shortcut + String.valueOf(choice.text);
            if (text.length() > 400) {
               text = text.substring(0, 400) + "...";
            }
            return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
        }
    }

}
