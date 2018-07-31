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
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode

// Structure of a content node:
// {
//   "t": "CodeBlock",
//   "c": [
//     [
//       "",
//       [ "puml", "otherClass" ],
//       [ ["scale", "0.5"], ["key", "value"] ]
//     ],
//     "@startuml\n@enduml"
//   ]
// }

fun JsonNode.type(): String = path("t").asText()
fun JsonNode.classes(): JsonNode = path("c").path(0).path(1)
fun JsonNode.classNames(): List<String> = classes().map(JsonNode::asText)
fun JsonNode.attributes(): JsonNode = path("c").path(0).path(2)
fun JsonNode.attributePair(): Pair<String, String> = Pair(path(0).asText(), path(1).asText())
fun JsonNode.attributeMap(): Map<String, String> = attributes().associate(JsonNode::attributePair)
fun JsonNode.content(): String = path("c").path(1).asText()

fun ObjectNode.setBlockType(type: String) = apply { set("t", TextNode.valueOf(type)) }
fun ObjectNode.setBlockContent(content: JsonNode) = apply { set("c", content) }
fun ObjectNode.setBlock(type: String, content: JsonNode) = setBlockType(type).setBlockContent(content)
