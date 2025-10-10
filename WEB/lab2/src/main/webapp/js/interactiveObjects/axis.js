class Axis extends InteractiveObject {
    constructor(canvas, R) {
        super(canvas);
        this.axisColor = '#000000';
        this.R = R;
    }

    draw() {
        this.context.strokeStyle = this.axisColor;

        let lineY = new Line(
            new Position(0, -this.height),
            new Position(0, this.height)
        );
        let lineX = new Line(
            new Position(-this.width, 0),
            new Position(this.width, 0)
        );


        let wR = this.canvas.width/3;
        let hR = this.canvas.height/3;
        let shift = 6;

        let dashes = [
            new Line(new Position(-shift, hR), new Position(shift, hR)),
            new Line(new Position(-shift, -hR), new Position(shift, -hR)),
            new Line(new Position(wR, shift), new Position(wR, -shift)),
            new Line(new Position(-wR, shift), new Position(-wR, -shift)),
            new Line(new Position(-shift, hR / 2), new Position(shift, hR / 2)),
            new Line(new Position(-shift, -hR / 2), new Position(shift, -hR / 2)),
            new Line(new Position(wR / 2, shift), new Position(wR / 2, -shift)),
            new Line(new Position(-wR / 2, shift), new Position(-wR / 2, -shift)),
        ]


        dashes.forEach(dashe => {
            dashe.draw(this.context);
        })

        lineX.draw(this.context);
        lineY.draw(this.context);
        this.drawArrows(this.context);
    }

    drawArrows(context) {
        context.fillStyle = this.axisColor;

        let topArrow = new Polygon([
            new Position(0, this.hHeight),
            new Position(5, this.hHeight - 10),
            new Position(-5, this.hHeight - 10)]);

        let rightArrow = new Polygon([
            new Position(this.hWidth, 0),
            new Position(this.hWidth - 10, -5),
            new Position(this.hWidth - 10, 5)]);

        topArrow.draw(context);
        rightArrow.draw(context);
    }
}