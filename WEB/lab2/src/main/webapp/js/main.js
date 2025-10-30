const canvas = document.getElementById('canvas');
const canvasController = new CanvasController(canvas);
//const dbManager = new DBManager();

let plane = new Plane(canvasController, 1);
plane.initObjects();

canvas.addEventListener('click', (e) => {
    let position = CanvasController.getCursorPositionOnCanvas(e, canvas);
    let x = (position.x / (canvas.width / 3) * plane.R).toFixed(2) ;
    let y = (position.y / (canvas.height / 3) * plane.R).toFixed(2);

    let json = sendRequest(x, y, [plane.R]);
    json
        .then(data => {
            addTableRows(data);
            data.results.forEach(result => {
                let point = createPoint(canvas, result.x / result.r, result.y/result.r, result.r, result.result);
                point.setCurrentR(plane.R)
               plane.addPoint(point);
               canvasController.updateFrame();
            });
        })
        .catch(err => {
            alert(err)
        })
});


//add points
const table = document.getElementById("results");
const rows = table.getElementsByTagName('tr');

for (const row of rows){
    if (row.querySelector("th")){
        continue;
    }
    const cells = row.getElementsByTagName('td');
    const data = [];
    for (let i = 0; i < cells.length; i++) {
        data.push(cells[i].textContent.trim());
    }

    const x = data[0];
    const y = data[1];
    const r = data[2];
    const res = data[3] === "true";

    let point = createPoint(canvas, x, y, r, res);
    plane.addPoint(point)
}

canvasController.updateFrame();