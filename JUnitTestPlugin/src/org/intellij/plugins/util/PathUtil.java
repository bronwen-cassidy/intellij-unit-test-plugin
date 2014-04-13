/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/PathUtil.java,v 1.4 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

public class PathUtil {

    private char separator;

    public PathUtil(char separator) {
        this.separator = separator;
    }

    public String getNameFromPath(String path) {
        if (path == null || path.equals("")) return "";
        int lastSlashIndex = path.lastIndexOf(separator);
        if (lastSlashIndex == path.length() - 1)
            return getNameFromPath(path.substring(0, path.length() - 1));
        return path.substring(lastSlashIndex + 1);
    }

    public String getParentPathFromPath(String path) {
        if (path == null || path.equals("")) return "";
        int lastSlashIndex = path.lastIndexOf(separator);
        if (lastSlashIndex == -1) return "";
        if (lastSlashIndex == path.length() - 1)
            return getParentPathFromPath(path.substring(0, path.length() - 1));
        return path.substring(0, lastSlashIndex);
    }

    public String getFirstElementOfPath(String path) {
        if (path == null || path.equals("")) return "";
        int slashIndex = path.indexOf(separator);
        String name = slashIndex > 0 ? path.substring(0, slashIndex) : path;
        return name;
    }

    public String getSubPathPastFirstElement(String path) {
        if (path == null || path.equals("")) return "";
        int slashIndex = path.indexOf(separator);
        String subPackageName = slashIndex > 0 ? path.substring(path.indexOf(separator) + 1) : "";
        return subPackageName;
    }
}
