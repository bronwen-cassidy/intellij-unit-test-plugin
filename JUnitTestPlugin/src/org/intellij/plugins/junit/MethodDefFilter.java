/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodDefFilter.java,v 1.1 2005/07/30 22:04:01 shadow12 Exp $
 * $Revision: 1.1 $
 * $Date: 2005/07/30 22:04:01 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import java.util.ArrayList;
import java.util.List;

public abstract class MethodDefFilter {

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
