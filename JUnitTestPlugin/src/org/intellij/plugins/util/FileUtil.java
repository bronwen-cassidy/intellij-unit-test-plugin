/*
 * $Header: /cvsroot/junitideas/PluginUtil/src/org/intellij/plugins/util/FileUtil.java,v 1.7 2005/07/30 18:25:49 shadow12 Exp $
 * $Revision: 1.7 $
 * $Date: 2005/07/30 18:25:49 $
 *
 * Copyright (c) 1999-2004 Jacques Morel.  All rights reserved.
 * Released under the Apache Software License, Version 1.1
 */
package org.intellij.plugins.util;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;


public class FileUtil {
   private CommandUtil commandUtil;

   public FileUtil() { this(new CommandUtil());}

   public FileUtil(CommandUtil commandUtil) {
      this.commandUtil = commandUtil;
   }

   public VirtualFile ioFileToVirtualFile(File file) {
/*@if Aurora@*/
      return LocalFileSystem.getInstance().findFileByIoFile(file);
/*@else@
        return LocalFileSystem.getInstance().findFileByPath(file.getAbsolutePath().replace(File.separatorChar, '/'));
  @end@*/
   }

   public void setFileWritability(final VirtualFile file,
                                  boolean writable) throws IOException {
      if (!(file.getFileSystem() instanceof LocalFileSystem)) {
         throw new IllegalArgumentException("Wrong file system: " + file.getFileSystem());
      }
      if (file.isWritable() != writable) {
         String s = file.getPresentableUrl();
         setFileWritability(s, writable);
         commandUtil.runWriteActionWithoutException(new CommandUtil.Command() {
            public Object run() throws Exception {
               file.refresh(false, false);
               return null;
            }
         });
      }
   }

   public void setFileWritability(String path,
                                  boolean writable) throws IOException {
      Process process;
      if (SystemInfo.isWindows) {
         process = Runtime.getRuntime().exec(new String[]{
            "attrib", writable ? "-r" : "+r", path
         });
      } else {
         process = Runtime.getRuntime().exec(new String[]{
            "chmod", writable ? "u+w" : "u-w", path
         });
      }
      try {
         process.waitFor();
      } catch (InterruptedException interruptedexception) {
      }
   }


    /**
     * Reads the specified file and returns the read value as a string.
     * Usefull while debugging etc.
     */
    public static String readFileIntoString(File file) throws IOException {
        byte bytes[];
        // open the file for reading.
        FileInputStream istream = new FileInputStream(file);

        // allocate sufficient buffer.
        bytes = new byte[istream.available()];

        // read the content into the buffer.
        istream.read(bytes);

        // close the file.
        istream.close();

        // return the string value.
        return new String(bytes);
    }

    /**
     * write a String to a file
     */
    public static void writeStringIntoFile(String s, File file) throws IOException
    {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(s);
            writer.write("\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    /**
     * Rename is a big headache in Java.  java does not throw any exception while doing rename and that makes things
     * really messy as to what exactly is happening. After going through a list of forums and faqs the following code
     * is what most of the people are running. So probably worth a try.
     *
     * If not I will just copy the source and content.
     */
    public static final void rename(File oldfile, File newfile) throws IOException {
        if( !oldfile.exists() ) {
            throw new IOException("File " + oldfile + " does not exist ");
        }

        if( newfile.exists() && !newfile.delete() ) {
            if( newfile.exists() ) throw new IOException("Cannot delete file " + newfile);
        }

        if( ! oldfile.renameTo(newfile) && !newfile.exists()) {
            // the biggest hack of all time :)
            System.gc();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }

            if(! oldfile.renameTo(newfile) && !newfile.exists()) { // give it another try.
                // give me a break, will ya.
                String data = readFileIntoString(oldfile);
                writeStringIntoFile(data, newfile);
                oldfile.delete();
                // if this doesn't work... god help me
            }
        }
    }


    /**
     * Deletes a file from the file system. this takes care of all the gotchas the file system has in there.
     *
     */

    public static final void delete(File file) {
        if( !file.exists() )
            return;

        // try to delete it first.
        if( !file.delete() ) {
            // ok may be there is some problem in the file handles etc. so
            // do a GC and sleep for some time.
            System.gc();

            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
            }

            // check if you can delete the file now.
            if( ! file.delete() ){

                // nope. then ok let java delete it when exiting the system.
                file.deleteOnExit();
            }
        }
    }

    /**
     * Copy a file from the file "from" to the "to" file. The actually reads the content of the file and write out
     * the data one by one.
     *
     * @param from The file from which to copy.
     * @param to The file to which to copy.
     * @throws IOException If there is any problem while reading or wrting the files.
     */
    public static void copyFile(String from, String to) throws IOException {
        File inputFile = new File(from);
        File outputFile = new File(to);

        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);

        char buf[] = new char[2048];
        int  count = -1;

        while ( (count = in.read(buf))!= -1 ) {
            out.write(buf,0,count);
        }

        in.close();
        out.close();
    }
}
