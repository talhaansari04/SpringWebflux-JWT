package com.mps.insight.cabi.feed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mps.insight.logger.MyLogger;

public class LoadFeed {
    private static List<String> column;
    private static final String LOCATION = "D:\\Sample.csv";

    public static void main(String[] args) {
        loadFeedData(LOCATION);
    }

    private static void loadFeedData(String path) {
        List<Map<String, String>> ls = null;
        List<String[]> col = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                String name = file.getName().substring(0, file.getName().lastIndexOf(".")).toLowerCase();
                MyLogger.log("File Name :: " + name);
                inputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                col = bufferedReader.lines().findFirst().map(item -> Collections.singletonList(splitRow(item)))
                        .orElseGet(Collections::emptyList);
                columnHeader(col.get(0));
                MyLogger.log("Header :: " + column);
                ls = bufferedReader.lines().skip(0).map(line -> getData(splitRow(line))).collect(Collectors.toList());
                bufferedReader.close();
                MyLogger.log("Total Record :: " + ls.size());
                FeedQueryBuilder.buildQuery(ls, column, name);

            } else {
                MyLogger.error("File Not found location :: " + path);
            }
        } catch (Exception e) {
            MyLogger.error(e.getMessage());
        }finally {
            try {
                inputStream.close();
                bufferedReader.close();
            }catch (Exception e){

            }
        }
    }

    private static Map<String, String> getData(String[] line) {
        HashMap<String, String> map = new LinkedHashMap<>();
        try {
            for (int i = 0; i < line.length; i++) {
                if (line[i].equals("")) {
                    map.put(column.get(i), "-");
                } else {
                    map.put(column.get(i), line[i]);
                }

            }
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return map;
    }

    private static String[] splitRow(String line) {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    private static void columnHeader(String[] header) {
        column = new ArrayList<>();
        for (String str : header) {
            String st = str.replace("/ ", "");
            String s = st.replace(" ", "_").replace("(", "").replace(")", "");
            column.add(s.toLowerCase());
        }
    }
}
