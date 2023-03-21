package io.mojolll.project.filereader;

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

    public static Map<String, Object> localFileReader (String filename){
        Yaml yaml = new Yaml();
        InputStream inputStream = ExcelMain.class.getClassLoader().getResourceAsStream(filename);
        Map<String, Object> obj = yaml.load(inputStream);
        return (Map<String, Object>) obj.get("excel");
    }


}
