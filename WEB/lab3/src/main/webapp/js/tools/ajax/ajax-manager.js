const api = 'http://localhost:8080/lab2-1.0-SNAPSHOT';

async function endSession(){
    let query = `${api}/end`

    let response = await fetch(query);

    if (response.ok) {
        return response.json();
    } else {
        showError(`HTTP Error! ${response.message}`);
    }
}

async function getHistory(){
    let query = `${api}/history`

    let response = await fetch(query);

    if (response.ok){
        return response.json()
    } else {
        showError(response.message);
    }
}

async function sendRequest(x, y, r, redirect=false) {
    //const api = 'http://localhost:8080/fcgi-bin/server.jar';
    let query = `${api}/start?x=${x}&y=${y}&r=${r}&redirect=${redirect}`

    let response = await fetch(query, {
        redirect: "follow"
    });

    if (response.redirected){
        window.location.href = response.url;
    } else{
        if (response.ok) {
            return response.json();
        } else {
            showError(`HTTP Error! ${response.message}`);
        }
    }
}

function showError(message){
    alert(message);
}