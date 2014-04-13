package org.intellij.plugins.ui;

import com.intellij.openapi.diagnostic.Logger;
import org.intellij.plugins.ui.SelectPanel.Choice;
import org.intellij.plugins.util.LogUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SelectPanelKeyAdapter extends KeyAdapter {
   protected SelectPanel panel;
   String shortcuts;
   protected static final Logger LOG = LogUtil.getLogger();

   public SelectPanelKeyAdapter() {
      shortcuts = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   }

   public SelectPanelKeyAdapter(String shortcuts) {
      this.shortcuts = shortcuts;
   }

   public void keyPressed(KeyEvent e) {
      debug("event{ch=" + e.getKeyChar() + ",co=" + e.getKeyCode() + ",modif=" + e.getModifiers() + "}");
      if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
         panel.selected();
         e.consume();
         debug("Processing 'ENTER' or 'SPACE'");
         return;
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
         panel.setSelectedIndex(getIndexWithinBound(panel.getSelectedIndex() - 1));
         e.consume();
         debug("Processing 'UP'");
         return;
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
         panel.setSelectedIndex(getIndexWithinBound(panel.getSelectedIndex() + 1));
         e.consume();
         debug("Processing 'DOWN'");
         return;
      }
      List/*<Choice>*/ choices = panel.getChoices();
      if (choices != null && (e.getModifiers() == 0 || e.getModifiers() == KeyEvent.SHIFT_MASK)) {
         for (int i = 0; i < choices.size(); i++) {
            Choice choice = (SelectPanel.Choice) choices.get(i);
            if (Character.toLowerCase(choice.shortcut) == Character.toLowerCase(e.getKeyChar())) {
               panel.setSelectedIndex(i);
               panel.selected();
               e.consume();
               debug("Processing '" + e.getKeyChar() + "'");
               return;
            }
         }
      }
   }

   private int getIndexWithinBound(int index) {
      if (index >= panel.getListSize()) {
         index = 0;
      } else if (index < 0) {
         index = panel.getListSize() - 1;
      }
      return index;
   }

   public void setPanel(SelectPanel dialog) {
      this.panel = dialog;
   }

   public static void debug(String message) {
      LOG.debug(message);
   }

}
