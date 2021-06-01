window.onscroll = function() {myFunction()};

const search = document.getElementById("search-nav");
const content = document.getElementById("content");
let sticky = search.offsetTop;

function myFunction() {
    if (window.pageYOffset >= sticky) {
        search.classList.add("sticky");
        content.classList.add("padding");
    } else {
        search.classList.remove("sticky");
        content.classList.remove("padding");
    }
}