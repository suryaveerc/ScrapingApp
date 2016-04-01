package com.scrape;

import java.io.*;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

import com.scrape.ScrapeWebUrl;

public class ScrapeMain {

    /**
     * @param args
     * @throws IOException
     */
    public static void scrape() throws IOException {
        // TODO Auto-generated method stub
        String filePath = "C:/Concordia/SDM/Assignment1/input";
        try {
            System.out.println("File path : " + filePath);
            filePath += "/input.csv";
            File f = new File(filePath);
            System.out.println("File : " + f.getName());
            scrapeFile(f);
        } catch (FileNotFoundException fnf) {
            System.out.println("File not found at the specified location!");

        }
    }

    public static void scrapeFile(File f) throws IOException {
        try {
            ArrayList<String> l = new ArrayList<String>();
            Scanner sc = new Scanner(f);

            while (sc.hasNext()) {
                String data = sc.next();

                if (data.startsWith("\"https"))
                //TO-DO - find fix for this logic
                {

                    String temp[] = data.split(";");


                    //ArrayList <String> k=new ArrayList<String>();
                    temp[0] = temp[0].substring(1);
                    System.out.println("modified value--->" + temp[0]);
                    if (temp[0] != "")
                        l.add(temp[0]);


                    //	System.out.println(l);

				/*for(int i =0;i<l.size();i++)
                {
					Iterator <String> it = l.iterator();
					String s=it.next();
					String tempo[]=s.split("\\.");
					if(tempo[3].equalsIgnoreCase("google"))
					k.add(tempo[3]+"."+tempo[4]+"."+tempo[5]);
					else if(tempo[3].equalsIgnoreCase("android"))
						k.add(tempo[3]+"."+tempo[4]);
					else
					k.add(tempo[3]);
					System.out.println(k);
				}*/

                }

            }
            System.out.println("values of the urls --->" + l.get(0));
            ScrapeWebUrl.scrapeUrl(l);

            //i++;
            //}
            //System.out.println("Sunil Prashanth"+br.readLine());
        } catch (FileNotFoundException fnf) {
            System.out.println("File not found at the specified location");
        }
    }
}
