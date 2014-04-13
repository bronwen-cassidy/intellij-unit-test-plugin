/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassPatternPanel.java,v 1.4 2005/11/19 11:56:10 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/11/19 11:56:10 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import org.intellij.plugins.ui.EditableListPanel;
import org.intellij.plugins.ui.EditableListTableModel;
import org.intellij.plugins.ui.EditableListTableModel.ColumnDesc;

public class ClassPatternPanel extends EditableListPanel {

   public ClassPatternPanel(ClassPatternConfig configuration) {
      super(configuration);
      initUI();
   }

   protected EditableListTableModel createModel() {
      return new EditableListTableModel(this, ClassPatternDesc.class, new ColumnDesc[]{
         new ColumnDesc("Tested class", "testedClassPattern", true),
         new ColumnDesc("Test class", "testClassPattern", true)});
   }

   protected Object[] newItems() {
      return new Object[]{new ClassPatternDesc()};
   }
}