package net.sf.enunciate.template;

import net.sf.enunciate.EnunciateTestProcessorFactory;
import static org.testng.Assert.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Ryan Heaton
 */
public abstract class EnunciateTemplateTestCase {

  private File outputDir;

  protected void setUp() throws Exception {
    outputDir = File.createTempFile("enunciatetest", "");
    outputDir.delete();
    outputDir.mkdirs();
  }

  /**
   * Run a the template found at the specified relative resource path.
   *
   * @param relativeResource The relative resource.
   */
  public void runTemplate(String relativeResource) {
    URL resource = getClass().getResource(relativeResource);
    if (resource == null) {
      throw new IllegalStateException(String.format("Cannot load resource: %s",relativeResource));
    }
    runTemplate(resource);
  }

  /**
   * Run the template at the specified url.
   *
   * @param url The url.
   */
  public void runTemplate(URL url) {
    ArrayList<String> sourceFiles = getAllJavaFiles(getSampleSourceSubdirectoryName());
    runTemplate(url, sourceFiles);
  }

  /**
   * Run the template at the specified url on all java files in the specified subdirectory.
   *
   * @param url The url.
   * @param subdir The subdirectory.
   */
  public void runTemplate(URL url, String subdir) {
    ArrayList<String> sourceFiles = getAllJavaFiles(subdir);
    runTemplate(url, sourceFiles);
  }

  /**
   * Get a list of all java files in the specified subdirectory.
   *
   * @param subdir The subdir.
   * @return The list of all java files in the specified subdir.
   */
  protected ArrayList<String> getAllJavaFiles(String subdir) {
    ArrayList<String> sourceFiles = new ArrayList<String>();
    findJavaFiles(getSourceFileDirectory(subdir), sourceFiles);
    return sourceFiles;
  }

  /**
   * Run the template at the specified url on the specified files.
   *
   * @param url The url of the template.
   * @param files The list of absolute file names of the source files.
   */
  public void runTemplate(URL url, Collection<String> files) {
    ArrayList<String> aptOpts = getAptOptions();
    aptOpts.addAll(files);

    int procCode = com.sun.tools.apt.Main.process(new EnunciateTestProcessorFactory(url), aptOpts.toArray(new String[aptOpts.size()]));
    assertTrue(procCode == 0);
  }

  /**
   * Get the source file directory given the specified subdirectory name relative to the base directory.
   *
   * @return The source file directory.
   */
  protected File getSourceFileDirectory(String subdir) {
    String srcDirPath = System.getProperty("enunciate.base.sample.src.dir");
    if (srcDirPath == null) {
      fail("The base directory for the sample source code must be specified in the 'enunciate.base.sample.src.dir' property.");
    }

    return new File(new File(srcDirPath), subdir);
  }

  /**
   * The name subdirectory of the sample source, relative to the base directory.
   *
   * @return The subdirectory of the sample source.
   */
  protected String getSampleSourceSubdirectoryName() {
    return "basic";
  }

  protected ArrayList<String> getAptOptions() {
    ArrayList<String> aptOpts = new ArrayList<String>();
    aptOpts.add("-cp");
    aptOpts.add(System.getProperty("java.class.path"));
    aptOpts.add("-s");
    aptOpts.add(getOutputDir().getAbsolutePath());
    aptOpts.add("-nocompile");
    return aptOpts;
  }

  public File getOutputDir() {
    return outputDir;
  }

  protected void findJavaFiles(File dir, Collection<String> filenames) {
    File[] javaFiles = dir.listFiles(EnunciateTemplateTestCase.JAVA_FILTER);
    for (File javaFile : javaFiles) {
      filenames.add(javaFile.getAbsolutePath());
    }

    File[] dirs = dir.listFiles(EnunciateTemplateTestCase.DIR_FILTER);
    for (File dir1 : dirs) {
      findJavaFiles(dir1, filenames);
    }
  }

  private static FileFilter JAVA_FILTER = new FileFilter() {
    public boolean accept(File pathname) {
      return pathname.getName().endsWith(".java");
    }
  };

  private static FileFilter DIR_FILTER = new FileFilter() {
    public boolean accept(File pathname) {
      return pathname.isDirectory();
    }
  };

  protected Properties readOutputAsProperties(String output) throws IOException {
    FileInputStream inStream = readOutputAsStream(output);
    Properties results = new Properties();
    try {
      results.load(inStream);
    }
    finally {
      inStream.close();
    }
    return results;
  }

  protected FileInputStream readOutputAsStream(String output) throws FileNotFoundException {
    File outputFile = new File(getOutputDir(), output);
    assertTrue(outputFile.exists(), "No outputFile found.");
    return new FileInputStream(outputFile);
  }

}