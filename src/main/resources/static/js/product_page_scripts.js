const current_image = document.getElementById('current_image');
const form = document.getElementById('change_form');
let isActive = false;
form.classList.add("hide");

function changeImage() {
    let target = event.target;
    let src = target.src;
    current_image.src = src;
}

function changeProduct() {
    if (isActive) {
        form.classList.remove("hide");
        isActive = false;
    } else {
        form.classList.add("hide");
        isActive = true;
    }
}