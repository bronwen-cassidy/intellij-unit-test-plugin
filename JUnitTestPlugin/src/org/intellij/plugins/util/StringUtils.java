package org.intellij.plugins.util;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: 1.1 $
 *          $Id: StringUtils.java,v 1.1 2005/08/25 05:56:16 shadow12 Exp $
 */
public class StringUtils {

   public static String fixLineBreaks(String s) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if (Character.LINE_SEPARATOR != c)
            sb.append(c);
      }

      return sb.toString();
   }
}
