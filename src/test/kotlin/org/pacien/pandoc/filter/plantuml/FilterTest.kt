/*
 * pandoc-filter-plantuml, a Pandoc AST filter rendering PlantUML diagrams
 * Copyright (C) 2018 Pacien TRAN-GIRARD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
