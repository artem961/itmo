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