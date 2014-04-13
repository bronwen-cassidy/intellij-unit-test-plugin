/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/JUnitTestConfiguration.java,v 1.15 2005/08/19 08:44:44 shadow12 Exp $
 * $Revision: 1.15 $
 * $Date: 2005/08/19 08:44:44 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.intellij.plugins.junit.config.Config;
import org.intellij.plugins.junit.ClassUntestedMethodsLocator;
import org.intellij.plugins.junit.RefactorerListenerProvider;
import org.intellij.plugins.junit.UntestedMethodHighlighter;
import org.intellij.plugins.junit.adaptor.IdeaFacade;

public class JUnitTestConfiguration extends Config {

    public boolean IS_AUTO_TEST_CLASS_RENAME_ENABLED = true;
    public boolean IS_AUTO_TEST_CLASS_MOVE_ENABLED = true;
    public boolean IS_AUTO_TEST_CLASS_REFACTORING_ENABLED = true;
    public boolean IS_AUTO_TEST_METHODS_RENAME_ENABLED = true;

    public boolean IS_UNTESTED_METHOD_ICON_ENABLED = true;
    public boolean IS_GET_METHOD_ICONS_ENABLED = true;
    public boolean IS_SET_METHOD_ICONS_ENABLED = true;
    public boolean IS_UNIT_TEST_ENABLED = true;

    private RefactorerListenerProvider refactorerListenerProvider;
    private UntestedMethodHighlighter untestedMethodHighlighter;
    private ClassUntestedMethodsLocator classUntestedMethodsLocator;
    private IdeaFacade ideaFacade;

    public static JUnitTestConfiguration getInstance(Project project) {
        return (JUnitTestConfiguration) project.getComponent(JUnitTestConfiguration.class);
    }

    public JUnitTestConfiguration(Project project) {
        super(project);
        debug("creating JUnitTestConfiguration for project");
    }

    public void projectClosed() {
        super.projectClosed();
        stopRefactoringListener();
        stopUntestedMethodHighlighter();
    }

    protected void initOnProjectOpened() {
    }

    public String getComponentName() {
        return "JUnitTestConfiguration";
    }

    protected void updateInternalState() throws ConfigurationException {
        updateRefactorerState();
        updateUntestedMethodHighlighterState();
    }

    private void updateUntestedMethodHighlighterState() {
        stopUntestedMethodHighlighter();
        classUntestedMethodsLocator.setOptions(IS_SET_METHOD_ICONS_ENABLED, IS_GET_METHOD_ICONS_ENABLED);
        if (isToBeMarked()) {
            untestedMethodHighlighter.mark();
        } else {
            untestedMethodHighlighter.unmark();
        }
        startUntestedMethodHighlighter();
    }

    private boolean isToBeMarked() {
        return IS_UNTESTED_METHOD_ICON_ENABLED && IS_UNIT_TEST_ENABLED;
    }

    private void startUntestedMethodHighlighter() {
        if (isToBeMarked()) {
            untestedMethodHighlighter.start();
        }
    }

    private void stopUntestedMethodHighlighter() {
        untestedMethodHighlighter.stop();
    }

    private void startRefactoringListener() {
        if (supportsRefactoring()) {
            refactorerListenerProvider.start();
        }
    }

    private boolean supportsRefactoring() {
        return IS_AUTO_TEST_CLASS_REFACTORING_ENABLED && IS_UNIT_TEST_ENABLED;
    }

    private void stopRefactoringListener() {
        if (refactorerListenerProvider != null) {
            refactorerListenerProvider.stop();
        }
    }

    public void updateRefactorerState() {
        stopRefactoringListener();
        refactorerListenerProvider.setOptions(IS_AUTO_TEST_CLASS_RENAME_ENABLED,
                IS_AUTO_TEST_CLASS_MOVE_ENABLED,
                IS_AUTO_TEST_METHODS_RENAME_ENABLED);
        startRefactoringListener();
    }

    public RefactorerListenerProvider getRefactorerListenerProvider() {
        return refactorerListenerProvider;
    }

    public void setRefactorerListenerProvider(RefactorerListenerProvider refactorerListenerProvider) {
        this.refactorerListenerProvider = refactorerListenerProvider;
    }

    public UntestedMethodHighlighter getUntestedMethodHighlighter() {
        return untestedMethodHighlighter;
    }

    public void setUntestedMethodHighlighter(UntestedMethodHighlighter untestedMethodHighlighter) {
        this.untestedMethodHighlighter = untestedMethodHighlighter;
    }

    public void setClassUntestedMethodsLocator(ClassUntestedMethodsLocator classUntestedMethodsLocator) {
        this.classUntestedMethodsLocator = classUntestedMethodsLocator;
    }

    public IdeaFacade getIdeaFacade() {
        return ideaFacade;
    }

    public void setIdeaFacade(IdeaFacade ideaFacade) {
        this.ideaFacade = ideaFacade;
    }
}
