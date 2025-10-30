let elements = document.getElementsByClassName("accordion");
[].forEach.call(elements, function(el) {
    new Accordion(el, "single", [1]);
    });
