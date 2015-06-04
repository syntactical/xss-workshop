I would recommend running through the workshop as a
participant before facilitating. At the very least,
scan though [the participant readme](readme-participant.md).

## Presentation

The slides are html/js built using reveal.js. To run:

* `./gradlew presentation:run`
* Browse to `http://localhost:8081`

For presentation mode and speaker notes: press `s` after opening. Will open in a seperate browser window

Use right and left arrow keys to navigate forward and backwards

`esc` will bring up a side overview

## Hands on sections

Hands on sections increase participation but add a lot of extra time. In order
to speed things up, you can choose to demonstrate the exploit in front of
everyone rather than having everyone exploit the vulnerability themselves.

To do this, have the demo application running before you start presenting,
and when you get to slide #7, switch away from the presentation, open up
`http://localhost:8081`, and demonstrate the exploit. One strategy to keep
people involved when doing this is having the participants make suggestions
about what you should type.

## Print / Export PDF

* `./gradlew presentation:run`
* Open `http://localhost:8081` in Chrome with the `print-pdf` query parameter
* For example: `http://localhost:8081/index.html?print-pdf`
* Open the print dialog and either select a printer or export to PDF
