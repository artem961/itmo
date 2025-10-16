//add Listeners
let submitButton = document.getElementById('submit');
let clearButton = document.getElementById('clear');
let elements = document.getElementsByName('r');

clearButton.addEventListener('click', (e) => {
   // dbManager.deleteAllItems();
    clearTableRows();
    endSession();
    canvasController.removeObjects(plane.points);
    plane.points = [];
    canvasController.updateFrame();
})

submitButton.addEventListener('click', () => {
    let x = document.querySelector('input[name="x"]:checked').value;
    let y = document.querySelector('input[name="y"]').value;
    let r = document.querySelectorAll('input[name="r"]:checked')
        .values()
        .map(el => el.value);


    if (validateAndShowTooltips()) {
        let json = sendRequest(x, y, r);
        json
            .then(data => {
                addTableRows(data);

                data.results.forEach(result => {
                    let point = new PointObject(canvas, result.x * canvas.width/3, result.y * canvas.height/3);
                    point.setHit(result.result);
                    plane.addPoint(point);
                    canvasController.updateFrame();
                });
                //saveToStorage(data);
            })
            .catch(err => {
                alert(err)
            })
    } else {
        submitButton.disabled = true;
        setTimeout(() => submitButton.disabled = false, 5000);
    }
});

for (const el of elements) {
    el.addEventListener('click', () => {

        let cnt = 0;
        let value = null;
        for (const element of elements) {
            if (element.checked) {
                cnt++;
                value = element.value;
            }
        }
        if (cnt === 1) {
            plane.switchLabels(value);
        } else if (plane.R !== String("R")) {
            plane.switchLabels("R");
        }
    })
}

//load data
/*
dbManager.getAllItems()
    .then(items => {
        if (items != null){
            items.forEach(item => {
                addTableRow(item.data);
            })
        }
    })
    .catch(err => {
        console.log(err);
    });
 */

function showTooltip(text, element) {
    let tooltip = new Tooltip(text);
    tooltip.showForElement(element);
}