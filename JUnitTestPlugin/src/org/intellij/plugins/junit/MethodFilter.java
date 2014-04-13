/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodFilter.java,v 1.6 2005/08/20 09:54:27 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/08/20 09:54:27 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodFilter {

   abstract boolean accept(MethodDef method);

   public List filterMethods(MethodDef[] methods) {
      ArrayList filteredMethods = new ArrayList();
      for (int i = 0; i < methods.length; i++) {
         MethodDef method = methods[i];
         if (accept(method))
            filteredMethods.add(method);
      }
      return filteredMethods;
   }

}
