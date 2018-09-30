package com.revature.bank.dbs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class FileIO {

    private static String pathway = "/home/developer/Workspace/project-0-ddavaloo24/src/com/revature/bank/dbs/";

    public static void write(String fileName, String pattern) {

        String fullFileName = pathway + fileName;

        try( FileWriter fw = new FileWriter(fullFileName, true) ) {
            fw.append(pattern);
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public static boolean lookupLogin(String fileName, String pattern) {

        String str = null;
        String fullFileName = pathway + fileName;
        File file = new File(fullFileName);

        if(!file.exists()) {
            return false;
        }

        try( Scanner sc = new Scanner(file) ) {

            while( sc.hasNextLine() ) {

                str = sc.findInLine(pattern);

                if( str != null ) {
                    return true;
                }

                sc.nextLine();

            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return false;
    }


    public static <T> T deSerialize(String fileName, Class<T> type) {

        String fullFileName = pathway + fileName;

        File file = new File(fullFileName);

        try {

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();

            ois.close();
            return (T) obj;

        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    

	public static void serialize(String fileName, Object obj) {

        String fullFileName = pathway + fileName;

        try {

            FileOutputStream fos = new FileOutputStream(fullFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(obj);
            oos.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
	}
}