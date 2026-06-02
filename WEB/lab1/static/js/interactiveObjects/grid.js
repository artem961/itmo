class Grid extends InteractiveObject{
    constructor(canvas) {
        super(canvas);
        this.gridColor = '#D0D0D0';
    }

    draw() {
        this.context.strokeStyle = this.gridColor;

        for (let x = 0; x < this.hWidth; x += this.hWidth / 6) {
            let line = new Line(
                new Position(x, this.height),
                new Position(x, -this.height));

            let lineSym = new Line(
                new Position(-x, this.height),
                new Position(-x, -this.height));

            line.draw(this.context);
            lineSym.draw(this.context);
        }

        for (let y = 0; y < this.hHeight; y += this.hWidth / 6) {
            let line = new Line(
                new Position(this.width, y),
                new Position(-this.width, y));

            let lineSym = new Line(
                new Position(this.width, -y),
                new Position(-this.width, -y));

            line.draw(this.context);
            lineSym.draw(this.context);
        }
    }
}