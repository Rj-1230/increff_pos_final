var role;
var fileData
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}
function getSupervisorBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getSupervisorBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   		document.getElementById("brand-form").reset();
	   		$('#create-brand-modal').modal('hide');
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
	   		document.getElementById('my-message').innerHTML="The brand was added successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
           handleAjaxError(response);
	   }
	});

	return false;
}



function updateBrand(event){
    var id = $("#brand-edit-form input[name=id]").val();
	var url = getSupervisorBrandUrl() + "/" + id;
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
            $('#edit-brand-modal').modal('hide');
   	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The brand was updated successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
//	   $('#edit-brand-modal').modal('show');
           handleAjaxError(response);
	   }
	});

	return false;
}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);
	   },
	   error: function(response){
	   		 handleAjaxError(response);
	   }
	});
}

function deleteBrand(id){
	var url = getSupervisorBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandList();
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The brand was deleted successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
       	   		 handleAjaxError(response);
       	   }
	});
}


function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml=''
		if(role=='supervisor'){
		    buttonHtml+='<button class="btn btn-dark" style="border:1px solid white;" onclick="displayEditbrand(' + e.id + ')"> <i class="bi bi-pen"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        j++;
	}
}


function displayEditbrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: function(response){
       	   		 handleAjaxError(response);
       	   }
	});
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

function readFileDataCallback(results){
	fileData = results.data;
	var url = getSupervisorBrandUrl();
	uploadRows(url);

}
function uploadData(){
document.getElementById('sample-file').innerHTML='<a href="../sample/brand.tsv" target="_blank">Download Sample</a>';
displayUploadData();
}

function getList(){
getBrandList();
}

//INITIALIZATION CODE
function init(){
    role= $("meta[name=role]").attr("content");
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(uploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#myFile').on('change', updateFileName)
}


$(document).ready(init);
$(document).ready(getBrandList);
