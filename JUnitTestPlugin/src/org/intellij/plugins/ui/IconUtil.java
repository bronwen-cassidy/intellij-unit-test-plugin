/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/ui/IconUtil.java,v 1.3 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.3 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public class IconUtil {

   public static final Icon DEFAULT_ICON = getDefaultIcon();

   /**
    * Gets an icon either via the class loader, or from a url (maybe a file). <p> To keep the peace, it will always
    * return <i>some</i> sort of icon even if it has to build one on-the-fly.
    *
    * @param  path
    * @return                     An Icon almost guaranteed to be usable.
    */
   public static Icon getIcon(String path) {
      if (path == null) return DEFAULT_ICON;
      URL url = IconUtil.class.getResource(path);      // pull icon from jar first
      if (url == null) {
         try {
            url = new URL(path);    // now try from a url
         } catch (MalformedURLException e) {
            return DEFAULT_ICON;
         }
      }

      Icon icon = new ImageIcon(url);
      if (icon.getIconWidth() < 0 || icon.getIconHeight() < 0) {
         return DEFAULT_ICON;
      }

      return icon;
   }

   private static Icon getDefaultIcon() {
      BufferedImage bi = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB_PRE);
      Graphics2D g2 = bi.createGraphics();
      g2.setBackground(Color.red);
      g2.clearRect(0, 0, bi.getWidth(), bi.getHeight());
      g2.setColor(Color.white);
      g2.setStroke(new BasicStroke(2));
      GeneralPath x = new GeneralPath();
      x.moveTo(0, 0);
      x.lineTo(bi.getWidth() - 1, bi.getHeight() - 1);
      x.moveTo(0, bi.getHeight() - 1);
      x.lineTo(bi.getWidth() - 1, 0);
      g2.draw(x);
      return new ImageIcon(bi);
   }

   public static Color parseColor(String rgba) {
      int red = 0, green = 0, blue = 0, alpha = 128;
      StringTokenizer t = new StringTokenizer(rgba);
      try {
         if (t.hasMoreTokens()) red = nextSample(t);
         if (t.hasMoreTokens()) green = nextSample(t);
         if (t.hasMoreTokens()) blue = nextSample(t);
         if (t.hasMoreTokens()) alpha = nextSample(t);
      } catch (NumberFormatException nfe) {
      }
      return new Color(red, green, blue, alpha);
   }

   private static int nextSample(StringTokenizer t) {
      return Math.min(Math.abs(Integer.valueOf(t.nextToken()).intValue()), 255);
   }

   public static String encodeColor(Color color) {
      return color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " " + color.getAlpha();
   }

}
