const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();

canvas.addEventListener('click', (e) => {
    let position = CanvasController.getCursorPositionOnCanvas(e, canvas);
    let x = (position.x / (canvas.width / 3) * plane.R).toFixed(2);
    let y = (position.y / (canvas.height / 3) * plane.R).toFixed(2);

    const redirect = document.getElementById("redirect").checked;
    let json = sendRequest(x, y, [plane.R], redirect);
    if (!redirect) {
        json
            .then(data => {
                addTableRows(data);
                data.results.forEach(result => {
                    let point = createPoint(canvas, result.x / result.r, result.y / result.r, result.r, result.result);
                    point.setCurrentR(plane.R)
                    plane.addPoint(point);
                    canvasController.updateFrame();
                });
            })
            .catch(err => {
                alert(err)
            })
    }
});

function addPointsToCanvas() {
    let json = getHistory();
    plane.clearPoints();

    json
        .then(data => {
            data.results.forEach(result => {
                let point = createPoint(canvas, result.x / result.r, result.y / result.r, result.r, result.result);
                point.setCurrentR(plane.R)
                plane.addPoint(point);
            });
            canvasController.updateFrame();
        })
        .catch(err => {
            showError(err)
        });
}

addPointsToCanvas();
canvasController.updateFrame();