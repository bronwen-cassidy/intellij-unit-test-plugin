/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/openapi/beans/PluginContext.java,v 1.5 2005/10/19 08:19:05 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/10/19 08:19:05 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.beans;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.intellij.plugins.util.LogUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PluginContext {

    public static final String SELENA_PLUGIN_CONTEXT_PATH = "/META-INF/pluginContextSelena.xml";
    public static final String PLUGIN_CONTEXT_PATH = "/META-INF/pluginContext.xml";
    public static final String DEFAULT_PLUGIN_PATH = "/META-INF/plugin.xml";
    public static final Logger LOG = LogUtil.getLogger();

    private Project project;
    private DefaultListableBeanFactory factory;
    private Application application;
    private Class[] projectBeanClasses = new Class[0];
    private Class[] applicationBeanClasses = new Class[0];
    private static boolean isApplicationBeansInited;

    public PluginContext(final Project project) throws BeansException {

        String versionedPluginContext = getVersionedPluginContext();
        init(ApplicationManager.getApplication(),
                project,
                PluginContext.class.getResourceAsStream(DEFAULT_PLUGIN_PATH),
                PluginContext.class.getResourceAsStream(PLUGIN_CONTEXT_PATH),
                PluginContext.class.getResourceAsStream(versionedPluginContext));
    }

    public PluginContext(final Application application,
                         final Project project,
                         final InputStream pluginConfig,
                         final InputStream pluginContextConfig, InputStream versionPluginContext) throws BeansException {
        init(application, project, pluginConfig, pluginContextConfig, versionPluginContext);
    }

    public PluginContext(final Project project,
                         final String pluginConfig,
                         final String pluginContextConfig) throws BeansException {
        String versionedPluginContext = getVersionedPluginContext();
        init(ApplicationManager.getApplication(), project, PluginContext.class.getResourceAsStream(pluginConfig),
                PluginContext.class.getResourceAsStream(pluginContextConfig),
                PluginContext.class.getResourceAsStream(versionedPluginContext));
    }

    private void init(Application application, Project project, InputStream pluginConfig, InputStream pluginContext, InputStream versionedPluginContext) {
        this.application = application;
        this.project = project;
        initPluginComponentBeanClasses(pluginConfig);
        initContext(pluginContext, versionedPluginContext);
        refresh();
    }

    private String getVersionedPluginContext() {                
        return SELENA_PLUGIN_CONTEXT_PATH;
    }

    private void initContext(final InputStream contextConfig, final InputStream versionedContextConfig) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.setBeanClassLoader(this.getClass().getClassLoader());
        registerIDEABeans(beanFactory);
        registerPluginBeans(beanFactory);
        registerPluginContextBeans(reader, contextConfig, versionedContextConfig);
        this.factory = beanFactory;
    }

    private void initPluginComponentBeanClasses(final InputStream pluginConfig) {
        if (pluginConfig == null) return;

        PluginConfigParser parser = createPluginConfigParser(pluginConfig);
        this.projectBeanClasses = parser.getProjectComponentClasses();
        this.applicationBeanClasses = parser.getApplicationComponentClasses();
    }

    private void refresh() {
        autoWireBeans(projectBeanClasses);
        if (!isApplicationBeansInited) {
            autoWireBeans(applicationBeanClasses);
            isApplicationBeansInited = true;
        }
    }

    private void autoWireBeans(Class[] beanClasses) {
        for (int i = 0; i < beanClasses.length; i++) {
            Class beanClass = beanClasses[i];
            Object bean = factory.getBean(beanClass.getName());
            factory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        }
    }

    private void registerIDEABeans(ConfigurableListableBeanFactory beanFactory) {
        registerComponents(beanFactory, getComponents(application, project));
    }

    private void registerComponents(ConfigurableListableBeanFactory beanFactory, Collection components) {
        for (Iterator it = components.iterator(); it.hasNext();) {
            Object component = it.next();
            try {
                LOG.debug("register " + component.getClass().getName());
                beanFactory.registerSingleton(component.getClass().getName(), component);
            } catch (BeanDefinitionStoreException e) {
                //Catch duplicate components because of the default project
            }
        }
    }

    private void registerPluginContextBeans(XmlBeanDefinitionReader reader, InputStream contextConfig, InputStream versionedContextConfig) {
        if (versionedContextConfig == null && contextConfig == null) return;
        if (versionedContextConfig != null)
            reader.loadBeanDefinitions(new InputStreamResource[]{new InputStreamResource(versionedContextConfig), new InputStreamResource(contextConfig)});
        else reader.loadBeanDefinitions(new InputStreamResource(contextConfig));
    }

    private void registerPluginBeans(ConfigurableListableBeanFactory beanFactory) {
        registerComponents(beanFactory, getComponents(project, projectBeanClasses));
        registerComponents(beanFactory, getComponents(project, applicationBeanClasses));
    }

    protected PluginConfigParser createPluginConfigParser(InputStream config) {
        return new PluginConfigParser(config);
    }

    public Object getBean(String name) throws BeansException {
        try {
            return factory.getBean(name);
        } catch (BeansException e) {
            throw(e);
        }
    }

    public Object getBean(Class clazz) throws BeansException {
        return getBean(clazz.getName());
    }

    public static List getComponents(Application application, Project project) {

        List list = getComponents(project);
        list.addAll(getComponents(application));
        list.add(CodeStyleManager.getInstance(project));
        list.add(JavaCodeStyleManager.getInstance(project));
        list.add(PsiManager.getInstance(project));
        list.add(ProjectRootManager.getInstance(project));
        list.add(LocalFileSystem.getInstance());
        list.add(FileEditorManager.getInstance(project));
        list.add(PsiDocumentManager.getInstance(project));
        return list;
    }

    private static List getComponents(ComponentManager mgr) {
        //getComponents
        Object[] componentClasses = mgr.getComponents(Object.class);
        List list = getComponents(mgr, componentClasses);
        list.add(mgr);
        return list;
    }

    private static List getComponents(ComponentManager mgr, Object[] componentClasses) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < componentClasses.length; i++) {
            Object component = mgr.getComponent(componentClasses[i].getClass());
            if (component != null) list.add(component);
        }
        return list;
    }

    public DefaultListableBeanFactory getFactory() {
        return factory;
    }
}
