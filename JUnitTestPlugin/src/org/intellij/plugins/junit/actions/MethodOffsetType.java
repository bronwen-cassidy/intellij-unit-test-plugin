/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/actions/MethodOffsetType.java,v 1.2 2005/07/30 18:27:53 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:27:53 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.actions;

import com.intellij.psi.PsiMethod;

public interface MethodOffsetType {

    public static MethodOffsetType NAME_END = new MethodOffsetType() {
        public int getOffset(PsiMethod method) {
            return EditorHelper.getEndOfIdentifierOffset(method);
        }
    };

    public static MethodOffsetType BODY_START = new MethodOffsetType() {
        public int getOffset(PsiMethod method) {
            return EditorHelper.getLeftBraceOffset(method);
        }
    };

    int getOffset(PsiMethod method);
}
