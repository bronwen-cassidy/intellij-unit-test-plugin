/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/ClassMethodListAction.java,v 1.5 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import org.intellij.plugins.actions.HandledAction;

public class ClassMethodListAction extends HandledAction {

   protected Class getHandlerClass() {
      return ClassMethodListActionHandler.class;
   }
}
