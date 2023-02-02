var message;
function toastRegister(){
 document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-danger');
            document.getElementById('my-message').innerHTML="Please contact supervisor for login credentials";
            $(".toast").toast('show');
}

function init(){
    message= $("meta[name=message]").attr("content");
    document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML=message;
                if(message!=undefined){
                $(".toast").toast('show');
                }
}

$(document).ready(init);
