package edu.washington.hcde.leah;

import org.apache.commons.io.FileUtils;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebateFileSorter {
    public static void main(String[] args) {
        File[] files = new File("C:/Users/Melissa Birchfield/Downloads/convote_v1.1/data_stage_three").listFiles();
        showFiles(files);
    }

    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                if (file.getName().equals("test_set")) {
                    showFiles(file.listFiles()); // Calls same method again.
                }
            } else {
                System.out.println("File: " + file.getName());

                String clean = null;
                try {
                    clean = readFile(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Matcher unwantedMatcher = UNWANTED_SYMBOLS.matcher(clean);
                clean = unwantedMatcher.replaceAll("");

                if (clean.length() < 50) {
                    continue;
                }

                Charset charset = StandardCharsets.UTF_8;
                try {
                    Files.write(Paths.get(file.getAbsolutePath()), clean.getBytes(charset));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if ((file.getName().charAt(file.getName().length() - 7)) == 'R') {
                    File source = new File("C:\\Users\\Melissa Birchfield\\Downloads\\convote_v1.1\\data_stage_three\\test_set\\" + file.getName());
                    File dest = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\DebateStudy\\data\\test\\republican");

                    try {
                        FileUtils.copyFileToDirectory(source, dest);
                        System.out.println("copied file " + file.getName() + " to republican");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if ((file.getName().charAt(file.getName().length() - 7)) == 'D') {
                    File source = new File("C:\\Users\\Melissa Birchfield\\Downloads\\convote_v1.1\\data_stage_three\\test_set\\" + file.getName());
                    File dest = new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\DebateStudy\\data\\test\\democrat");

                    try {
                        FileUtils.copyFileToDirectory(source, dest);
                        System.out.println("copied file " + file.getName() + " to democrat");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("oops: " + file.getName());
                }
            }
        }
    }

    private static final Pattern UNWANTED_SYMBOLS =
            Pattern.compile(".,<>;(?:--|[\\[\\]{}()+/\\\\])");

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
