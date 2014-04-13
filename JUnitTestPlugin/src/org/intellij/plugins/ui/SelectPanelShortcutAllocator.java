/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/SelectPanelShortcutAllocator.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import org.intellij.plugins.ui.SelectPanel.Choice;

import java.util.List;

public class SelectPanelShortcutAllocator {
   String shortcuts;

   public SelectPanelShortcutAllocator() {
      shortcuts = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   }

   public SelectPanelShortcutAllocator(String shortcuts) {
      this.shortcuts = shortcuts;
   }


   public void assignShortcuts(List/*<Choice>*/ choices) {
      for (int i = 0; i < choices.size(); i++) {
         Choice choice = (SelectPanel.Choice) choices.get(i);
         choice.shortcut = getShortcut(i);
      }
   }

   private char getShortcut(int i) {
      if (i > shortcuts.length()) return 0;
      return shortcuts.toCharArray()[i];
   }

}
