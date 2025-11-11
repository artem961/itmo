class CanvasController {
    constructor(canvas) {
        this.canvas = canvas;
        this.context = canvas.getContext('2d');
        this.objects = []; // InteractiveObject Array
        this.isAnimating = false;

        canvas.addEventListener('mousemove', (e) => {
            let position = CanvasController.getCursorPositionOnCanvas(e, this.canvas);
            this.processHover(position);

            if (this.checkUpdateRequests()) {
                this.updateFrame();
            }
        })
    }

    startAnimationIfNeeded() {
        if (!this.isAnimating && this.checkAnimationsStarted()) {
            this.isAnimating = true;
            this.animationCycle();
        }
    }

    animationCycle() {
        if (!this.checkAnimationsStarted()) {
            this.isAnimating = false;
            return;
        }

        this.objects.forEach(object => {
            if (object.supportsAnimation && object.animationStarted) {
                object.animate();
            }
        });

        this.updateFrame();
        this.animationId = requestAnimationFrame(() => this.animationCycle());
    }

    updateFrame() {
        this.clearCanvas();
        this.drawObjects();
        this.startAnimationIfNeeded();
    }

    checkUpdateRequests() {
        for (const object of this.objects) {
            if (object.requestFrameUpdate) {
                return true;
            }
        }
        return false;
    }

    checkAnimationsStarted(){
        for (const object of this.objects) {
            if (object.supportsAnimation && object.animationStarted) {
                return true;
            }
        }
        return false;
    }

    getCanvas() {
        return this.canvas;
    }

    addObject(interactiveObject) {
        this.objects.push(interactiveObject);
    }

    addObjects(objects) {
        objects.forEach(object => {this.addObject(object);});
    }

    removeObject(interactiveObject) {
        this.objects.splice(this.objects.indexOf(interactiveObject), 1);
    }

    removeObjects(objects) {
        objects.forEach(object => {this.removeObject(object)});
    }

    clearCanvas() {
        this.context.clearRect(0, 0, canvas.width, canvas.height);
    }

    drawObjects() {
        this.objects.forEach((interactiveObject) => {
            interactiveObject.draw(this.context);
        });
    }

      static getCursorPositionOnCanvas(event, canvas) {
        const rect = canvas.getBoundingClientRect();
        const style = window.getComputedStyle(canvas);
        const paddingLeft = parseFloat(style.paddingLeft);
        const paddingTop = parseFloat(style.paddingTop);

        let x = Math.round(event.clientX - rect.left - paddingLeft - 1);
        let y = Math.round(event.clientY - rect.top - paddingTop - 1);

        const centerX = canvas.width / 2;
        const centerY = canvas.height / 2;
        x = x - centerX;
        y = y - centerY;

        return new Position(x, -y);
    }

     processHover(position) {
        this.objects.forEach(object => {
            if (object.checkHover && object.checkPositionInObject(position)) {
                object.setHovered(true);
            } else {
                object.setHovered(false);
            }
        })
    }
}