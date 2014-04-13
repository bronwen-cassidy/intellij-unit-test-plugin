/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/TemplateConfigEditor.java,v 1.17 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.17 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.project.Project;
import org.intellij.plugins.config.ConfigEditor;
import org.intellij.plugins.ui.DualListPickList;
import org.intellij.plugins.util.JPanelBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateConfigEditor extends ConfigEditor {

   private DualListPickList testTemplatesSelectionPanel;
   private JCheckBox dontShowDialogIfOnlyOneTemplateField;
   private String title;
   private String htmlHelp;

   public TemplateConfigEditor(Project project, Class configClass, String title, String htmlHelp) {
      super(project, configClass);
      this.title = title;
      this.htmlHelp = htmlHelp;
   }

   public JComponent createEditorPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.fill = GridBagConstraints.HORIZONTAL;
      c.anchor = GridBagConstraints.NORTHEAST;
      c.weightx = 0;

      JPanel templateSelectionPanel = createTemplateSelectionPanel(getConfig().getListOfPotentialTestTemplates(),
                                                                       new ArrayList(getConfig().getListOfChosenTestTemplates()));
      panel.add(templateSelectionPanel,c);
      c.gridx = 1;
      c.weightx = 1.0;
      panel.add(getHelpLabel(),c);
      return panel;
   }

   private JLabel getHelpLabel() {
      return createHtmlLabel("Template variables", htmlHelp);
   }

   private JPanel createTemplateSelectionPanel(List availableList, List selectedList) {
      JPanelBuilder builder = new JPanelBuilder();
      testTemplatesSelectionPanel = new DualListPickList("Other templates", title + " templates", availableList, selectedList);
      builder.add(testTemplatesSelectionPanel);
      dontShowDialogIfOnlyOneTemplateField = new JCheckBox("Don't show template dialog when only one is available");
      builder.add(dontShowDialogIfOnlyOneTemplateField);

      return builder.getPanel();
   }

   public void updateAvailableTemplates() {
      testTemplatesSelectionPanel.setLeftList(getConfig().getListOfPotentialTestTemplates());
   }

   private TemplateConfig getConfig() {
      return ((TemplateConfig) getConfiguration());
   }

   protected void copyFromConfig() {
      testTemplatesSelectionPanel.setRightList(new ArrayList(getConfig().TEST_TEMPLATES));
      dontShowDialogIfOnlyOneTemplateField.setSelected(getConfig().DONT_SHOW_TEMPLATE_DIALOG_IF_ONLY_ONE_TEMPLATE);
   }

   protected void copyToConfig() {
      getConfig().TEST_TEMPLATES.clear();
      getConfig().TEST_TEMPLATES.addAll(testTemplatesSelectionPanel.getRightList());
      getConfig().DONT_SHOW_TEMPLATE_DIALOG_IF_ONLY_ONE_TEMPLATE = dontShowDialogIfOnlyOneTemplateField.isSelected();
   }
   public boolean isModified() {
      final boolean equals =
            getConfig().DONT_SHOW_TEMPLATE_DIALOG_IF_ONLY_ONE_TEMPLATE == dontShowDialogIfOnlyOneTemplateField.isSelected() &&
            getConfig().TEST_TEMPLATES.equals(testTemplatesSelectionPanel.getRightList()); // &&
      return !equals;
   }

   public DualListPickList getTestTemplatesSelectionPanel() { return testTemplatesSelectionPanel; }

   public String getComponentName() {
      return "TemplateConfigEditor";
   }


}
