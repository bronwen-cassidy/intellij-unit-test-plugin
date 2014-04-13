/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodNameAtStartOfStringFilter.java,v 1.7 2005/08/20 09:54:27 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/08/20 09:54:27 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

class MethodNameAtStartOfStringFilter extends MethodFilter {
   private String string;

   public MethodNameAtStartOfStringFilter(String string) {
      this.string = string;
   }

   boolean accept(MethodDef method) {
      String other = string.substring(0, 1).toUpperCase() + string.substring(1);
      return string.startsWith(method.getName()) || other.startsWith(method.getName());
   }
}
