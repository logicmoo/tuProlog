/*
 *   Tools.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package alice.util;
import java.io.*;


public class Tools extends Object {

    public static String loadText(String fileName) throws IOException {
        //FileInputStream is=new FileInputStream(fileName);
        try {
            BufferedInputStream is = new BufferedInputStream(ClassLoader.getSystemResourceAsStream(fileName));
            byte[] info=new byte[is.available()];
            is.read(info);
            return new String(info);
        } catch (Exception ex){
        }
        // resource not found among system resources: try as a file
        try {
            FileInputStream is = new FileInputStream(fileName);
            byte[] info=new byte[is.available()];
            is.read(info);
            is.close(); // ED 2013-05-21
            return new String(info);
        } catch (Exception ex){
        }
        throw new IOException("File not found.");
    }

    
    public static String loadText(InputStream is) throws IOException {
        byte[] info=new byte[is.available()];
        is.read(info);
        return new String(info);
    }

    
    public static String getOpt(String[] args,String prefix){
        for (int i=0; i<args.length; i++)
            if (args[i].equals(prefix)){
                return args[i+1];
            }
        return null;
    }

    
    public static boolean isOpt(String[] args,String prefix){
        for (int i=0; i<args.length; i++)
            if (args[i].equals(prefix)){
                return true;
            }
        return false;
    }
    
    public static String removeApices(String st){
        if (st.startsWith("'")&&st.endsWith("'")){
            return st.substring(1,st.length()-1);
       } else {
           return st;
       }
    }
}
