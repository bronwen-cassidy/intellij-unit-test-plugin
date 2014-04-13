/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodNameStartsWithPrefixFilter.java,v 1.5 2005/08/20 09:54:27 shadow12 Exp $
 * $Revision: 1.5 $
 * $Date: 2005/08/20 09:54:27 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

//DEBT TO be deleted
class MethodNameStartsWithPrefixFilter extends MethodFilter {
   private String prefix;

   public MethodNameStartsWithPrefixFilter(String prefix) {
      this.prefix = prefix;
   }

   public boolean accept(MethodDef method) {
      return method.getName().startsWith(prefix);
   }
}
