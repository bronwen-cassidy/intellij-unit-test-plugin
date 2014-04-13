/*
 * $Header: /cvsroot/junitideas/JUnitTestPlugin/src/org/intellij/plugins/junit/config/ClassPatternConfigEditor.java,v 1.8 2005/07/30 18:27:54 shadow12 Exp $
 * $Revision: 1.8 $
 * $Date: 2005/07/30 18:27:54 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.config;

import org.intellij.plugins.ui.common.HtmlLabel;
import org.intellij.plugins.util.JPanelBuilder;

import javax.swing.*;

public class ClassPatternConfigEditor {

    private ClassPatternPanel patternsPanel;

    public ClassPatternConfigEditor(ClassPatternPanel patternsPanel) {
        this.patternsPanel = patternsPanel;
    }

    public JComponent createEditorPanel() {
        JPanelBuilder patternsBuilder = new JPanelBuilder();
        patternsBuilder.add(patternsPanel);
        JLabel help = new HtmlLabel("Help",
                "<b>$CLASS$</b> denotes the name of a class file<p>" +
                        "<b>$DIRECTORY$</b> denotes the directory path of a class file<p>" +
                        "<center><table border=\"1\" cellspacing=\"1\" cellpadding=\"0\">" +
                        "<tr><th  bgcolor=\"#99CCCC\"> Tested Class Path <br> = <br>Tested Class Pattern  </th><th  bgcolor=\"#99CCCC\"> Test Class Path <br> = <br> Test Class Pattern  </th></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/src/org/intellij/plugin/Class <br> = <br> $CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/src/org/intellij/plugin/Class<b>Test</b><br> = <br>$CLASS$Test   </td></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/src/org/intellij/plugin/Class <br> = <br> $CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/src/org/intellij/plugin/<b>Test</b>Class<br> = <br>Test$CLASS$   </td></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/src/org/intellij/plugin/Class <br> = <br> $CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/src/org/intellij/plugin/<b>My</b>Class<b>Test</b><br> = <br>My$CLASS$Test   </td></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/src/org/intellij/plugin/Class <br> = <br> $CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/src/org/intellij/plugin/<b>test</b>/ClassTest<br> = <br>test/$CLASS$Test   </td></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/<b>src</b>/org/intellij/plugin/Class <br> = <br> src/$DIRECTORY$/$CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFFF\"> c:/proj/<b>test</b>/org/intellij/plugin/ClassTest<br> = <br>test/$DIRECTORY$/$CLASS$Test   </td></tr>" +
                        "<tr><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/<b>src/org/intellij</b>/plugin/Class <br> = <br> src/org/intellij/$DIRECTORY$/$CLASS$   </td><td  align=\"center\" bgcolor=\"#FFFFCC\"> c:/proj/<b>test</b>/plugin/ClassTest<br> = <br>test/$DIRECTORY$/$CLASS$Test   </td></tr>" +
                        "</table></center>");

        patternsBuilder.add(help);
        return patternsBuilder.getPanel();
    }

    protected void reset() {
        patternsPanel.reset();
    }

    protected void apply() {        
        patternsPanel.apply();
    }

    public boolean isModified() {
        return patternsPanel.isModified();
    }

    public String getComponentName() {
        return "ClassPatternConfigEditor";
    }
}
