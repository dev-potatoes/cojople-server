<<<<<<<< HEAD:src/main/java/io/mojolll/project/filereader/YmlReader.java
package io.mojolll.project.filereader;
========
package io.mojolll.project.excelfilereader;
>>>>>>>> feature/issue/11-3:src/main/java/io/mojolll/project/excelfilereader/YmlReader.java

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YmlReader {
    public static Map<String, Object> datasourceReader (String filename){
        Yaml yaml = new Yaml();
        InputStream inputStream = ExcelConnection.class.getClassLoader().getResourceAsStream(filename);
        Map<String, Object> obj = yaml.load(inputStream);
        return (Map<String, Object>)((Map<String, Object>) obj.get("spring")).get("datasource");
    }
}
