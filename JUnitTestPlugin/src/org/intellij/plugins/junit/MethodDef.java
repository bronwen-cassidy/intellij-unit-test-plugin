/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/MethodDef.java,v 1.2 2005/08/20 16:55:55 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/08/20 16:55:55 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiMethod;

public class MethodDef {
   private String name;
   private PsiMethod psiMethod;
   private ClassDef classDef;
   
   public MethodDef(String name) {
      this.name = name;
   }

   public MethodDef(PsiMethod psiMethod) {
      this(psiMethod.getName());
      this.psiMethod = psiMethod;
   }

   public MethodDef(ClassDef classDef, PsiMethod psiMethod) {
      this(psiMethod);
      this.classDef = classDef;
   }

   public void setClassDef(ClassDef classDef) {
      this.classDef = classDef;
   }

   public String getName() {
      return name;
   }

   public PsiMethod getPsiMethod() {
      return psiMethod;
   }

   public void setPsiMethod(PsiMethod psiMethod) {
      this.psiMethod = psiMethod;
   }

   public ClassDef getClassDef() {
      return classDef;
   }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof MethodDef)) return false;

      final MethodDef methodDef = (MethodDef) o;

      if (classDef != null ? !classDef.equals(methodDef.classDef) : methodDef.classDef != null) return false;
      if (!name.equals(methodDef.name)) return false;
      if (psiMethod != null ? !psiMethod.equals(methodDef.psiMethod) : methodDef.psiMethod != null) return false;

      return true;
   }

   public int hashCode() {
      int result;
      result = name.hashCode();
      result = 29 * result + (psiMethod != null ? psiMethod.hashCode() : 0);
      result = 29 * result + (classDef != null ? classDef.hashCode() : 0);
      return result;
   }


   public String toString() {
      return "MethodDef{" +
             "name='" + name + "'" +
             ", psiMethod=" + psiMethod +
             ", classDef=" + classDef +
             "}";
   }
}