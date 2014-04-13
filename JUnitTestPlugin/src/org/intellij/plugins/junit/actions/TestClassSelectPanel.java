/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/TestClassSelectPanel.java,v 1.6 2005/10/19 08:16:55 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/10/19 08:16:55 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import org.intellij.plugins.junit.TestClassDef;
import org.intellij.plugins.junit.TestedClassDef;
import org.intellij.plugins.ui.SelectPanel;
import org.intellij.plugins.ui.SelectPanelListModel;
import org.intellij.plugins.util.JPanelBuilder;
import org.intellij.plugins.util.PluginPsiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Comparator;

public class TestClassSelectPanel extends SelectPanel {

   private PluginPsiUtil pluginPsiUtil;

   private TestClassActionHandler testClassActionHandler;

   public static final String NEW_TEST_CLASS_TITLE = "New test class";
   public static final String GO_TO_TEST_CLASS_TITLE = "Go to test class";
   private Choice[] existingTestClassChoices;
   private Choice[] newTestClassChoices;
   private String newTestTitle;
   private TestedClassDef testedClassDef;
   private JCheckBox fullTestPathCheckbox;
   public static boolean displayFullPaths;

   public interface TestClassActionHandler {
      void createOrGoToTestClass(TestClassDef testClassDef);
   }

   public TestClassSelectPanel(PluginPsiUtil pluginPsiUtil) {
      super(new TestClassSelectPanel.TestClassComparator(), new TestClassSelectPanelKeyAdapter());
      this.pluginPsiUtil = pluginPsiUtil;
   }

   public void show(TestedClassDef testedClassDef, TestClassActionHandler testClassActionHandler) {
      this.testClassActionHandler = testClassActionHandler;
      this.testedClassDef = testedClassDef;
      String testedClassTitle = GO_TO_TEST_CLASS_TITLE + " (" + testedClassDef.getName() + ")";
      this.newTestTitle = NEW_TEST_CLASS_TITLE + " (" + testedClassDef.getName() + ")";
      this.existingTestClassChoices = createExistingChoices();
      this.newTestClassChoices = createNewTestClassChoices(testedClassDef.getUntestedTests());
      setDisplayFullPaths(displayFullPaths);
      Choice defaultSelectedChoice = existingTestClassChoices != null ? existingTestClassChoices[0] : newTestClassChoices[0];
      Choice[] choices = existingTestClassChoices != null ? existingTestClassChoices : newTestClassChoices;
      show((testedClassDef.hasExistingTestClasses() ? testedClassTitle : newTestTitle), defaultSelectedChoice, choices);
   }

   private Choice[] createNewTestClassChoices(TestClassDef[] untestedTests) {
      Choice[] choices = new Choice[untestedTests.length];
      for (int i = 0; i < untestedTests.length; i++) {
         choices[i] = createTestClassChoice(untestedTests[i]);
      }
      return choices;
   }

   private Choice[] createExistingChoices() {
      if(!testedClassDef.hasExistingTestClasses()) return null;
      TestClassDef[] existingTests = testedClassDef.getExistingTests();
      Choice[] existingChoices = null;
      int index = 0;
      if (testedClassDef.hasUntestedTestClasses()) {
         existingChoices = new Choice[existingTests.length + 1];
         existingChoices[0] = createNewTestClassChoice();
         index++;
      } else {
         existingChoices = new Choice[existingTests.length];
      }
      for (int i = 0; i < existingTests.length; i++) {
         TestClassDef existingTest = existingTests[i];
         existingChoices[index] = createTestClassChoice(existingTest);
         index++;
      }
      return existingChoices;
   }

   private Choice createTestClassChoice(final TestClassDef testClassDef) {
      String path = displayFullPaths ? testClassDef.getFilePath() : pluginPsiUtil.resolveClassPath(testClassDef.getFilePath());
      return new Choice(path, testClassDef, new ChoiceListener() {
         public void itemChosen(Object choice) {
            testClassActionHandler.createOrGoToTestClass(testClassDef);
         }
      });
   }

