const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();
canvasController.updateFrame();

canvas.addEventListener('click', (e) => {
    let position = CanvasController.getCursorPositionOnCanvas(e, canvas);
    let x = (position.x / (canvas.width / 3)).toFixed(2);
    let y = (position.y / (canvas.height / 3)).toFixed(2);

    let json = sendRequest(x, y, [plane.R]);
    json
        .then(data => {
            addTableRows(data);
            data.results.forEach(result => {
                let point = new PointObject(canvas, result.x * canvas.width/3, result.y * canvas.height/3);
                point.setHit(result.result);
               plane.addPoint(point);
               canvasController.updateFrame();
            });
        })
        .catch(err => {
            alert(err)
        })
});