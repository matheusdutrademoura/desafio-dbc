import logic.FileProcessor;
import static util.SimpleLogger.log;

public final class Main {

    public static void main(String [] args) {

        final String homePath = System.getenv("HOMEPATH");

        if (homePath == null || homePath.isBlank()) {
            log("HOMEPATH não está definido no seu sistema.");
            System.exit(0);
        }

        FileProcessor fp = new FileProcessor(
            homePath + "/data/in/",
            homePath + "/data/out/",
            homePath + "/data/tmp/"
        );

        while(true) {
            try {            
                fp.process();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}