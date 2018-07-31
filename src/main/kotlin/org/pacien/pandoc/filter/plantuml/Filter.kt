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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.io.InputStream
import java.io.OutputStream

object Filter {
  private val mapper = ObjectMapper()

  private inline fun <T> T.conditionally(condition: Boolean, block: (T) -> T): T =
    if (condition) block(this) else this

  private inline fun <T, A> T.withNonNull(value: A?, block: (T, A) -> T): T =
    if (value != null) block(this, value) else this

  private fun JsonNode.isCodeBlock() = type() == "CodeBlock"
  private fun JsonNode.isPlantUmlBlock() = isCodeBlock() && "puml" in classNames()

  private fun Latex.resizeBox(attrs: Map<String, String>) =
    resizeBox(attrs["width"] ?: "!", attrs["height"] ?: "!")

  private fun Latex.setOptions(classes: List<String>, attrs: Map<String, String>) =
    this
      .conditionally("width" in attrs || "height" in attrs) { it -> it.resizeBox(attrs) }
      .conditionally("centered" in classes, Latex::centering)
      .withNonNull(attrs["caption"], Latex::caption)
      .withNonNull(attrs["label"], Latex::label)
      .conditionally("caption" in attrs || "label" in attrs, Latex::figure)

  private fun arrayNodeOf(type: String, content: String): ArrayNode =
    mapper.createArrayNode()
      .add(TextNode.valueOf(type))
      .add(TextNode.valueOf(content))

  private fun renderPlantumlNode(node: ObjectNode) {
    val puml = node.content()
    val tikz = PlantUml.renderTikz(puml)
    val block = tikz.setOptions(node.classNames(), node.attributeMap())
    node.setBlock("RawBlock", arrayNodeOf("latex", block.raw()))
  }

  private fun walk(node: JsonNode): Unit = when {
    node.isPlantUmlBlock() -> renderPlantumlNode(node as ObjectNode)
    else -> node.forEach(Filter::walk)
  }

  fun filter(input: InputStream, output: OutputStream) {
    mapper.readTree(input)?.let { tree ->
      walk(tree)
      mapper.writeValue(output, tree)
    }
  }
}
