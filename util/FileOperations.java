package util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import static util.SimpleLogger.log;

public class FileOperations {

    public static Set<Path> list(String folder) {
        
        Set<Path> fileList = new HashSet<>();

        try {
            Files.walkFileTree(Paths.get(folder), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                throws IOException {
                    if (path.getFileName().toString().endsWith(".dat")) {
                        fileList.add(path);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println("Erro ao listar os arquivos do diretório [" + folder + "]");
        }

        return fileList;
    }

    public static void write(String content, Path filePath) throws IOException {
        try {
            Files.createFile(filePath);
            Files.write(filePath, content.getBytes());

        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    public static void move(Path file, String to) throws IOException {
        Files.move(file, Paths.get(to + file.getFileName().toString()));
    }

    public static Path outputFilePath(Path file, String folder) {

        String fileName = file.getFileName().toString();

        int lastDot = fileName.lastIndexOf('.');
        fileName = fileName.substring(0, lastDot) + ".done" + fileName.substring(lastDot);

        Path newFilePath = Paths.get(folder + fileName);

        return newFilePath;
    }

}