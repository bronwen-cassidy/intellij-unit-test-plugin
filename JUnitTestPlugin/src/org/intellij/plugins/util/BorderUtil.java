
/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/BorderUtil.java,v 1.6 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.6 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BorderUtil
{
    public static TitledBorder newTitledBorder(String s)
    {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), s);
    }

    public static Border newEtchedBorder()
    {
        return BorderFactory.createEtchedBorder();
    }
}
