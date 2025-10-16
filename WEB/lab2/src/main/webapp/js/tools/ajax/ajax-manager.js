const api = 'http://localhost:8080/lab2-1.0-SNAPSHOT';

async function endSession(){
    let query = `${api}/end`

    let response = await fetch(query);

    if (response.ok) {
        return response.json();
    } else {
        alert(`HTTP Error! ${response.message}`);
    }
}

async function sendRequest(x, y, r) {
    //const api = 'http://localhost:8080/fcgi-bin/server.jar';
    let query = `${api}/start?x=${x}&y=${y}`
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