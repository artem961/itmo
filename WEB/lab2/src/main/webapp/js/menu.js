//add Listeners
let submitButton = document.getElementById('submit');
let clearButton = document.getElementById('clear');
let rSelect = document.getElementById("r");
let xButtons = document.getElementsByName("x");

clearButton.addEventListener('click', (e) => {
    clearTableRows();
    endSession();
    canvasController.removeObjects(plane.points);
    plane.points = [];
    canvasController.updateFrame();
})

submitButton.addEventListener('click', () => {
    let x = document.querySelector('input[name="x"].selected').value;
    let y = document.querySelector('input[name="y"]').value;
    let r = rSelect.options[rSelect.selectedIndex].value;

    if (validateAndShowTooltips()) {
        let json = sendRequest(x, y, r);
        json
            .then(data => {
                addTableRows(data);

                data.results.forEach(result => {
                    let point = createPoint(canvas, result.x/result.r, result.y/result.r, result.r, result.result);
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

rSelect.addEventListener('change', () => {
    let r = rSelect.options[rSelect.selectedIndex].value;
    plane.switchLabels(r);
});

for (const button of xButtons) {
    button.addEventListener('click', () => {
        for (const btn of xButtons){
            btn.classList.remove("selected");
        }

        button.classList.add("selected");
    });
}

function showTooltip(text, element) {
    let tooltip = new Tooltip(text);
    tooltip.showForElement(element);
}