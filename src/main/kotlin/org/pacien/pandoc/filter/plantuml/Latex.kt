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

fun Sequence<String>.toLatex() = Latex(this)

class Latex(private val body: Sequence<String>) {
  fun raw() = body.filterNot(String::isEmpty).joinToString("\n")

  private fun surround(prefix: String, suffix: String) =
    Latex(sequenceOf(prefix) + body + sequenceOf(suffix))

  fun resizeBox(width: String, height: String) = surround("\\resizebox{$width}{$height}{", "}")
  fun centering() = surround("\\centering", "")
  fun label(label: String) = surround("", "\\label{$label}")
  fun caption(caption: String) = surround("", "\\caption{$caption}")
  fun figure() = surround("\\begin{figure}[h]", "\\end{figure}")
}
