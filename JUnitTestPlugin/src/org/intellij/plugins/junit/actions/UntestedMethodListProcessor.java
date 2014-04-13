/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/UntestedMethodListProcessor.java,v 1.4 2005/08/20 16:55:54 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/08/20 16:55:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import org.intellij.plugins.junit.*;
import org.intellij.plugins.junit.config.MethodTemplateConfig;
import org.intellij.plugins.junit.template.TestMethodCreator;
import org.intellij.plugins.ui.SelectDialog;
import org.intellij.plugins.ui.SelectPanel.Choice;
import org.intellij.plugins.ui.SelectPanel.ChoiceListener;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Set;

import com.intellij.openapi.project.Project;

public class UntestedMethodListProcessor extends TestMethodProcessor {

   private ClassLocator classLocator;
   private ClassUntestedMethodsLocator classUntestedMethodsLocator;
   private SelectDialog selectDialog;

   public UntestedMethodListProcessor(ClassLocator classLocator,
                                      ClassUntestedMethodsLocator classUntestedMethodsLocator,
                                      MethodTemplateConfig methodTemplateConfig,
                                      MethodLocator methodLocator,
                                      TestMethodCreator testMethodCreator,
                                      EditorHelper editorHelper,
                                      Project project) {
      super(methodTemplateConfig, methodLocator, testMethodCreator, editorHelper);
      this.classLocator = classLocator;
      this.classUntestedMethodsLocator = classUntestedMethodsLocator;
      this.selectDialog = new SelectDialog(project);
   }

   protected void execute() {
      if (!classLocator.isTestClassConform(originClass)) return;

      TestedClassDef testedClassDef = classLocator.findTestedClassDef(originClass);
      TestClassDef testClassDef = testedClassDef.getTestClassDef(originClass);

      Set untestedMethods = classUntestedMethodsLocator.getUntestedMethods(testedClassDef, testClassDef);
      displayUntestedMethodList(untestedMethods, testClassDef);
   }

   private void displayUntestedMethodList(Set methods, final TestClassDef testClassDef) {
      if (methods.size() == 0) return;

      Choice[] choices = new Choice[methods.size()];
      int i = 0;
      for (Iterator it = methods.iterator(); it.hasNext();) {
         MethodDef method = (MethodDef) it.next();
         choices[i++] = new Choice(method.getName(), method);
      }
      selectDialog.show("Untested Methods",
                        choices[0],
                        choices,
                        new ChoiceListener() {
                           public void itemChosen(Object choice) {
                              originMethod = ((MethodDef) choice).getPsiMethod();
                              selectMethodTemplateAndCreate(testClassDef, null);
                           }
                        },
                        new UntestedMethodListCellRenderer());
   }

   class UntestedMethodListCellRenderer extends JLabel implements ListCellRenderer {

      private final Color SELECTED_COLOR = new Color(166, 202, 240);

      public UntestedMethodListCellRenderer() {
         setOpaque(true);
      }

      public Component getListCellRendererComponent(JList list,
                                                    Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus) {
         Choice choice = (Choice) value;
         setText(choice.text);
         Color background;
         if (isSelected || cellHasFocus) {
            background = SELECTED_COLOR;
         } else {
            background = list.getBackground();
         }
         setIcon(Icons.SELECT_METHOD_ACTION);
         setBackground(background);
         return this;
      }
   }
}
