class SectorZone extends InteractiveObject {
    constructor(canvas, center, radius, startAngle, endAngle) {
        super(canvas);
        this.zoneColor = 'rgba(73,103,200,0.5)';
        this.zoneColorHovered = 'rgba(73,103,200, 1)';

        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    draw() {
        if (this.hovered) {
            this.context.fillStyle = this.zoneColorHovered;
        } else{
            this.context.fillStyle = this.zoneColor;
        }

        let sector = new Sector(this.center, this.radius, this.startAngle, this.endAngle);
        sector.draw(this.context);
    }

    checkPositionInObject(position) {
        const dx = position.x - this.center.x;
        const dy = position.y - this.center.y;

        if (dx * dx + dy * dy > this.radius * this.radius){
            return false;
        }

        let angle = Math.atan2(dy, dx);
        if (angle < 0) angle += Math.PI * 2;

        return angle >= this.startAngle && angle <= this.endAngle;
    }
}