package com.github.agmcc.slate.test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;

public final class FileUtils {

  private FileUtils() {
    /* Static Access */
  }

  public static String readResourceAsString(String path) {
    final var url = Resources.getResource(path);
    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
