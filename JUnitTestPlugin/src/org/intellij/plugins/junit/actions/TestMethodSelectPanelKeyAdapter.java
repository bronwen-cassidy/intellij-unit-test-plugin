package org.intellij.plugins.junit.actions;

import org.intellij.plugins.ui.SelectPanel;
import org.intellij.plugins.ui.SelectPanelKeyAdapter;

import java.awt.event.KeyEvent;

public class TestMethodSelectPanelKeyAdapter extends SelectPanelKeyAdapter {
   public void setPanel(SelectPanel panel) {
      assert panel instanceof TestMethodSelectPanel;
      super.setPanel(panel);
   }

   public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      if (e.getKeyCode() == KeyEvent.VK_A && e.getModifiers() == KeyEvent.ALT_MASK) {
         ((TestMethodSelectPanel)panel).toggleAllTests();
         e.consume();
         debug("Processing 'Alt-A'");
         return;
      }
      if (e.getKeyCode() == KeyEvent.VK_P && e.getModifiers() == KeyEvent.ALT_MASK) {
         ((TestMethodSelectPanel)panel).togglePrettyTests();
         e.consume();
         debug("Processing 'Alt-P'");
         return;
      }
   }
}
