/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/common/HtmlLabel.java,v 1.2 2005/07/30 18:25:48 shadow12 Exp $
 * $Revision: 1.2 $
 * $Date: 2005/07/30 18:25:48 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui.common;

import org.intellij.plugins.util.BorderUtil;

import javax.swing.*;

import java.awt.*;

public class HtmlLabel extends JLabel {

    public HtmlLabel(String title, String htmlHelp) {
        super("<html>" + htmlHelp + "</html>");
        Font font = getFont();
        setFont(font.deriveFont(Font.PLAIN));
        setBorder(BorderUtil.newTitledBorder(title));
    }
}