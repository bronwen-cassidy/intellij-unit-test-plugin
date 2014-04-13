/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/TemplateSelectorCreator.java,v 1.1 2005/07/31 08:45:15 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/07/31 08:45:15 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.IncorrectOperationException;
import org.intellij.plugins.junit.config.TemplateConfig;
import org.intellij.plugins.ui.MessageHandler;
import org.intellij.plugins.ui.SelectDialog;
import org.intellij.plugins.ui.SelectPanel.ChoiceListener;
import org.intellij.plugins.util.CommandUtil;
import org.intellij.plugins.util.LogUtil;

public class TemplateSelectorCreator {

    protected static final Logger LOG = LogUtil.getLogger();

    public interface ElementCreationListener {

        void elementCreated(Object createdElement);
    }

    public static abstract class TemplateInstantiationCommand extends CommandUtil.NamedCommand {

        protected String templateName;

        protected TemplateInstantiationCommand(String name) {
            super(name);
        }
    }

    private TemplateConfig templateConfig;
    private SelectDialog selectDialog;
    private CommandUtil commandUtil;
    private MessageHandler messageHandler;

    public TemplateSelectorCreator(Project project,
                                   TemplateConfig templateConfig,
                                   CommandUtil commandUtil,
                                   MessageHandler messageHandler) {
        this.selectDialog = new SelectDialog(project);
        this.templateConfig = templateConfig;
        this.commandUtil = commandUtil;
        this.messageHandler = messageHandler;
    }

    public void selectTemplateAndCreate(final TemplateInstantiationCommand creationCommand,
                                        final ElementCreationListener creationListener) {
        if (isSelectTemplateDialogNecessary()) {
            selectDialog.show(templateConfig.getType(),
                    templateConfig.LAST_TEMPLATE_NAME,
                    templateConfig.getValidTestTemplates(),
                    new ChoiceListener() {
                        public void itemChosen(/*T*/Object choice) {
                            handleChoice((String) choice, creationCommand, creationListener);
                        }
                    });
        } else {
            handleChoice(templateConfig.LAST_TEMPLATE_NAME, creationCommand, creationListener);
        }
    }

    private void handleChoice(String templateName,
                              TemplateInstantiationCommand creationCommand,
                              ElementCreationListener creationListener) {
        Object o = createElement(templateName, creationCommand);
        if (creationListener != null) {
            creationListener.elementCreated(o);
        }
    }

    protected Object createElement(String templateName, TemplateInstantiationCommand creationCommand) {
        Object o = null;
        try {
            LOG.debug("create element with template '" + templateName + "'");
            creationCommand.templateName = templateName;
            o = commandUtil.executeWriteCommand(creationCommand);
            templateConfig.LAST_TEMPLATE_NAME = templateName;
        } catch (IncorrectOperationException e) {
            messageHandler.showErrorDialog(e.getMessage(), "Error while creating method");
        } catch (Exception e) {
            LOG.error(e);
        }
        return o;
    }

    private boolean isSelectTemplateDialogNecessary() {
        return !templateConfig.DONT_SHOW_TEMPLATE_DIALOG_IF_ONLY_ONE_TEMPLATE ||
                templateConfig.getValidTestTemplates().length > 1;
    }
}