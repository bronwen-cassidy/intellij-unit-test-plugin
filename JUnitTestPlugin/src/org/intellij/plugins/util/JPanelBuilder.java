/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/JPanelBuilder.java,v 1.7 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.util.Comparing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JPanelBuilder {
   private String title;
   private ArrayList items;
   private ArrayList isOffset;
   private Insets insetsOverride;

   public JPanelBuilder(String s) {
      title = s;
      items = new ArrayList();
      isOffset = new ArrayList();
   }

   public JPanelBuilder() {
      this(null);
   }

   public void add(JComponent component) {
      add(component, false);
   }

   public void add(JComponent component, boolean isOffset) {
      items.add(component);
      this.isOffset.add(new Boolean(isOffset));
   }

   public void add(JComponent leftComponent, JComponent rightComponent) {
      add(leftComponent, rightComponent, false);
   }

   public void add(JComponent leftComponent, JComponent rightComponent, boolean isOffset) {
      items.add(new Pair(leftComponent, rightComponent));
      this.isOffset.add(new Boolean(isOffset));
   }

   public JPanel getTitledPanel(String title) {
      this.title = title;
      return getPanel();
   }


   public JPanel getPanel() {
      JPanel panel = new JPanel();
      if (title != null) {
         panel.setBorder(BorderUtil.newTitledBorder(title));
      }
      panel.setLayout(new GridBagLayout());
      for (int i = 0; i < items.size(); i++) {
         int weighty = 0;
         int offset = Boolean.TRUE.equals(isOffset.get(i)) ? 15 : 5;
         if (title == null) {
            offset -= 4;
         }
         Object obj = items.get(i);
         if (obj instanceof JComponent) {
            JComponent c = (JComponent) obj;
            byte vspace = ((byte) (!(c instanceof JLabel) && !(c instanceof JTextField) ? 0 : 2));
            panel.add(c, new GridBagConstraints(0, i, 2, 1,
                                                1.0D, weighty,
                                                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                                                getInsets(new Insets(vspace, offset, vspace, 5)), 0, 0));
         } else {
            Pair pair = (Pair) obj;
            JComponent c1 = (JComponent) pair.first;
            c1.setPreferredSize(c1.getPreferredSize());
            JComponent c2 = (JComponent) pair.second;
            byte vspace = ((byte) (!(c1 instanceof JLabel) && !(c1 instanceof JTextField) ? 0 : 2));
            panel.add(c1, new GridBagConstraints(0, i, 1, 1,
                                                 0, weighty,
                                                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                                                 getInsets(new Insets(vspace, offset, vspace, 5)), 0, 0));
            vspace = ((byte) (!(c2 instanceof JLabel) && !(c2 instanceof JTextField) ? 0 : 2));
            panel.add(c2, new GridBagConstraints(1, i, 1, 1,
                                                 0.0D, weighty,
                                                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                                                 getInsets(new Insets(vspace, 5, vspace, 5)), 0, 0));
         }
      }

      JPanel jpanel = new JPanel();
      jpanel.setPreferredSize(new Dimension(0, 0));
      panel.add(jpanel,
                new GridBagConstraints(0, items.size(), 2, 1, 1.0D, 1.0D, 11, 2, new Insets(0, 0, 0, 0), 0, 0));
      return panel;
   }

   public void setOverrideInsets(Insets insets) {
      insetsOverride = insets;
   }

   private Insets getInsets(Insets insets) {
      if (insetsOverride != null) return insetsOverride;
      return insets;
   }

   static public class Pair {

      public Pair(Object obj, Object obj1) {
         first = obj;
         second = obj1;
      }

      public final boolean equals(Object obj) {
         return (obj instanceof Pair) &&
                Comparing.equal(first, ((Pair) obj).first) &&
                Comparing.equal(second, ((Pair) obj).second);
      }

      public final int hashCode() {
         int i = 0;
         if (first != null) {
            i += first.hashCode();
         }
         if (second != null) {
            i += second.hashCode();
         }
         return i;
      }

      public Object first;
      public Object second;
   }

}
