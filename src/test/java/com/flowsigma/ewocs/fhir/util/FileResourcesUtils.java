package com.flowsigma.ewocs.fhir.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileResourcesUtils {

  //https://mkyong.com/java/java-read-a-file-from-resources-folder/
  public File getFileAsStringFromResource(String fileName)  {
    ClassLoader classLoader = FileResourcesUtils.class.getClassLoader();
    URL resource = classLoader.getResource(fileName);
    if (resource == null) {
      throw new IllegalArgumentException("file not found! " + fileName);
    }

    try {
      return new File(resource.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static String readFileAsString(String file)throws Exception {
    return new String(Files.readAllBytes(Paths.get(file)));
  }
}
