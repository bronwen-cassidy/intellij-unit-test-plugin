/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/MessageHandler.java,v 1.2 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;

public class MessageHandler {
   private Project project;

   public MessageHandler(Project project) {
      this.project = project;
   }

   public void showErrorDialog(String message, String title) {
      Messages.showErrorDialog(project, message, title);
   }

   public void showMessageDialog(String message, String title, Icon icon) {
      Messages.showMessageDialog(project, message, title, icon);
   }
}