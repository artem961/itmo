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

rSelect.addEventListener('change', () => {
    let r = rSelect.options[rSelect.selectedIndex].value;
    addPointsToCanvas();
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