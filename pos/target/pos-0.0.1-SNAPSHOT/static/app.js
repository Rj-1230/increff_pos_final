var role;
var fileData;
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
    document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
    document.getElementById('toast-container').classList.add('bg-danger');
	var myResponse = JSON.parse(response.responseText);
	document.getElementById('my-message').innerHTML=myResponse.message;
    $(".toast").toast('show');
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
console.log(arr);
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}



//File upload methods
 fileData = [];
var errorData = [];

var processCount = 0;


function processData(){
    document.getElementById('uploadDetails').style.display="block";
	var file = $('#myFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function uploadRows(url){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
//	console.log(fileData.length)
	if(processCount==fileData.length){
		return;
	}

	//Process next row
	var row = fileData[processCount];

	processCount++;

	var json = JSON.stringify(row);
	var url = url;
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows(url);
	   	    getList();

	   },
	   error: function(response){
	   		row.error=response.responseJSON.message;
	   		errorData.push(row);
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
            	document.getElementById('my-message').innerHTML="There was some error while uploading the files.Please download error file to see in detail";
                $(".toast").toast('show');
	   		uploadRows(url);
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}


function resetUploadDialog(){
	//Reset file name
	var $file = $('#myFile');
	$file.val('');
	$('#myFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#successCount').html("" + processCount-errorData.length);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#myFile');
	var fileName = $file.val();
	$('#myFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-file-modal').modal('toggle');
}




function init(){
    role= $("meta[name=role]").attr("content");
	if(role=='supervisor'){
	document.getElementById("nav-supervisor").style.display = "block";
	document.getElementById("actions-supervisor").style.display = "block";
	}
    	if(role=='operator'){
    	var x = document.getElementsByClassName("supervisor");
        var i;
        for (i = 0; i < x.length; i++) {
            x[i].style.display = 'none';
        }
        }
}
$(document).ready(init);
