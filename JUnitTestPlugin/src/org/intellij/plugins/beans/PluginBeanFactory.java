/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/openapi/beans/PluginBeanFactory.java,v 1.1 2005/08/01 06:11:16 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/08/01 06:11:16 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.beans;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.intellij.plugins.util.LogUtil;
import org.intellij.plugins.beans.PluginContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class PluginBeanFactory implements ProjectComponent {

    final Project project;
    private PluginContext context;

    public static final Logger LOG = LogUtil.getLogger();

    public PluginBeanFactory(Project project) {
        this.project = project;
    }

    public String getComponentName() {
        return "PluginContext";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void projectOpened() {
        StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
            public void run() {
                getContext();
            }
        });
    }

    public synchronized PluginContext getContext() {
        if (context == null) {
            context = new PluginContext(project);
        }
        return context;
    }

    public void projectClosed() {
    }

    public Object getBean(String name) throws BeansException {
        try {
            return getContext().getBean(name);
        } catch (BeansException e) {
            if (LOG.isDebugEnabled()) LOG.debug(e);
            return null;
        }
    }

    public Object getBean(Class clazz) throws BeansException {
        try {
            return BeanFactoryUtils.beanOfType(getContext().getFactory(), clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        } catch (BeansException e) {
            if (LOG.isDebugEnabled()) LOG.debug(e);
            throw e;
        }
    }

    public void autowireBean(Object bean) {
        ConfigurableListableBeanFactory factory = getContext().getFactory();
        factory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
    }

    public static PluginBeanFactory getInstance(Project project) {
        if (project == null) return null;
        return project.getComponent(PluginBeanFactory.class);
    }

    public static Object getBean(Project project, Class clazz) throws BeansException {
        PluginBeanFactory instance = getInstance(project);
        if (instance == null) return null;
        return instance.getBean(clazz);
    }

    public static Object getBean(Project project, String name) throws BeansException {
        PluginBeanFactory instance = getInstance(project);
        if (instance == null) return null;
        return instance.getBean(name);
    }

    public static void autowireBean(Project project, Object bean) throws BeansException {
        PluginBeanFactory instance = getInstance(project);
        if (instance != null) {
            instance.autowireBean(bean);
        }
    }
}
