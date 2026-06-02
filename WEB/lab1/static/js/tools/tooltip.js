class Tooltip{
    constructor(text){
        this.text = text;
        this.x = 0;
        this.y = 0;
        this.timeout = 5000;
        this.tooltip = null;
    }

    showTooltip(){
        this.tooltip = document.createElement("div");
        this.tooltip.className = "tooltip";
        this.tooltip.innerHTML = `${this.text}`;
        document.body.appendChild(this.tooltip);
        this.tooltip.style.visibility = "visible";

        this.positionTooltip();

        setTimeout(()=>{
            this.hideTooltip();
        }, this.timeout)
    }

    showForElement(element){
        let rect = element.getBoundingClientRect();

        const scrollX = window.pageXOffset;
        const scrollY = window.pageYOffset;
        this.x = rect.left + scrollX + (rect.width / 2);
        this.y = rect.top + scrollY;

        this.showTooltip();
    }

    hideTooltip(){
        this.tooltip.style.visibility = "hidden";
    }

    positionTooltip(){
        const tooltipRect = this.tooltip.getBoundingClientRect();
        const left = this.x - (tooltipRect.width / 2);
        const top = this.y - tooltipRect.height - 10;

        this.tooltip.style.left = `${left}px`;
        this.tooltip.style.top = `${top}px`;
    }
}