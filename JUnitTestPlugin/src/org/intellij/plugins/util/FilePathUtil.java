/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/FilePathUtil.java,v 1.7 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;



public class FilePathUtil {
   static PathUtil util = new PathUtil('/');

   public static String extractNameFromPath(String path) {
      return util.getNameFromPath(path);
   }

   public static String extractParentPathFromPath(String path) {
      return util.getParentPathFromPath(path);
   }

   public static String extractFirstPathElement(String path) {
      return util.getFirstElementOfPath(path);
   }

   public static String extractSubPathPastFirstElement(String path) {
      return util.getSubPathPastFirstElement(path);
   }

   public static String removeExtension(String path) {
      int dotIndex = path.lastIndexOf('.');
      if (dotIndex != -1) {
         int slashIdex = path.lastIndexOf('/');
         if (dotIndex > slashIdex)
            return path.substring(0, dotIndex);
      }
      return path;
   }

   public static String replaceFileSeparator(String filePath, char replacement) {
      return filePath.replaceAll("[/\\\\]", ""+replacement);
   }
}
