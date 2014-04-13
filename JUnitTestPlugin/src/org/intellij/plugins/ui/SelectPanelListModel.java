/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/SelectPanelListModel.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import com.intellij.ui.SortedListModel;

import java.util.Comparator;
import java.util.List;

public class SelectPanelListModel extends SortedListModel {
   public static final Comparator NOP_COMPARATOR = new Nop();
   private SelectPanelShortcutAllocator shortcutAllocator;

   public SelectPanelListModel() {
      this(NOP_COMPARATOR);
   }

   public SelectPanelListModel(Comparator comparator) {
      super(comparator);
      shortcutAllocator = new SelectPanelShortcutAllocator();
   }

   public SelectPanelListModel(SelectPanel.Choice[] list) {
      this();
      addAll(list);
   }

   public SelectPanelListModel(List/*<SelectPanel.Choice>*/ list) {
      this();
      addAll(list);
   }

   public void contentsChanged() {
      fireContentsChanged(this, 0, getSize());
   }

   protected void fireContentsChanged(Object source, int index0, int index1) {
      allocateShortcuts();
      super.fireContentsChanged(source, index0, index1);
   }

   protected void fireIntervalAdded(Object source, int index0, int index1) {
      allocateShortcuts();
      super.fireIntervalAdded(source, index0, index1);
   }

   protected void fireIntervalRemoved(Object source, int index0, int index1) {
      allocateShortcuts();
      super.fireIntervalRemoved(source, index0, index1);
   }

   private void allocateShortcuts() {
      shortcutAllocator.assignShortcuts(getItems());
   }

   public static class Nop implements Comparator {
      public int compare(Object o, Object o1) {
         return -1;
      }
   }
}
