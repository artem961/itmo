class TriangleZone extends InteractiveObject {
    constructor(canvas, vert1, vert2, vert3) {
        super(canvas);
        this.zoneColor = 'rgba(73,103,200,0.5)';
        this.zoneColorHovered = 'rgba(73,103,200, 1)';

        this.vert1 = vert1;
        this.vert2 = vert2;
        this.vert3 = vert3;
    }

    draw() {
        if (this.hovered) {
            this.context.fillStyle = this.zoneColorHovered;
        } else{
            this.context.fillStyle = this.zoneColor;
        }

        let triangle = new Polygon([
            this.vert1,
            this.vert2,
            this.vert3,
        ]);

        triangle.draw(this.context);
    }

    checkPositionInObject(position) {
        const x = position.x;
        const y = position.y;

        const area = this.triangleArea(this.vert1, this.vert2, this.vert3);
        
        const area1 = this.triangleArea({x, y}, this.vert2, this.vert3);
        const area2 = this.triangleArea(this.vert1, {x, y}, this.vert3);
        const area3 = this.triangleArea(this.vert1, this.vert2, {x, y});


        return Math.abs(area - (area1 + area2 + area3)) < 0.001;
    }

    triangleArea(a, b, c){
        return Math.abs((a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2);
    }
}