   private Choice createNewTestClassChoice() {
      return new Choice(NEW_TEST_CLASS_TITLE, testedClassDef, new ChoiceListener() {
         public void itemChosen(Object choice) {
            setDisplayNewTestOptions();
         }
      });
   }

   private void setDisplayNewTestOptions() {
      ((SelectPanelListModel) getList().getModel()).setAll(newTestClassChoices);
      titleLabel.setText(newTestTitle);      
      resize();
   }

   public void selected() {
      if (getSelectedValue() == null || getSelectedValue().equals(testedClassDef)) {
         setDisplayNewTestOptions();
      } else {
         super.selected();
      }
   }

   public JComponent createTitleBar() {
      JPanelBuilder builder = new JPanelBuilder();
      builder.setOverrideInsets(new Insets(0, 0, 0, 0));
      builder.add(super.createTitleBar());

      JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
      builder.add(sep);

      fullTestPathCheckbox = createCheckbox();
      fullTestPathCheckbox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { setDisplayFullPaths(fullTestPathCheckbox.isSelected()); }
      });
      builder.add(fullTestPathCheckbox, createLabel("Full paths", 'A'));

      JPanel panel = builder.getPanel();
      panel.setBackground(TITLE_COLOR);
      return panel;
   }

   private static JLabel createLabel(String text, char mnemonicChar) {
      JLabel lc = new JLabel(text);
      lc.setDisplayedMnemonic(mnemonicChar);
      lc.setBackground(TITLE_COLOR);
      return lc;
   }

   private static JCheckBox createCheckbox() {
      JCheckBox fullPathsCheckBox = new JCheckBox();
      fullPathsCheckBox.setFocusable(false);
      fullPathsCheckBox.setBackground(TITLE_COLOR);
      fullPathsCheckBox.setSelected(displayFullPaths);
      return fullPathsCheckBox;
   }

   public String getTitle() {
      return titleLabel.getText();
   }

   public void toggleShowFullPaths() {
      fullTestPathCheckbox.setSelected(!fullTestPathCheckbox.isSelected());
      setDisplayFullPaths(fullTestPathCheckbox.isSelected());
   }

   public void setDisplayFullPaths(boolean fullPaths) {
      displayFullPaths = fullPaths;
      if (newTestClassChoices != null && newTestClassChoices.length > 0) {
         changeChoiceText(newTestClassChoices, fullPaths, 0);
      }
      if (existingTestClassChoices != null && existingTestClassChoices.length > 0) {
         changeChoiceText(existingTestClassChoices, fullPaths, testedClassDef.hasUntestedTestClasses() ? 1 : 0);
      }
      ((SelectPanelListModel) getList().getModel()).contentsChanged();
      resize();
   }

   private void changeChoiceText(Choice[] choices, boolean fullpath, int startIndex) {
      for (int i = startIndex; i < choices.length; i++) {
         Choice choice = choices[i];
         TestClassDef testClassDef = (TestClassDef) choice.value;
         String choiceName;
         if (fullpath) {
            choiceName = testClassDef.getFilePath();
         } else {
            choiceName = pluginPsiUtil.resolveClassPath(testClassDef.getFilePath());
         }
         choice.text = choiceName;
      }
   }

   public static class TestClassComparator implements Comparator/*<SelectPanel.Choice>*/ {
      public int compare(/*SelectPanel.Choice*/Object choice, /*SelectPanel.Choice*/Object choice1) {
         Choice c1 = (Choice) choice;
         Choice c2 = (Choice) choice1;
         if (NEW_TEST_CLASS_TITLE.equals(c1.text)) return -1;
         if (NEW_TEST_CLASS_TITLE.equals(c2.text)) return 1;
         return c1.text.compareToIgnoreCase(c2.text);
      }
   }
}

