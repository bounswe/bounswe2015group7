package sculture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sculture.lucene.SearchEngine;

import java.io.File;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        SearchEngine.initialize();
        File image_root = new File("/image");
        if (!image_root.exists())
            image_root.mkdirs();
    }
}
