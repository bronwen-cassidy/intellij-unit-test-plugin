/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 12-Jul-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit.adaptor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jul-2007 17:02:55
 */
public interface IdeaFacade {

    VirtualFile getProjectFile(Project project);

    boolean isDefaultProject(Project project);
}
