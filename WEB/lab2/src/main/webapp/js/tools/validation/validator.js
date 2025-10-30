function validateAndShowTooltips() {
    let valid = true;
    let y = document.querySelector('input[name="y"]').value;
    let yField = document.querySelector('input[name="y"]');

    if (!validateNumberInString(y)) {
        showTooltip("Введите целое или дробное число!", yField);
        valid = false;
    } else if (!validateRange(y, -5, 3)) {
        showTooltip("Диапазон значений y -5 ... 3", yField);
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