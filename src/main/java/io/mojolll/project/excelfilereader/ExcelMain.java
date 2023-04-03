<<<<<<<< HEAD:src/main/java/io/mojolll/project/filereader/ExcelMain.java
package io.mojolll.project.filereader;
========
package io.mojolll.project.excelfilereader;
>>>>>>>> feature/issue/11-3:src/main/java/io/mojolll/project/excelfilereader/ExcelMain.java

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ExcelMain {
    public static void main(String[] args) {
        ExcelFileReader er = new ExcelFileReader();
        ExcelConnection ec = new ExcelConnection();
        String path = ExcelProperties.PATH;
        String fileName = ExcelProperties.FILE_NAME;
        List<Map<Object, Object>> excelData = er.readExcel(path, fileName);
        try {
            ec.excute(excelData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
