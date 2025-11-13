function addMenuListeners() {
    const rCheckboxDivs = document.querySelectorAll('#menuForm\\:r .ui-chkbox');

    for (const checkboxDiv of rCheckboxDivs) {
        checkboxDiv.addEventListener('click', (event) => {
            let rCheckboxes = document.querySelectorAll('input[name="menuForm:r"]');

            let r = "R"
            let selected = 0
            for (const checkbox of rCheckboxes) {
                if (checkbox.getAttribute('aria-checked') === 'true') {
                    r = checkbox.value;
                    selected++
                }
            }

            r = selected !== 1 ? "R" : r
            addPointsToCanvas();
            plane.switchLabels(r);
        });
    }

}

function addSubmitButtonListener() {
    let submitButton = document.getElementById("menuForm:submit");

    submitButton.addEventListener('click', (event) => {
        setTimeout(updateCanvas, 100);
    });
}

function updateCanvas(){
    addPointsToCanvas();
    canvasController.updateFrame();
}

function updateAll() {
    addMenuListeners();
    addSubmitButtonListener();
    updateCanvas();
}