/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/adaptor/RefactoringAdaptor.java,v 1.3 2005/08/21 17:00:33 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/08/21 17:00:33 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.adaptor;

import com.intellij.psi.PsiNamedElement;
import org.intellij.plugins.junit.TestClassDef;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 30-Jul-2005 07:55:43
 */
public interface RefactoringAdaptor {

   void rename(PsiNamedElement[] elementToBeRenamed, String[] newName);
   void moveClasses(TestClassDef[] testClassDefs);
}
