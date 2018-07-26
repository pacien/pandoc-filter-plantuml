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
