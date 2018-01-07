package org.pacien.pandoc.filter.plantuml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Collectors;

final public class Filter {

  private static final String BEGIN_TAG = "\\begin{tikzpicture}[yscale=-1]";
  private static final String LINE_SEP = "\n";
  private static final String TYPE_KEY = "t";
  private static final String CONTENT_KEY = "c";
  private static final String CODE_BLOCK_TYPE = "CodeBlock";
  private static final String RAW_BLOCK_TYPE = "RawBlock";
  private static final String PLANTUML_TYPE = "puml";
  private static final String LATEX_TYPE = "latex";
  private static final int META_INDEX = 0;
  private static final int META_PROP_INDEX = 1;
  private static final int META_PROP_TYPE_INDEX = 0;
  private static final int CONTENT_INDEX = 1;

  private static String plantumlToLatex(String puml) throws IOException {
    try (ByteArrayOutputStream s = new ByteArrayOutputStream()) {
      new SourceStringReader(puml).generateImage(s, new FileFormatOption(FileFormat.LATEX_NO_PREAMBLE));
      try (BufferedReader r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.toByteArray())))) {
        return BEGIN_TAG + LINE_SEP + r.lines().filter(l -> !l.equals(BEGIN_TAG)).collect(Collectors.joining(LINE_SEP));
      }
    }
  }

  private static void renderPlantumlNode(ObjectNode n) throws IOException {
    String puml = n.get(CONTENT_KEY).get(CONTENT_INDEX).asText();
    String tikz = plantumlToLatex(puml);

    n.set(TYPE_KEY, TextNode.valueOf(RAW_BLOCK_TYPE));
    ((ArrayNode) n.get(CONTENT_KEY)).removeAll()
    .add(TextNode.valueOf(LATEX_TYPE))
    .add(TextNode.valueOf(tikz));
  }

  private static boolean isPlantumlNode(JsonNode n) {
    return n.path(TYPE_KEY).asText().equals(CODE_BLOCK_TYPE) &&
           n.path(CONTENT_KEY).path(META_INDEX).path(META_PROP_INDEX).path(META_PROP_TYPE_INDEX).asText().equals(PLANTUML_TYPE);
  }

  private static void walk(JsonNode n) throws IOException {
    if (isPlantumlNode(n))
      renderPlantumlNode((ObjectNode) n);
    else if (n.isContainerNode())
      for (Iterator<JsonNode> i = n.elements(); i.hasNext(); ) walk(i.next());
  }

  public static void filter(InputStream i, OutputStream o) throws IOException {
    ObjectMapper m = new ObjectMapper();
    JsonNode t = m.readTree(i);
    if (t != null) {
      walk(t);
      m.writeValue(o, t);
    }
  }

  public static void main(String args[]) {
    try {
      filter(System.in, System.out);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private Filter() {
    // static class
  }

}
