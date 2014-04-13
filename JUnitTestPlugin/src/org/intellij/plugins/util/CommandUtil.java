/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/CommandUtil.java,v 1.12 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.12 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class CommandUtil {
   public interface Command {
      Object run() throws Exception;
   }

   public static abstract class NamedCommand implements Command {
      protected String name;

      protected NamedCommand() { }

      protected NamedCommand(String name) { this.name = name; }
   }

   final static int READ = 0;
   final static int WRITE = 1;

   //FIXME Potential problem if multiple threads are running at the same time or the code is reentrent
   Object result;
   Exception exception;
   Project project;

   /**
    * @deprecated
    */
   public CommandUtil() { }

   public CommandUtil(Project project) {
      this.project = project;
   }

   public Object executeWriteCommand(NamedCommand cmd) throws Exception {
      return executeWriteCommand(cmd.name, cmd);
   }

   public Object executeWriteCommand(final String name, final Command cmd) throws Exception {
      if (EventQueue.isDispatchThread()) {
         executeCommand(name, cmd, WRITE);
      } else {
         SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
               executeCommand(name, cmd, WRITE);
            }
         });
      }
      return getResult();
   }

   /**
    * @deprecated
    */
   public Object executeWriteCommand(Project project, NamedCommand cmd) throws Exception {
      return executeWriteCommand(project, cmd.name, cmd);
   }

   /**
    * @deprecated
    */
   public Object executeWriteCommand(Project project, String name, Command cmd) throws Exception {
      this.project = project;
      return executeWriteCommand(name, cmd);
   }

   /**
    * @deprecated
    */
   public Object executeWriteCommandWithoutException(Project project, NamedCommand cmd) {
      return executeWriteCommandWithoutException(project, cmd.name, cmd);
   }

   public Object executeWriteCommandWithoutException(NamedCommand cmd) {
      return executeWriteCommandWithoutException(cmd.name, cmd);
   }

   /**
    * @deprecated
    */
   public Object executeWriteCommandWithoutException(Project project, String name, Command cmd) {
      this.project = project;
      return executeWriteCommandWithoutException(name, cmd);
   }

   public Object executeWriteCommandWithoutException(String name, Command cmd) {
      try {
         return executeWriteCommand(name, cmd);
      } catch (Exception e) {
         throw new RuntimeException("Should not have thrown an exception", e);
      }
   }

   public Object executeReadCommand(NamedCommand cmd) throws Exception {
      return executeReadCommand(cmd.name, cmd);
   }

   /**
    * @deprecated
    */

   public Object executeReadCommand(Project project, NamedCommand cmd) throws Exception {
      return executeReadCommand(project, cmd.name, cmd);
   }

   /**
    * @deprecated
    */
   public Object executeReadCommand(Project project, String name, Command cmd) throws Exception {
      this.project = project;
      return executeReadCommand(name, cmd);
   }

   public Object executeReadCommand(String name, Command cmd) throws Exception {
      executeCommand(name, cmd, READ);
      return getResult();
   }

   /**
    * @deprecated
    */

   public Object executeReadCommandWithoutException(Project project, NamedCommand cmd) throws Exception {
      this.project = project;
      return executeReadCommandWithoutException(cmd);
   }

   public Object executeReadCommandWithoutException(NamedCommand cmd) throws Exception {
      return executeReadCommandWithoutException(project, cmd.name, cmd);
   }

   /**
    * @deprecated
    */

   public Object executeReadCommandWithoutException(Project project, String name, Command cmd) throws Exception {
      this.project = project;
      return executeReadCommandWithoutException(name, cmd);
   }

   public Object executeReadCommandWithoutException(String name, Command cmd) {
      try {
         return executeReadCommand(project, name, cmd);
      } catch (Exception e) {
         throw new RuntimeException("Should not have thrown an exception", e);
      }
   }

   public Object runWriteAction(final Command cmd) throws Exception {
      if (EventQueue.isDispatchThread()) {
         runAction(cmd, WRITE);
      } else {
         SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
               runAction(cmd, WRITE);
            }
         });
      }

      return getResult();
   }

   public void runWriteActionWithoutException(Command command) {
      try {
         runWriteAction(command);
      } catch (Exception e) {
         throw new RuntimeException("Should not have thrown an exception", e);
      }
   }

   public Object runReadAction(Command cmd) throws Exception {
      runAction(cmd, READ);
      return getResult();
   }

   public void runReadActionWithoutException(Command command) {
      try {
         runReadAction(command);
      } catch (Exception e) {
         throw new RuntimeException("Should not have thrown an exception", e);
      }
   }

   private Object getResult() throws Exception {
      if (exception != null) {
         throw exception;
      }
      return result;
   }

   private void executeCommand(String name, final Command cmd, final int type) {
      Runnable runnable = new Runnable() {
         public void run() {
            runAction(cmd, type);
         }
      };
      CommandProcessor.getInstance().executeCommand(project, runnable, name, null);
   }

   private void runAction(Command cmd, int type) {
      Runnable runnable = getRunnable(cmd);
      if (type == WRITE) {
         ApplicationManager.getApplication().runWriteAction(runnable);
      } else {
         ApplicationManager.getApplication().runReadAction(runnable);
      }
   }

   private Runnable getRunnable(final Command cmd) {
      Runnable runnable = new Runnable() {
         public void run() {
            try {
               result = cmd.run();
            } catch (Exception e) {
               exception = e;
            }
         }
      };
      return runnable;
   }

   public void runOnMainThreadAsynchonously(Runnable runnable) {
      SwingUtilities.invokeLater(runnable);
   }

   public void runOnMainThreadSynchonously(Runnable runnable) {
      try {
         SwingUtilities.invokeAndWait(runnable);
      } catch (InterruptedException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      }
   }

   public Object runOnMainThreadSynchonously(Command command) throws Exception {
      runOnMainThreadSynchonously(getRunnable(command));
      return getResult();
   }
}
