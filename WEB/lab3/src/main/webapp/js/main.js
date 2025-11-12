const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();

canvas.addEventListener('click', (e) => {
    let position = CanvasController.getCursorPositionOnCanvas(e, canvas);
    let x = (position.x / (canvas.width / 3) * plane.R).toFixed(2);
    let y = (position.y / (canvas.height / 3) * plane.R).toFixed(2);

    const formX = document.getElementById("menuForm:x");
    const formY = document.getElementById("menuForm:y");
    const sendButton = document.getElementById("menuForm:submit");

    formX.value = x;
    formY.value = y;
    sendButton.click();
});

function addPointsToCanvas() {
    plane.clearPoints();

    getServerResults().results.forEach(result => {
        let point = createPoint(canvas, result.x / result.r, result.y / result.r, result.r, result.result);
        point.setCurrentR(plane.R)
        plane.addPoint(point);
    });
    canvasController.updateFrame();
}

addPointsToCanvas();
canvasController.updateFrame();