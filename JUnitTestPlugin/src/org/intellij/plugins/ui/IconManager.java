/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/IconManager.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 21, 2003
 * Time: 4:28:57 PM
 * To change this template use Options | File Templates.
 */
public class IconManager {
   HashMap iconPathsByName;
   HashMap iconByName;

   public IconManager() {
      this.iconPathsByName = new HashMap();
      this.iconByName = new HashMap();
   }

   public void setIconPathsByName(HashMap iconPathsByName) {
      this.iconPathsByName = iconPathsByName;
   }

   public HashMap getIconPathsByName() {
      return iconPathsByName;
   }

   public Icon getIcon(String name) {
      Icon icon = (Icon) iconByName.get(name);
      if (icon == null) {
         String path = (String) iconPathsByName.get(name);
         icon = fetchIcon(path);
         iconByName.put(name, icon);
      }
      return icon;
   }

   protected Icon fetchIcon(String path) {
      return IconUtil.getIcon(path);
   }
}
