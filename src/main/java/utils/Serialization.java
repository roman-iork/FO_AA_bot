package utils;

import java.io.IOException;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Serialization {
    public static String serialize(LinkedHashMap<String, String> map) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return json;
    }

    public static LinkedHashMap<String, String> unserialize(String json) {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        try {
            data = mapper.readValue(json, new TypeReference<>() { });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static String readFile(String path) {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        String content = "";
        try {
            content = Files.readString(fullPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

//    public static void writeFile(String path, String content) {
//        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
//        try {
//            Files.writeString(fullPath, content, StandardOpenOption.WRITE);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public static void renew(String path, String data) {
        try {
            Files.delete(Path.of(path));
            Files.writeString(Path.of(path), data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
