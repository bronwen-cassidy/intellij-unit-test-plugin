/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/TemplateConfig.java,v 1.10 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.10 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.JDOMExternalizableStringList;
import org.intellij.plugins.junit.adaptor.IdeaFacade;
import org.intellij.plugins.util.CollectionsUtil;
import org.intellij.plugins.util.TemplateEngine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class TemplateConfig extends Config {

    public boolean DONT_SHOW_TEMPLATE_DIALOG_IF_ONLY_ONE_TEMPLATE;
    public boolean DONT_SHOW_METHODS_DIALOG_IF_ONLY_ONE_METHOD;

    private String[] validTestTemplates;
    public String LAST_TEMPLATE_NAME = null;
    public JDOMExternalizableStringList TEST_TEMPLATES = new JDOMExternalizableStringList();
    private String type;

    public TemplateConfig(Project project, String type) {
        super(project);
        this.type = type;
        debug("creating TemplateConfiguration for current project");
    }

    protected void initOnProjectOpened() {
    }

    public String getComponentName() {
        return "JUnitTemplateConfiguration";
    }

    public void updateInternalState() {
        if (TEST_TEMPLATES.size() == 0) {
            TEST_TEMPLATES.addAll(getDefaultTemplateNames());
        }
        if (isTemplateInvalid(LAST_TEMPLATE_NAME)) {
            LAST_TEMPLATE_NAME = getDefaultTemplate();
        }
        initValidTestTemplates();
    }

    private String getDefaultTemplate() {
        if (getDefaultTemplateNames() != null && getDefaultTemplateNames().size() > 0)
            return (String) getDefaultTemplateNames().get(0);
        return null;
    }

    protected abstract List/*<String>*/ getDefaultTemplateNames();

    protected boolean isTemplateInvalid(String templateName) {
        return !Arrays.asList(getValidTestTemplates()).contains(templateName);
    }

    private void initValidTestTemplates() {
        List list = getListOfValidTestTemplates();
        validTestTemplates = (String[]) list.toArray(new String[0]);
    }

    public String[] getValidTestTemplates() {
        if (validTestTemplates == null) initValidTestTemplates();
        return validTestTemplates;
    }

    public List getListOfValidTestTemplates() {
        List list = getListOfPotentialTestTemplates();
        list = CollectionsUtil.intersection(TEST_TEMPLATES.toArray(), list.toArray());
        if (list.size() == 0)
            addDefaultTemplatesTo(list);
        Collections.sort(list);
        return list;
    }

    public List getListOfPotentialTestTemplates() {
        List list = initTemplateList();
        addDefaultTemplatesTo(list);
        return list;
    }

    private void addDefaultTemplatesTo(List list) {
        for (Iterator it = getDefaultTemplateNames().iterator(); it.hasNext();) {
            String template = (String) it.next();
            if (!list.contains(template)) {
                list.add(template);
            }
        }
    }

    private List initTemplateList() {
        String[] templates = getFileTemplatesDir().list(getFileFilter());
        if (templates == null) templates = new String[0];
        return new ArrayList(Arrays.asList(templates));
    }

    protected FilenameFilter getFileFilter() {
        return new FileOnlyFilter();
    }

    protected File getFileTemplatesDir() {
        return TemplateEngine.getFileTemplatesDir();
    }

    public List getListOfChosenTestTemplates() {
        return TEST_TEMPLATES;
    }

    public String getType() {
        return type;
    }

    class FileOnlyFilter implements FilenameFilter {

        public boolean accept(File dir,
                              String name) {
            return new File(dir, name).isFile();
        }
    }
}
