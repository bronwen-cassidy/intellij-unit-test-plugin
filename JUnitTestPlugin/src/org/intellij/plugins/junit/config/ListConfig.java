/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/config/ListConfig.java,v 1.3 2005/08/19 08:47:29 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/19 08:47:29 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.intellij.plugins.util.ListenerNotifier;
import org.jdom.Element;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ListConfig extends Config implements ListenerNotifier, ProjectComponent {

    private PropertyChangeSupport listenerSupport = new PropertyChangeSupport(this);
    protected List items = new ArrayList();

    public ListConfig(Project project) {
        super(project);
        items = new ArrayList();
    }

    public String getComponentName() {
        return getClass().getName();
    }

    public List getItems() {
        // todo return to return items;
        return checkItems(items);
    }

    public void addItem(Object element) {
        items.add(element);
    }

    public void setItems(List newItems) {
        items = new ArrayList(newItems);
        notifyListenersOfChange();
    }

    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        Element itemsElt = element.getChild(getXMLRootElement());
        if (itemsElt != null) {
            for (Iterator iterator = itemsElt.getChildren().iterator(); iterator.hasNext();) {
                Element itemElt = (Element) iterator.next();
                items.add(getItemFromXML(itemElt));
            }
        }
    }

    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        Element itemsElt = new Element(getXMLRootElement());
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            Object elt = iterator.next();
            Element forItem = getXMLForItem(elt);
            if (forItem != null) {
                itemsElt.addContent(forItem);
            }
        }
        element.addContent(itemsElt);
    }

    // todo remove please!
    protected abstract List checkItems(List items);

    protected abstract Element getXMLForItem(Object elt) throws WriteExternalException;

    protected abstract Object getItemFromXML(Element itemElt) throws InvalidDataException;

    protected abstract String getXMLRootElement();

    public PropertyChangeListener[] getListeners() {
        return listenerSupport.getPropertyChangeListeners();
    }

    public void addListener(PropertyChangeListener listener) {
        listenerSupport.addPropertyChangeListener(listener);
    }

    public void notifyListenersOfChange() {
        listenerSupport.firePropertyChange("configuration", null, this);
    }

    public void removeListener(PropertyChangeListener listener) {
        listenerSupport.removePropertyChangeListener(listener);
    }
}