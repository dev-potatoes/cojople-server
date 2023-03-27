package io.mojolll.project.excelfilereader;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ExcelMain {
    public static void main(String[] args) {
        ExcelFileReader er = new ExcelFileReader();
        ExcelConnection ec = new ExcelConnection();
        Map<String, Object> excelFileData = YmlReader.localFileReader("application.yml");
        String path = (String) excelFileData.get("path");
        String fileName = (String) excelFileData.get("fileName");
        List<Map<Object, Object>> excelData = er.readExcel(path, fileName);
        try {
            ec.excute(excelData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
