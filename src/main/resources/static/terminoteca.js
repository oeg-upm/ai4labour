/**
 * This is necessary for including HTML: used by footer and header.
 **/
function includeHTML() {
    var z, i, elmnt, file, xhttp;
    console.log("Including html...");
    /* Loop through a collection of all HTML elements: */
    z = document.getElementsByTagName("*");
    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        /*search for elements with a certain atrribute:*/
        file = elmnt.getAttribute("w3-include-html");
        if (file) {
            //file = file.replace("/blog/", "/posts/"); //apocrifa
            /* Make an HTTP request using the attribute value as the file name: */
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4) {
                    if (this.status == 200) {
                        elmnt.innerHTML = this.responseText;
                    }
                    if (this.status == 404) {
                        elmnt.innerHTML = "Page not found.";
                    }
                    /* Remove the attribute, and call this function once more: */
                    elmnt.removeAttribute("w3-include-html");
                    includeHTML();

                }
            }
            xhttp.open("GET", file, true);
            xhttp.send();
            /* Exit the function: */
            return;
        }
    }
}


/**
//This is the script necessary to control the DRAG&DROP
 **/
function dragOverHandler(ev) {
    ev.preventDefault();
}
async function dropHandler(ev) {
    // Prevent default behavior (Prevent file from being opened)
    ev.preventDefault();

    if (ev.dataTransfer.items) {
        // Use DataTransferItemList interface to access the file(s)
        for (var i = 0; i < ev.dataTransfer.items.length; i++) {
            // If dropped items aren't files, reject them
            if (ev.dataTransfer.items[i].kind === 'file') {
                var archivo = ev.dataTransfer.items[i].getAsFile();
                console.log('Vamos a enviar el archiv: ' + archivo.name);
                let formData = new FormData();
                formData.append("file", archivo);
//                document.body.style.cursor = 'wait';
                document.getElementById("idloader1").style.visibility = 'visible';
                const response = await fetch('/api/upload/', {method: "POST", body: formData});
                const jresponse = await response.json();
                var sresponse = JSON.stringify(jresponse, null, 2);
                document.getElementById("idloader1").style.visibility = 'hidden';
  //              document.body.style.cursor = 'normal';
                if (jresponse.success==='false')
                {
                    var x = document.getElementById("snackbar");
                    x.className = "show";
                    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);                
                    console.log("Fail: "+sresponse);
                }
                else
                {
                    console.log("Hooray: "+sresponse);
                    loadschemes();
                }
            }
        }
    } /*else {
        // Use DataTransfer interface to access the file(s)
        for (var i = 0; i < ev.dataTransfer.files.length; i++) {
            let archivo = ev.dataTransfer.files[i];
            console.log('Vamos a enviar el archivo: ' + archivo.name);
            let formData = new FormData();
            formData.append("file", archivo);
            document.body.style.cursor = 'wait';
            const response = await fetch('/upload/', {method: "POST", body: formData});
            console.log(response);
            document.body.style.cursor = 'normal';
        }
    }*/
}
