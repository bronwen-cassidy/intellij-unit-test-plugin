/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/actions/HandledAction.java,v 1.4 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import org.intellij.plugins.util.DataContextUtil;
import org.intellij.plugins.beans.PluginBeanFactory;
import org.intellij.plugins.util.LogUtil;

public class HandledAction extends AnAction {

   protected static final Logger LOG = LogUtil.getLogger();

   private static final ActionHandler NULL_HANDLER = new NullActionHandler();

   public void actionPerformed(AnActionEvent event) {
       PsiDocumentManager.getInstance(DataContextUtil.getProject(event)).commitAllDocuments();
       getHandler(event).actionPerformed();
   }

    protected ActionHandler getHandler(AnActionEvent event) {
      ActionHandler handler = createHandler(event);
      handler.setEvent(event);
      return handler;
   }

   protected ActionHandler createHandler(AnActionEvent event) {
      ActionHandler handler = createHandler(DataContextUtil.getProject(event), getHandlerClass());
      if (handler == null) return NULL_HANDLER;
      return handler;
   }

   protected ActionHandler createHandler(Project project, Class handlerClass) {
      return (ActionHandler) PluginBeanFactory.getBean(project, handlerClass);
   }

   public void update(AnActionEvent event) {
      getHandler(event).update();
   }

   protected String getIconName() {
      return null;
   }

   protected Class getHandlerClass() {return null;}

   private static class NullActionHandler extends ActionHandler {
      public void update() { getPresentation().setVisible(false); }

      public void actionPerformed() { }
   }

   public static void debug(String message) {
      if (LOG.isDebugEnabled()) LOG.debug(message);
   }
}
