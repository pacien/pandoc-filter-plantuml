package org.pacien.pandoc.filter.plantuml;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

final public class FilterTest {

  private static final String INPUT_FILE = "/input.json";
  private static final String EXPECTED_FILE = "/expected.json";

  @Test
  public void filterTest() {
    try (ByteArrayOutputStream o = new ByteArrayOutputStream()) {
      byte[] e = Files.readAllBytes(Paths.get(getClass().getResource(EXPECTED_FILE).toURI()));
      Filter.filter(getClass().getResourceAsStream(INPUT_FILE), o);
      Assert.assertArrayEquals(o.toByteArray(), e);
    } catch (IOException | URISyntaxException e) {
      Assert.fail();
    }
  }

}
