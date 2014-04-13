/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/FileFilter.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * User: sg426575
 * Date: Oct 2, 2003
 * Time: 1:54:33 PM
 */
public interface FileFilter extends java.io.FileFilter {
   boolean accept(String path);

   boolean accept(VirtualFile file);

   boolean accept(File file);
}
