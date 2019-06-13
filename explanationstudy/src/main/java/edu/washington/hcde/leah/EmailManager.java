package edu.washington.hcde.leah;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Load emails
 */
public class EmailManager {

    public static List<Email> test = new ArrayList<>();
    private static List<Email> train = new ArrayList<>();
    private static Email practice;

    public static List<File> iterate(String path) throws IOException {
        List<File> ls = new ArrayList<>();
        final File jarFile = new File(EmailManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (name.startsWith(path + "/")) { //filter according to the path
                    if (!name.endsWith(".txt")) {
                        continue;
                    }
                    System.out.println("found: " + name);
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    List<String> lines = new ArrayList<>();
                    try (InputStream stream = loader.getResourceAsStream(name);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                        String next;
                        while ((next = reader.readLine()) != null) {
//                            log.info("next: " + next);
                            lines.add(next);
                        }
                    }
                    // has to end in .txt to be detected!
                    File tmpDir = new File("tmp/");
                    if (!tmpDir.exists()) {
                        tmpDir.mkdir();
                    }
                    File tmp = new File(tmpDir, name.substring(name.lastIndexOf("/")));
                    if (tmp.exists()) {
                        tmp.delete();
                    }
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tmp)));
                    for (String s : lines) {
//                        log.info("Writing " + s);
                        out.println(s);
                    }
                    out.flush();
                    out.close();
//                    log.info("done reading and printing to tmp: " + tmp.getName());
//                    log.info(lines.toString());
                    ls.add(tmp);
                }
            }
            jar.close();
        } else { // Run with IDE
            final URL url = Launcher.class.getResource("/" + path);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        log.info("HERE: " + app);
                        ls.add(app);
                    }
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
        }
        return ls;
    }

    public static void initialize() throws IOException, URISyntaxException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

//        Enumeration<URL> iter = loader.get("practice");
        List<File> dir = iterate("practice");

//        URL i = iter.nextElement();
//        log.info("Got URL: " + i);
//        log.info("URI v: " + i.toURI());
//        File practiceDir = new File(i.toURI());
        log.info("practice size: " + dir.size());
        for (File f : dir) {
            if (f.getName().endsWith(".txt")) {
                practice = Email.fromFile(f);
                break;
            }
        }
        log.info("Loaded practice email: " + practice);

        dir = iterate("train");
//        Enumeration<URL> iter = loader.getResources("train");
//        File trainDir = new File(iter.nextElement().toURI());
        for (File f : dir) {
            log.info("Trying " + f.getName());
            if (f.getName().endsWith(".txt")) {
                train.add(Email.fromFile(f));
            }
        }
        log.info("Loaded " + train.size() + " train emails.");

        dir = iterate("test");
//        iter = loader.getResources("test");
//        File testDir = new File(iter.nextElement().toURI());
        for (File f : dir) {
            if (f.getName().endsWith(".txt")) {
                test.add(Email.fromFile(f));
            }
        }
        log.info("Loaded " + test.size() + " test emails.");
    }

    public synchronized static Email getPracticeEmail() {
        return practice;
    }

    public synchronized static List<Email> getTrainEmails() {
        return new ArrayList<>(train);
    }

    public synchronized static List<Email> getTestEmails() {
        return new ArrayList<>(test);
    }

}
