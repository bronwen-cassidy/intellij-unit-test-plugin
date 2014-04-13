/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package org.intellij.plugins.util;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 12-Sep-2009 16:19:46
 * @version 0.1
 */

import junit.framework.*;
import org.intellij.plugins.util.FilePathUtil;

public class TestFilePathUtil extends TestCase {

    public void testRemoveExtension() throws Exception {
        assertEquals("Test", FilePathUtil.removeExtension("Test.java"));
        assertEquals("Test", FilePathUtil.removeExtension("Test"));
        assertEquals("c:/test.3/Test", FilePathUtil.removeExtension("c:/test.3/Test.java"));
        assertEquals("c:/test.3/test.3.2/Test", FilePathUtil.removeExtension("c:/test.3/test.3.2/Test.java"));
        assertEquals("c:/test.3/test.3.2/Test.3", FilePathUtil.removeExtension("c:/test.3/test.3.2/Test.3.java"));
        assertEquals("c:/test.3/Test", FilePathUtil.removeExtension("c:/test.3/Test"));
    }

    public void testReplaceFileSeparator() throws Exception {
        assertEquals("com.test", FilePathUtil.replaceFileSeparator("com/test", '.'));
        assertEquals("com.test", FilePathUtil.replaceFileSeparator("com\\test", '.'));
    }
}