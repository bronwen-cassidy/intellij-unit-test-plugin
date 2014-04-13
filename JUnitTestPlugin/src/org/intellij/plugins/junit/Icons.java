/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/Icons.java,v 1.8 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.8 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import org.intellij.plugins.ui.IconUtil;

import javax.swing.*;

public interface Icons {

   public static final Icon JUNIT_TEST_PLUGIN = IconUtil.getIcon("/icons/junit.gif");
   public static final Icon METHOD_NOT_TESTED = IconUtil.getIcon("/icons/goToTestRedPlus.png");
   public static final Icon EDIT_TEMPLATE_ACTION = IconUtil.getIcon("/icons/template.png");
   public static final Icon SELECT_METHOD_ACTION = IconUtil.getIcon("/icons/method.png");
}