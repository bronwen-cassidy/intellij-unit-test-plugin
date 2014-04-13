/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/SimpleListModel.java,v 1.6 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleListModel/*<T>*/ extends AbstractListModel {
   private final List/*<T>*/ list;

   public SimpleListModel() {
      this(new ArrayList/*<T>*/());
   }

   public SimpleListModel(Object[] list) {
      this.list = new ArrayList/*<T>*/(Arrays.asList(list));
   }

   public SimpleListModel(List/*<T>*/ list) {
      this.list = list;
   }

   public int getSize() {
      return list.size();
   }

   public List/*<T>*/ getElements() {
      return list;
   }

   public /*T*/Object getElementAt(int index) {
      return list.get(index);
   }

   public void addElement(Object elt) {
      list.add(elt);
      fireIntervalAdded(this, list.size() - 1, list.size());
   }

   public void addAllElements(Object[] elts) {
      int fromIndex = list.size();
      list.addAll(Arrays.asList(elts));
      int toIndex = list.size();
      fireIntervalAdded(this, fromIndex, toIndex);
   }

   public void removeAllElement(Object[] elts) {
      int toIndex = list.size();
      list.removeAll(Arrays.asList(elts));
      int fromIndex = list.size();
      fireIntervalRemoved(this, fromIndex, toIndex);
   }

}
