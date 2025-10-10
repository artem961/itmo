class RectangleZone extends InteractiveObject {
    constructor(canvas, x, y, width, height) {
        super(canvas);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.zoneColor = 'rgba(73,103,200,0.6)';
        this.zoneColorHovered = 'rgba(73,103,200, 1)';
    }

    draw() {
        if (this.hovered) {
            this.context.fillStyle = this.zoneColorHovered;
        } else{
            this.context.fillStyle = this.zoneColor;
        }

        let x = this.x;
        let y = this.y;
        let width = this.width;
        let height = this.height;

        let rectangle = new Polygon([
            new Position(x, y),
            new Position(x, y + height),
            new Position(x + width, height),
            new Position(x + width, y)
        ]);

        rectangle.draw(this.context);
    }

    checkPositionInObject(position) {
        const x = position.x;
        const y = position.y;
        return (x >= this.x && x <= this.x + this.width) && (y >= this.y && y <= this.y + this.height);
    }
}