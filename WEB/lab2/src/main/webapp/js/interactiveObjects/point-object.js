class PointObject extends InteractiveObject {
    constructor(canvas, x, y) {
        super(canvas);
        this.x = x;
        this.y = y;

        this.hitColor = 'rgb(64,223,83)';
        this.missedColor = 'rgb(253,61,61)';
        this.hit = false;
    }

    draw() {
        if (this.hit){
            this.context.fillStyle = this.hitColor;
        } else{
            this.context.fillStyle = this.missedColor;
        }

        let point = new Point(new Position(this.x, this.y).convert(canvas))
        point.draw(this.context, 3);
    }

    checkPositionInObject(position) {
        const x = position.x;
        const y = position.y;
        return (x >= this.x && x <= this.x + this.width) && (y >= this.y && y <= this.y + this.height);
    }

    setHit(isHit){
        this.hit = isHit;
    }
}