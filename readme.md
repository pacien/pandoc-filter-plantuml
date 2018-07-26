pandoc-filter-plantuml
======================

A Pandoc AST filter rendering PlantUML code blocks into vector diagrams.

This filter produces TikZ code that can be rendered as vector diagrams in PDF documents,
or as raster graphics by using another filter such as [tikz.py][tikz].


Example
-------

A PlantUML diagram in an example Pandoc Markdown file `example.md`:

    ---
    header-includes: \usepackage{tikz}
    ---
    
    ```{.puml .centered caption="Courtesy protocol" width=\columnwidth}
    @startuml
    Bob->Alice : hello
    Alice->Bob : hi
    @enduml
    ```

Using the helper scripts `pandoc-filter-plantuml.sh`:

    #/bin/sh
    java -jar pandoc-filter-plantuml.jar <&0

Can be rendered as a vector resource in a PDF by running:

    % pandoc --filter=pandoc-filter-plantuml.sh \
             --output=example.pdf \
             example.md

Or as a raster image using [tikz.py][tikz] in an HTML document with the following command:

    % pandoc --filter=pandoc-filter-plantuml.sh \
             --filter=tikz.py \
             --output=example.html \
             example.md


Options
-------

The following rendering options can be supplied as [fenced code attributes][fenced_code_attribute]:

* `.centered`: centers the diagram horizontally on the page
* `caption="Some caption"`: adds a figure caption below the diagram
* `label="somelabel`: adds a label to the figure
* `width=\columnwidth` and `height=100pt`: resize the diagram using the `\resizebox` command,
  keeping the aspect ration of only one of the two is given


Build
-----

The project can be built using the Gradle `build` task.

An autonomous jar file can be generated using the `jar` task.


License
-------

Project released under the terms of the GNU GPL v3.
See /license.txt


[tikz]: https://github.com/jgm/pandocfilters/blob/master/examples/tikz.py
[fenced_code_attribute]: http://pandoc.org/MANUAL.html#fenced-code-blocks
