package com.revature.bank.dbs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static boolean lookup(String fileName, String pattern) {

        String str = null;
        String fullFileName = pathway + fileName;

        try( Scanner sc = new Scanner(fullFileName) ) {

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
}