/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/SelectDialog.java,v 1.13 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.13 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.intellij.plugins.ui.SelectPanel.Choice;
import org.intellij.plugins.ui.SelectPanel.ChoiceListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;


public class SelectDialog/*<T>*/ extends DialogWrapper implements Closeable {
   protected SelectPanel/*<T>*/ panel;

   public SelectDialog(Project project) {
      this(project, new SelectPanel/*<T>*/());
   }

   public SelectDialog(Project project, SelectPanel/*<T>*/ panel) {
      super(project, false);
      this.panel = panel;
      panel.setParent(this);
      setUndecorated(true);
      setModal(true);
      init();
   }

   public SelectDialog(Project project,
                       String title,
                       String defaultChoice,
                       String[] choices,
                       SelectPanel.ChoiceListener/*<T>*/ runAfterOk) {
      this(project, new SelectPanel/*<T>*/(title, defaultChoice, choices, runAfterOk));
   }

   public SelectDialog(Project project,
                       String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk,
                       ListCellRenderer choiceRenderer) {
      this(project, new SelectPanel/*<T>*/(title, defaultChoice, choices, runAfterOk, choiceRenderer));
   }

   public SelectDialog(Project project,
                       String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk) {
      this(project, new SelectPanel/*<T>*/(title, defaultChoice, choices, runAfterOk));
   }

   public SelectDialog(Project project,
                       String title,
                       int defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk,
                       ListCellRenderer choiceRenderer) {
      this(project,
           new SelectPanel/*<T>*/(title,
                                  (Choice /*<T>*/) getDefaultChoice(choices, defaultChoice),
                                  choices,
                                  runAfterOk,
                                  choiceRenderer));
   }


   public SelectDialog(Project project,
                       String title,
                       int defaultChoice,
                       Choice[] choices,
                       ChoiceListener/*<T>*/ runAfterOk) {
      this(project, title, (Choice /*<T>*/) getDefaultChoice(choices, defaultChoice), choices, runAfterOk);
   }

   public SelectDialog(Project project,
                       String title,
                       Choice/*<T>*/ defaultChoice,
                       Choice[] choices) {
      this(project, title, defaultChoice, choices, null);
   }

   public SelectDialog(Project project,
                       String title,
                       int defaultChoice,
                       Choice[] choices) {
      this(project, title, (Choice /*<T>*/) getDefaultChoice(choices, defaultChoice), choices);
   }

   private static /*<T>*/ Choice/*<T>*/ getDefaultChoice(Choice[] choices, int defaultChoice) {
      if (defaultChoice < 0 || defaultChoice >= choices.length) return null;
      return choices[defaultChoice];
   }

   protected Action[] createActions() {
      return new Action[0];
   }

   protected JComponent createCenterPanel() {
      return panel;
   }

   protected Border createContentPaneBorder() {
      return new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.GRAY, Color.WHITE, new Color(216, 216, 216));
   }

   public void show(String title, Choice/*<T>*/ selectedChoice, Choice[] choices) {
      panel.show(title, selectedChoice, choices);
      finishShow();
   }

   public void show(String title, Choice/*<T>*/ selectedChoice, Choice[] choices, ChoiceListener/*<T>*/ runAfterOk) {
      panel.show(title, choices, selectedChoice, runAfterOk);
      finishShow();
   }

   public void show(String title,
                    Choice/*<T>*/ selectedChoice,
                    Choice[] choices,
                    ChoiceListener/*<T>*/ runAfterOk,
                    ListCellRenderer renderer) {
      panel.show(title, choices, selectedChoice, runAfterOk, renderer);
      finishShow();
   }

   public void show(String title, String selectedChoice, String[] choices, ChoiceListener runAfterOk) {
      panel.show(title, choices, selectedChoice, runAfterOk);
      finishShow();
   }

   public void show() {
      panel.show();
      finishShow();
   }

   protected void finishShow() {
      super.show();
   }

   public void close() {
      close(0);
   }

   // For test
   public void simulateSelection(int index) {
      if (index >= 0) {
         panel.setSelectedIndex(index);
         panel.selected();
      }
   }

   public int getSelectedIndex() {
      return panel.getSelectedIndex();
   }

   public/*T*/Object getSelectedValue() {
      return panel.getSelectedValue();
   }


   public String getTitle() {
      return panel.getTitle();
   }
}
