//Get the button
let back_to_top_btn = document.getElementById("btn-back-to-top");

// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function () {
    scrollFunction();
};

function scrollFunction() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        back_to_top_btn.style.display = "block";
    } else {
        back_to_top_btn.style.display = "none";
    }
}
// When the user clicks on the button, scroll to the top of the document
back_to_top_btn.addEventListener("click", backToTop);

function backToTop() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}
