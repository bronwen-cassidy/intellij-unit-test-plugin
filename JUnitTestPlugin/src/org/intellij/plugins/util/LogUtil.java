/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/LogUtil.java,v 1.5 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;

/**
 * User: Jacques
 * Date: Aug 5, 2003
 * Time: 11:54:29 AM
 */
public class LogUtil {
   public static String getCallerMethod() {
      String methodName = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null) {
         methodName = stackTrace[2].getClassName() + "." + stackTrace[2].getMethodName();
      }

      return methodName;
   }

   public static String getCallerClass() {
      String className = "";
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stackTrace != null) {
         className = stackTrace[2].getClassName();
      }

      return className;
   }

   public static Logger getLogger(Class c) {
      return Logger.getInstance(c.getName());
   }

   public static Logger getLogger() {
      return Logger.getInstance(getCallerClass());
   }

//   static {
//      Logger.setApplicationInfoProvider(new ApplicationInfoProvider() {
//         public String getInfo() { return new Version().getVersion(); }
//      });
//   }

}
