package store.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FileReadTool {
    private static final String errorHeader = "FileReadTool : ";
    public static List<String> readFile(String fileName){
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = FileReadTool.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
        return lines;
    }
}
