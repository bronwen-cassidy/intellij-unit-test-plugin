/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/CollectionsUtil.java,v 1.4 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.4 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 14, 2003
 * Time: 1:05:03 AM
 * To change this template use Options | File Templates.
 */
public class CollectionsUtil {

    static public List intersection(Object[] array1, Object[] array2) {
        ArrayList result = new ArrayList();
        if (array1 != null && array2 != null) {
            for (int i = 0; i < array1.length; i++) {
                Object o1 = array1[i];
                for (int j = 0; j < array2.length; j++) {
                    Object o2 = array2[j];
                    if (o2.equals(o1)) {
                        result.add(o1);
                    }
                }
            }
        }
        return result;
    }
}
