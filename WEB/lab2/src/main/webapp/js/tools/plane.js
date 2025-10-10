class Plane {
    constructor(canvasController, R) {
        this.canvasController = canvasController;
        this.canvas = canvasController.getCanvas();
        this.context = canvas.getContext('2d');
        this.R = R;

        this.rLabels = null;
        this.newLabels = null;
    }

    initObjects(){
        let R = this.R;

        let hWidth = canvas.width / 2;
        let hHeight = canvas.height / 2;
        let widthR = canvas.width/3;
        let heightR = canvas.height/3;
        let shift = 10;

        let grid = new Grid(canvas);
        let axis = new Axis(canvas);
        let rect = new RectangleZone(canvas, 0, 0, widthR, heightR/2);
        let triangle = new TriangleZone(canvas,
            new Position(0, 0),
            new Position(-widthR / 2, 0),
            new Position(0, -heightR));
        let sector = new SectorZone(canvas,
            new Position(0, 0),
            widthR,
            3 * Math.PI / 2,
            Math.PI * 2);

        this.rLabels = this._initRLabels(R, shift);
        this.newLabels = this.rLabels;


        rect.setCheckHover(true);
        triangle.setCheckHover(true);
        sector.setCheckHover(true);

        this.rLabels.forEach((label) => {
            label.setSupportsAnimation(true);
        })

        this.canvasController.addObject(grid);
        this.canvasController.addObject(rect);
        this.canvasController.addObject(triangle);
        this.canvasController.addObject(sector);
        this.canvasController.addObject(axis);
        this.canvasController.addObject(new LabelObject(canvas, new Position(shift,  hHeight -shift*1.5), "Y"));
        this.canvasController.addObject(new LabelObject(canvas, new Position(hWidth -shift*1.5, shift), "X"));
        canvasController.addObjects(this.rLabels);
    }

    switchLabels(newR){
        let shift = 10;
        canvasController.objects = [];
        this.initObjects();
        this.newLabels = this._initRLabels(newR, shift);

        if (newR === String("R") || newR >= this.R){
            this.newLabels.forEach((label) => {
                label.position.y += shift;
                label.distance = -label.distance;
                label.setSupportsAnimation(true);
                label.fadeOut = false;
                label.animationTime = 300;
                label.animate();
            })

            this.rLabels.forEach((label) => {
                label.distance = -label.distance;
                label.animationTime = 150;
                label.animate();
            })
        } else{
            this.newLabels.forEach((label) => {
                label.setSupportsAnimation(true);
                label.position.y -= shift;
                label.fadeOut = false;
                label.animationTime = 300;
                label.animate();
            })

            this.rLabels.forEach((label) => {
                label.animationTime = 150;
                label.animate();
            })
        }

        canvasController.addObjects(this.newLabels);
        canvasController.updateFrame();
        this.R = newR;
    }

    _initRLabels(R, shift){
        let widthR = canvas.width/3;
        let heightR = canvas.height/3;

        if (R !== String("R")) {
            return [
                new LabelObject(canvas, new Position(shift, heightR), String(R)),
                new LabelObject(canvas, new Position(shift, -heightR), String(-R)),
                new LabelObject(canvas, new Position(shift, heightR / 2), String(R / 2)),
                new LabelObject(canvas, new Position(shift, -heightR / 2), String(-R / 2)),
                new LabelObject(canvas, new Position(widthR, shift), String(R)),
                new LabelObject(canvas, new Position(-widthR, shift), String(-R)),
                new LabelObject(canvas, new Position(widthR / 2, shift), String(R / 2)),
                new LabelObject(canvas, new Position(-widthR / 2, shift), String(-R / 2))
            ];
        } else{
            return [
                new LabelObject(canvas, new Position(shift, heightR), "R"),
                new LabelObject(canvas, new Position(shift, -heightR), "-R"),
                new LabelObject(canvas, new Position(shift, heightR / 2), "R/2"),
                new LabelObject(canvas, new Position(shift, -heightR / 2), "-R/2"),
                new LabelObject(canvas, new Position(widthR, shift), "R"),
                new LabelObject(canvas, new Position(-widthR, shift), "-R"),
                new LabelObject(canvas, new Position(widthR / 2, shift), "R/2"),
                new LabelObject(canvas, new Position(-widthR / 2, shift), "-R/2")]
        }
    }
}