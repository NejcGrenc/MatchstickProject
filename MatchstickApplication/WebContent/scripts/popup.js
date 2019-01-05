
buttonThatOpensPopup = function(buttonId, popupId) {
    // Get the modal
    var modal = document.getElementById(popupId);

    // Get the button that opens the modal
    var btn = document.getElementById(buttonId);

    // When the user clicks the button, open the modal 
    btn.onclick = function() {
        modal.style.display = "block";
    }
}

closePopupButton = function(buttonId, popupId) {
    // Get the modal
    var modal = document.getElementById(popupId);

    // Get the <span> element that closes the modal
    var closeButton = document.getElementById(buttonId);

    // When the user clicks on <span> (x), close the modal
    closeButton.onclick = function() {
        modal.style.display = "none";
    }
}
