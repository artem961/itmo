const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();

function getSelectedRvalues() {
    const rCheckboxDivs = document.querySelectorAll('#menuForm\\:r .ui-chkbox');

    for (const checkboxDiv of rCheckboxDivs) {
        let rCheckboxes = document.querySelectorAll('input[name="menuForm:r"]');
        let selected = []

        for (const checkbox of rCheckboxes) {
            if (checkbox.getAttribute('aria-checked') === 'true' ||
                checkbox.getAttribute('checked') === 'checked' &&
                checkbox.getAttribute('aria-checked') === null) {

                selected.push(checkbox.value)
            }
        }

        return selected;
    }
}

canvas.addEventListener('click', (e) => {
    let position = CanvasController.getCursorPositionOnCanvas(e, canvas);
    let x = (position.x / (canvas.width / 3) * 1).toFixed(2);
    let y = (position.y / (canvas.height / 3) * 1).toFixed(2);
    let r = getSelectedRvalues();


    const formX = document.getElementById("secretForm:x");
    const formY = document.getElementById("secretForm:y");
    const formR = document.getElementById("secretForm:r");
    const sendButton = document.getElementById("secretForm:submit");

    formX.value = x;
    formY.value = y;
    formR.value = r.join("&");
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
