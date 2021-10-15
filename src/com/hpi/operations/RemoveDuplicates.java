package com.hpi.operations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveDuplicates {



    public static void removeDuplicates(String fileInput, String fileOutput) throws IOException {

        FileWriter newCsv = new FileWriter(fileOutput);
        BufferedWriter newCsvBW = new BufferedWriter(newCsv);
        BufferedReader reader = new BufferedReader(new FileReader(fileInput));
        String data;


        try {
            String temp = null;
            List<String> tempList = new ArrayList<String>();
            do {
                data = reader.readLine();
                String urlToken = null;

                if (data != null) {
                    String[] splitText = data.split(",");
                    urlToken = splitText[0];
                }

                if (temp != null) {
                    if (data == null || urlToken.contains(urlToken)) {
                        if (!(temp.equals(urlToken))) {
                            for (int i = 0; i < tempList.size() ; i++) {
                                newCsvBW.append(tempList.get(i));
                                newCsvBW.append("\n");
                            }
                        }
                        tempList.clear();
                        temp = urlToken;
                    }
                } else {
                    temp = urlToken;
                }
                tempList.add(data);
            }
            while (data != null);
        } finally {
            newCsvBW.close();
            reader.close();
        }
    }



}
