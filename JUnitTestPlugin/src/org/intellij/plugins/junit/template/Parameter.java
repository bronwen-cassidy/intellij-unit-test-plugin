/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/template/Parameter.java,v 1.3 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.template;

import com.intellij.psi.PsiParameter;

/**
 * User: Jacques
 * Date: Oct 12, 2003
 * Time: 12:58:00 AM
 */
public class Parameter {

    private String type;
    private String name;

    public Parameter(PsiParameter param) {
        this.type = param.getType().getPresentableText();
        this.name = param.getName();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
