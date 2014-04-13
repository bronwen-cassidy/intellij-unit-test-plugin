/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/PsiDirectoryUtil.java,v 1.11 2005/08/27 20:30:44 shadow12 Exp $
 * $Revision: 1.11 $
 * $Date: 2005/08/27 20:30:44 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class PsiDirectoryUtil {

    private static final Logger LOG = LogUtil.getLogger(PsiDirectoryUtil.class);
    private ProjectRootManager rootManager;
    private PsiManager manager;

    public PsiDirectoryUtil(ProjectRootManager projectRootManager, PsiManager manager) {
        this.rootManager = projectRootManager;
        this.manager = manager;
    }

    public PsiDirectory findOrCreateDirectory(PsiDirectory dir,
                                              String path) throws IncorrectOperationException {
        StringTokenizer tokens = new StringTokenizer(path, "/\\.");
        while (tokens.hasMoreTokens()) {
            String name = tokens.nextToken();
            dir = findOrCreateSubDirectory(dir, name);
        }
        return dir;
    }

    private PsiDirectory findOrCreateSubDirectory(PsiDirectory dir,
                                                  String name) throws IncorrectOperationException {
        assert dir != null;
        assert name != null;
        PsiDirectory subDir = null;
        final PsiDirectory[] subdirectories = dir.getSubdirectories();
        for (int i = 0; i < subdirectories.length; i++) {
            if (subdirectories[i].getName().equals(name)) {
                subDir = subdirectories[i];
                LOG.debug("found " + subDir);
                break;
            }
        }
        if (subDir == null) {
            LOG.debug("creating " + name);
            subDir = dir.createSubdirectory(name);
        }
        return subDir;
    }

    public PsiDirectory findOrCreateDirectory(String path) throws IncorrectOperationException {
        LOG.debug("findOrCreateDirectory(" + path + ")");
        PsiDirectory root = findRootThatContainsPath(path);
        if (root == null) return null;
        String rootPath = root.getVirtualFile().getPath();
        if (path.length() == rootPath.length()) return root;
        String remainerPath = path.substring(rootPath.length() + 1);
        return findOrCreateDirectory(root, remainerPath);
    }

    public boolean isWithinSourceRoots(String testClassPath) {
        return findRootThatContainsPath(testClassPath) != null;
    }

    public PsiDirectory findRootThatContainsPath(String path) {
        String pathWithTrailingSlash = path.charAt(path.length() - 1) == '/' ? path : path + '/';
        ArrayList matchingRoots = new ArrayList();
        VirtualFile[] rootDirectories = rootManager.getContentSourceRoots();
        for (int i = 0; i < rootDirectories.length; i++) {
            VirtualFile rootDirectory = rootDirectories[i];
            String rootPath = rootDirectory.getPath();
            if (pathWithTrailingSlash.startsWith(rootPath + "/")) {
                matchingRoots.add(manager.findDirectory(rootDirectory));
            }
        }
        if (matchingRoots.size() == 0) {
            return null;
        }
        sortDirsLongestNameFirst(matchingRoots);
        return (PsiDirectory) matchingRoots.get(0);
    }

    private void sortDirsLongestNameFirst(List dirs) {
        Collections.sort(dirs, new Comparator() {
            public int compare(Object o1,
                               Object o2) {
                return ((PsiDirectory) o2).getVirtualFile().getPath().length() -
                        ((PsiDirectory) o1).getVirtualFile().getPath().length();
            }
        });
    }
}
