/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassPatternConfig.java,v 1.10 2005/08/27 21:30:28 shadow12 Exp $
 * $Revision: 1.10 $
 * $Date: 2005/08/27 21:30:28 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.intellij.plugins.junit.config.ListConfig;
import org.intellij.plugins.junit.ClassPattern;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassPatternConfig extends ListConfig {

    public transient ClassPattern testPattern;

    public static ClassPatternConfig getInstance(Project project) {
        return (ClassPatternConfig) project.getComponent(ClassPatternConfig.class);
    }

    public ClassPatternConfig(Project project) {
        this(project, new ClassPattern());
    }

    public ClassPatternConfig(Project project, ClassPattern testPattern) {
        super(project);
        this.testPattern = testPattern;
    }

    protected void initOnProjectOpened() {
    }

    protected void updateInternalState() throws ConfigurationException {
        if (testPattern == null) testPattern = new ClassPattern();
        migrateTestPattern();
        updateTestPattern();
    }

    private void updateTestPattern() throws ConfigurationException {
        try {
            testPattern.reset(getClassPatternDesc());
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException(e.getMessage());
        }
    }

    private void migrateTestPattern() {
        getClassPatternDesc().testedClassPattern = ClassPattern.migrate(getClassPatternDesc().testedClassPattern);
        getClassPatternDesc().testClassPattern = ClassPattern.migrate(getClassPatternDesc().testClassPattern);
    }

    public String getComponentName() {
        return "ClassPatternConfiguration";
    }

    protected List checkItems(List items) {
        if (items == null) items = new ArrayList();
        if (items.isEmpty()) items.add(new ClassPatternDesc());
        return items;
    }

    protected Element getXMLForItem(Object elt) throws WriteExternalException {
        ClassPatternDesc desc = (ClassPatternDesc) elt;
        return desc.writeXML();
    }

    protected Object getItemFromXML(Element itemElt) throws InvalidDataException {
        ClassPatternDesc desc = new ClassPatternDesc();
        desc.readXML(itemElt);
        return desc;
    }

    protected String getXMLRootElement() {
        return "testPatterns";
    }

    public ClassPatternDesc getClassPatternDesc() {
        if (getItems() == null || getItems().isEmpty()) {
            addItem(new ClassPatternDesc());
        }
        return (ClassPatternDesc) getItems().get(0);
    }

    public ClassPattern getDefaultTestPattern() {
        return getClassPatternDesc().getClassPattern();
    }

    public void setTestPattern(ClassPattern testPattern) {
        this.testPattern = testPattern;
    }

    public List getTestPatterns() {
        List items = getItems();
        List result = new ArrayList();
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            ClassPatternDesc classPatternDesc = (ClassPatternDesc) iterator.next();
            result.add(classPatternDesc.getClassPattern());
        }
        if (result.isEmpty()) result.add(testPattern);
        return result;
    }
}
