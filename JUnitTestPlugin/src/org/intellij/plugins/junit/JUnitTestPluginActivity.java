/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/JUnitTestPluginActivity.java,v 1.4 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.junit.adaptor.IdeaFacade;

public abstract class JUnitTestPluginActivity {

    protected ClassLocator classLocator;
    protected MethodLocator methodLocator;
    protected IdeaFacade ideaFacade;
    protected Project project;
    protected boolean started = false;

    public JUnitTestPluginActivity(Project project, ClassLocator classLocator, MethodLocator methodLocator, IdeaFacade IdeaFacade) {
        this.project = project;
        this.classLocator = classLocator;
        this.methodLocator = methodLocator;
        ideaFacade = IdeaFacade;
    }

    public void start() {
        if (ideaFacade.isDefaultProject(project)) return;
        if (!started) {
            register();
            started = true;
            VirtualFile projectFile = ideaFacade.getProjectFile(project);
            debug("Starting " + getClass().getName() + " for project " + projectFile.getName());
        }
    }

    public void stop() {
        if (started) {
            unregister();
            started = false;
            debug("Stopping " + getClass().getName() + " for project " + ideaFacade.getProjectFile(project).getName());
        }
    }

    protected abstract void register();

    protected abstract void unregister();

    private static final Logger LOG = Logger.getInstance("org.intellij.plugins.junit.JUnitTestPluginActivity");

    private void debug(String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
    }

    public boolean isStarted() {
        return started;
    }
}
