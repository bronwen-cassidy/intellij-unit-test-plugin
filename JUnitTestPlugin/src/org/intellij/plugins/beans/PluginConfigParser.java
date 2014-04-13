/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/openapi/beans/PluginConfigParser.java,v 1.1 2005/08/01 06:11:16 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/08/01 06:11:16 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.beans;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.JDOMUtil;
import org.intellij.plugins.util.LogUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 14, 2003
 * Time: 10:33:50 PM
 * To change this template use Options | File Templates.
 */
public class PluginConfigParser {

    private InputStream configStream;
    private Class[] projectComponentClasses;
    private Class[] applicationComponentClasses;
    protected static final Logger LOG = LogUtil.getLogger();

    public PluginConfigParser(InputStream pluginConfigStream) {
        this.configStream = pluginConfigStream;
        try {
            parse();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document createDocument(InputStream pluginConfig) throws JDOMException, IOException {
        return JDOMUtil.loadDocument(pluginConfig);
    }

    public Class[] getProjectComponentClasses() {
        return projectComponentClasses;
    }

    public Class[] getApplicationComponentClasses() {
        return applicationComponentClasses;
    }

    public void parse() throws JDOMException, IOException {
        Document document = createDocument(configStream);
        Element root = document.getRootElement();
        projectComponentClasses = createClassArray(parseComponentClasses(root, "project-components"));
        applicationComponentClasses = createClassArray(parseComponentClasses(root, "application-components"));
    }

    private static Class[] createClassArray(Collection classes) {
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    private Set parseComponentClasses(Element parentElement, String componentListElementName) {
        List projectComponentsElts = parentElement.getChildren(componentListElementName);
        Set componentClasses = new HashSet();
        for (int i = 0; i < projectComponentsElts.size(); i++) {
            Element element = (Element) projectComponentsElts.get(i);
            List componentElts = element.getChildren("component");
            for (Iterator iterator = componentElts.iterator(); iterator.hasNext();) {
                Element componentElt = (Element) iterator.next();
                List implementations = componentElt.getChildren("implementation-class");
                if (implementations.size() > 0) {
                    Element implementation = (Element) implementations.get(0);
                    Class clazz = getClassForName(implementation.getTextTrim());
                    if (clazz != null) componentClasses.add(clazz);
                }
            }
        }
        log(componentClasses, componentListElementName);
        return componentClasses;
    }

    private void log(Set componentClasses, String componentListElementName) {
        if (LOG.isDebugEnabled()) {
            String message = "found " + componentClasses.size() + " " + componentListElementName + ":[";
            boolean first = true;
            for (Iterator iterator = componentClasses.iterator(); iterator.hasNext();) {
                if (!first) {
                    message += ", ";
                } else {
                    message += "\n";
                    first = false;
                }
                message += ((Class) iterator.next()).getName();
            }
            message += "]";
            LOG.debug(message);
        }
    }

    private Class getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
