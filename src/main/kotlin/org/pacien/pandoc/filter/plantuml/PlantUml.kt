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
