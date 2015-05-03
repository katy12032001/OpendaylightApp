package org.opendaylight.controller.dynamic_route;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class HandlerServicePriorityList {

    String inputFile = "AppPriority.conf";
    InputStream fis;
    BufferedReader br = null;
    String line = null;

    HashMap<String, String> priorityList = new HashMap<>();

    public HandlerServicePriorityList() {

        // Set the input file
        try {
            fis = new FileInputStream(inputFile);
            br = new BufferedReader(new InputStreamReader(fis,
                    Charset.forName("UTF-8")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Process the input file
        try {
            while ((line = br.readLine()) != null) {
                String[] listEntry = line.split(", ");
                System.out.println("org.opendaylight.controller.dynamic_route.HandlerServicePriorityList");
                System.out.println(listEntry[0]+"\n"+listEntry[1]);
                priorityList.put(listEntry[0], listEntry[1]);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Clean up
        try {
            br.close();
            br = null;
            fis = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public HashMap<String, String> getPriorityList() {
        return priorityList;
    }

}
