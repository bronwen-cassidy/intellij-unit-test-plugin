package org.intellij.plugins.junit.actions;

import org.intellij.plugins.ui.SelectPanel;
import org.intellij.plugins.ui.SelectPanelKeyAdapter;

import java.awt.event.KeyEvent;

// TODO add keyboard shortcuts for goto and create test class options
public class TestClassSelectPanelKeyAdapter extends SelectPanelKeyAdapter {
   
   public void setPanel(SelectPanel panel) {
      assert panel instanceof TestClassSelectPanel;
      super.setPanel(panel);
   }

   public void keyPressed(KeyEvent e) {
      super.keyPressed(e);
      if (e.getKeyCode() == KeyEvent.VK_A && e.getModifiers() == KeyEvent.ALT_MASK) {
         ((TestClassSelectPanel)panel).toggleShowFullPaths();
         e.consume();
         debug("Processing 'Alt-A'");
         return;
      }
   }
}
