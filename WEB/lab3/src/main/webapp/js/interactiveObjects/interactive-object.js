class InteractiveObject{
    constructor(canvas) {
        this.canvas = canvas;
        this.context = canvas.getContext("2d");

        this.width = canvas.width;
        this.height = canvas.height;
        this.hWidth = canvas.width / 2;
        this.hHeight = canvas.height / 2;

        this.checkHover = false;
        this.hovered = false;
        this.requestFrameUpdate = false;

        this.supportsAnimation = false;
        this.animationStarted = false;
    }

    draw() {
    }

    checkPositionInObject(position) {
        return true;
    }

    setCheckHover(check) {
        this.checkHover = check;
    }

    setHovered(hovered) {
        this.requestFrameUpdate = this.hovered !== hovered;
        this.hovered = hovered;
    }

    setSupportsAnimation(supports) {
        this.supportsAnimation = supports;
    }

    animate(){
    }
}