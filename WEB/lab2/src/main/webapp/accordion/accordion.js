class Accordion {
    constructor(element, mode, expanded) {

        this.element = element;
        this.mode = mode;
        if (typeof expanded !== "object"){
            this.expanded = expanded === undefined ? [] : JSON.parse(expanded);
        } else{
            this.expanded = expanded;
        }

        this.items = this.element.querySelectorAll(".accordion-item");
        this.expanded.forEach((n) => {
            this.openItem(this.items[n]);
        })

        this.items.forEach(item => {
            let header = item.querySelector(".accordion-header");

            header.addEventListener("click", () => {
                if (item.classList.contains("open")) {
                    this.closeItem(item);
                } else{
                    if (this.mode === "single") {
                        this.closeAll();
                    }
                    this.openItem(item);
                }
            });
        })
    }

    openItem(item) {
        item.classList.add('open');

    }

    closeItem(item) {
        item.classList.remove('open');
    }

    closeAll(){
        this.items.forEach(item => {
            this.closeItem(item);
        })
    }
}