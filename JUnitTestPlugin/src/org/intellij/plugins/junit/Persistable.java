/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 12-Jul-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package org.intellij.plugins.junit;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jul-2007 15:04:26
 */
public interface Persistable {

    Object readState();

    void writeState(Object object);
}
