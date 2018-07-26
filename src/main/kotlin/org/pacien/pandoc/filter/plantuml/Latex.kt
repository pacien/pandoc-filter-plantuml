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
