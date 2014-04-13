/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/adaptor/JDomAdaptor.java,v 1.1 2005/08/25 05:56:16 shadow12 Exp ${}
 * $Revision: 1.1 ${}
 * $Date: 22-Aug-2005
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.junit.adaptor;

import org.jdom.Document;

import java.io.Writer;
import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Aug-2005 05:24:45
 */
public interface JDomAdaptor {

   void writeDocument(Document document, Writer writer, String lineSeparator) throws IOException;
}
