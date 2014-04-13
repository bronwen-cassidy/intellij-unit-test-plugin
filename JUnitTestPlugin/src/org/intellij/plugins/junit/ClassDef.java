/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/ClassDef.java,v 1.6 2005/09/26 19:07:22 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/09/26 19:07:22 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.diagnostic.Logger;

public class ClassDef {

    PsiDirectory root;
    String packageName;
    String name;
    PsiClass psiClass;
    String filePath;

    public ClassDef(PsiDirectory root, String packageName, String name) {
        this.root = root;
        this.packageName = packageName;
        this.name = name;
        this.filePath = root.getVirtualFile().getPath() + '/' +
                packageName.replace('.', '/') + '/' +
                name + ".java";
    }

    public ClassDef(PsiClass psiClass) {
        try {
            this.name = psiClass.getName();
            this.psiClass = psiClass;
            PsiFile containingFile = psiClass.getContainingFile();
            if (containingFile != null) {
                this.filePath = containingFile.getVirtualFile().getPath();
                this.packageName = ((PsiJavaFile) containingFile).getPackageName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassDef)) return false;

        final ClassDef classDef = (ClassDef) o;

        if (name != null ? !name.equals(classDef.name) : classDef.name != null) return false;
        if (packageName != null ? !packageName.equals(classDef.packageName) : classDef.packageName != null) return false;
        if (root != null ? !root.equals(classDef.root) : classDef.root != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (root != null ? root.hashCode() : 0);
        result = 29 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return getFilePath();
    }

    public PsiDirectory getRoot() {
        return root;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
    }

    private static final Logger LOG = Logger.getInstance(ClassDef.class.getName());
}