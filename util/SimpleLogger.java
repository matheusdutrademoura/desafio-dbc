package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger {

    public static void log(String s) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": " + s);
    }
    
}