package io.mojolll.project.api.filereader;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ExcelMain {
    public static void main(String[] args) {
        ExcelFileReader er = new ExcelFileReader();
        ExcelConnection ec = new ExcelConnection();

        String path = "/Users/ryu/mojolll/";
        String fileName = "학교개황(20230228 기준).xls";
        List<Map<Object, Object>> excelData = er.readExcel(path, fileName);
        try {
            ec.excute(excelData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
