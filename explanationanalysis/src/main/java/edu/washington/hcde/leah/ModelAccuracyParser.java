package edu.washington.hcde.leah;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class ModelAccuracyParser {

    private static String filepath = "C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\feature_instance_trained.txt";

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
            String line;
            try (PrintWriter writer = new PrintWriter(new File("C:\\Users\\Melissa Birchfield\\IdeaProjects\\bh-classifier\\feature_accuracy.csv"))) {
                while ((line = br.readLine()) != null) {
                    // process the line.
                    String accuracy = line.substring(line.indexOf("[") + 1, line.indexOf("]"));

//                    String num_hockey = line.substring(line.indexOf("]") + 2, line.indexOf("]") + 4);
//                    String num_baseball = line.substring(line.length() - 2);

                    StringBuilder sb = new StringBuilder();
                    sb.append(accuracy + "\n");
                    writer.write(sb.toString());
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
