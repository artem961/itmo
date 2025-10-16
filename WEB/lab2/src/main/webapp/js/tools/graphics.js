class GeometricShape{
    draw(ctx) {
    }
}

class Position{
    constructor(x, y){
        this.x = x;
        this.y = y;
    }

    convert(canvas){
        let centerX = canvas.width/2;
        let centerY = canvas.height/2;
        return new Position(centerX + this.x, centerY - this.y);
    }
}

class Point extends GeometricShape{
    constructor(position) {
        super();
        this.position = position;
    }

    draw(ctx, scale) {
        ctx.beginPath();
        ctx.arc(this.position.x, this.position.y, scale, 0, 2 * Math.PI);
        ctx.fill();
        ctx.closePath();
    }
}

class Sector extends GeometricShape{
    constructor(position, radius, startAngle, endAngle) {
        super();
        this.position = position;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    draw(ctx) {
        let position = this.position.convert(ctx.canvas);

        ctx.beginPath();
        ctx.moveTo(position.x, position.y);
        ctx.arc(position.x, position.y, this.radius, -this.endAngle, -this.startAngle);
        ctx.closePath();
        ctx.fill();
    }
}

class Label extends GeometricShape{
    constructor(position, text) {
        super();
        this.position = position;
        this.text = text;
    }

    draw(ctx) {
        let position = this.position.convert(ctx.canvas);

        ctx.fillText(this.text, position.x, position.y);
    }
}

class Line extends GeometricShape{
    constructor(positionFrom, positionTo) {
        super();
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
    }

    draw(ctx) {
        let from = this.positionFrom.convert(ctx.canvas);
        let to = this.positionTo.convert(ctx.canvas);

        ctx.beginPath();
        ctx.moveTo(from.x, from.y);
        ctx.lineTo(to.x, to.y);
        ctx.stroke();
        ctx.closePath();
    }
}

class Polygon extends GeometricShape{
    constructor(vertices) {
        super();
        this.vertices = vertices;
    }

    draw(ctx) {
        let startPoint = this.vertices[0].convert(ctx.canvas);

        ctx.beginPath();
        ctx.moveTo(startPoint.x, startPoint.y);
        this.vertices.forEach(vertex => {
            let vertexConverted = vertex.convert(ctx.canvas);
            ctx.lineTo(vertexConverted.x, vertexConverted.y);
        });
        ctx.fill();
        ctx.stroke();
        ctx.closePath();
    }
}


