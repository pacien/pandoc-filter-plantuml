package org.pacien.pandoc.filter.plantuml

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.ByteArrayOutputStream

object PlantUml {
  private val OUTPUT_FORMAT = FileFormatOption(FileFormat.LATEX_NO_PREAMBLE)

  private fun SourceStringReader.generateImage(outputFormat: FileFormatOption) =
    ByteArrayOutputStream().use { buffer ->
      generateImage(buffer, outputFormat)
      buffer.toString().lineSequence()
    }

  fun renderTikz(plantuml: String) =
    SourceStringReader(plantuml)
      .generateImage(OUTPUT_FORMAT)
      .toLatex()
}
