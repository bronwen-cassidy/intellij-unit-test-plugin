/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/RegularFileFilter.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * User: sg426575
 * Date: Oct 2, 2003
 * Time: 1:56:06 PM
 */
public class RegularFileFilter implements FileFilter {
   private Project project;
   private FileUtil fileUtil;

   public RegularFileFilter() {
      this.fileUtil = new FileUtil();
   }

   public boolean accept(String path) {
      return accept(new File(path));
   }

   public boolean accept(File file) {
      return accept(fileUtil.ioFileToVirtualFile(file));
   }

   public boolean accept(VirtualFile file) {
      return file.getFileSystem().getProtocol().equals("file");
   }

}
