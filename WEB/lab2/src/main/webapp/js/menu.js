//add Listeners
let submitButton = document.getElementById('submit');
let clearButton = document.getElementById('clear');
let elements = document.getElementsByName('r');

clearButton.addEventListener('click', (e) => {
    dbManager.deleteAllItems();
    clearTableRows();
})

submitButton.addEventListener('click', () => {
    let x = document.querySelector('input[name="x"]:checked').value;
    let y = document.querySelector('input[name="y"]').value;
    let r = document.querySelectorAll('input[name="r"]:checked')
        .values()
        .map(el => el.value);


    if (validateAndShowTooltips()) {
        let json = sendRequest(x, y, r);
        console.log(json)
        json
            .then(data => {
                addTableRows(data);
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


function validateAndShowTooltips() {
    let valid = true;

    let y = document.querySelector('input[name="y"]').value;
    let r = document.querySelectorAll('input[name="r"]:checked')
        .values()
        .map(el => el.value);

    let yField = document.querySelector('input[name="y"]');
    let rLabel = document.querySelector('input[name="r"]');

    if (!validateNumberInString(y)) {
        showTooltip("Введите целое или дробное число!", yField);
        valid = false;
    } else if (!validateRange(y, -5, 3)) {
        showTooltip("Диапазон значений y -5 ... 3", yField);
        valid = false;
    }

    if (r.toArray().length <= 0) {
        showTooltip("Выберите значение R!", rLabel);
        valid = false;
    }

    return valid;
}

function validateNumberInString(string) {
    return /^-?[0-9]+([.][0-9]+)?$/.test(string);
}

function validateRange(n, min, max) {
    return +n >= min & +n <= max;
}

function showTooltip(text, element) {
    let tooltip = new Tooltip(text);
    tooltip.showForElement(element);
}


async function sendRequest(x, y, r) {
    //const api = 'http://localhost:8080/fcgi-bin/server.jar';
    const api = 'http://localhost:8080/lab2-1.0-SNAPSHOT/start';
    let query = `${api}/calc?x=${x}&y=${y}`
    r.forEach(rad => {
        query += `&r=${rad}`;
    })

    let response = await fetch(query);

    if (response.ok) {
        return response.json();
    } else {
        alert(`HTTP Error! ${response.message}`);
    }
}

function addTableRows(results) {
    results.results.forEach((row) => {
        addTableRow(row);
    });
}

function addTableRow(data) {
    let x = data.x;
    let y = data.y;
    let r = data.r;
    let res = data.result;
    let time = data.time;
    let currentTime = data.currentTime;

    const table = document.getElementById("results").getElementsByTagName('tbody')[0];
    const row = table.insertRow(0);
    addCell(row, x);
    addCell(row, y);
    addCell(row, r);
    let resCell = addCell(row, res);
    resCell.setAttribute('data-result', res);
    addCell(row, time);
    addCell(row, currentTime);
}

function addCell(row, value) {
    const newCell = row.insertCell();
    const newText = document.createTextNode(value);
    newCell.appendChild(newText)
    return newCell;
}

function clearTableRows(){
    const table = document.getElementById("results").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}

function saveToStorage(data) {
    data.results.forEach((res) => {
        dbManager.addItem(res);
    });
}