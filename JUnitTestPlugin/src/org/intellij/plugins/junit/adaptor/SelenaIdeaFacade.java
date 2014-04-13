/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 12-Jul-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit.adaptor;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jul-2007 17:06:34
 */
public class SelenaIdeaFacade implements IdeaFacade {

    public VirtualFile getProjectFile(Project project) {
        return project.getBaseDir();
    }

    public boolean isDefaultProject(Project project) {
        return project == null || project.getBaseDir() == null;
    }
}
