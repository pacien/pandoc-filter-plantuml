pandoc-filter-plantuml
======================

A Pandoc AST filter rendering PlantUML code blocks into vector diagrams.

This filter produces TikZ code that must then be rendered using another filter such as
[tikz.py][tikz].


Usage
-----

A PlantUML diagram in an example Pandoc Markdown file `example.md`:

    ```puml
    @startuml
    Bob->Alice : hello
    @enduml
    ```

Using the helper scripts [tikz.py][tikz] and `pandoc-filter-plantuml.sh`:

    #/bin/sh
    java -jar pandoc-filter-plantuml.jar <&0

Can be rendered and included as a vector resource in a PDF by running:

    % pandoc --filter=pandoc-filter-plantuml.sh \
             --filter=tikz.py \
             --output=example.pdf \
             example.md

Or as an image in an HTML document with the following command:

    % pandoc --filter=pandoc-filter-plantuml.sh \
             --filter=tikz.py \
             --output=example.html \
             example.md


Build
-----

The project can be built using the Gradle `build` task.

An autonomous jar file can be generated using the `jar` task.


License
-------

Project released under the terms of the GNU GPL v3.
See /license.txt


[tikz]: https://github.com/jgm/pandocfilters/blob/master/examples/tikz.py
