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