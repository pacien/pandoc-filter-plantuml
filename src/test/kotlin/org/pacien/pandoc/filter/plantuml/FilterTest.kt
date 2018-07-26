package org.pacien.pandoc.filter.plantuml

import org.junit.Assert
import org.junit.Test

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

class FilterTest {
  private fun testCompare(inputResource: String, expectedOutputResource: String) {
    val inputStream = javaClass.getResourceAsStream(inputResource)
    val expectedOutputFilePath = Paths.get(javaClass.getResource(expectedOutputResource).toURI())
    val expectedOutput = Files.readAllBytes(expectedOutputFilePath)

    ByteArrayOutputStream().use { outputStream ->
      Filter.filter(inputStream, outputStream)
      Assert.assertArrayEquals(outputStream.toByteArray(), expectedOutput)
    }
  }

  /**
   * Should only replace the PlantUML code block and leave other ones untouched.
   */
  @Test fun testIdentifyBlock() =
    testCompare("/identifyblock.input.json", "/identifyblock.expected.json")

  /**
   * Figure should be centered with a caption and a label.
   */
  @Test fun testAttributes() =
    testCompare("/attributes.input.json", "/attributes.expected.json")

  /**
   * Large figure should be scaled down to column width, keeping its aspect ratio.
   */
  @Test fun testResize() =
    testCompare("/resize.input.json", "/resize.expected.json")
}
