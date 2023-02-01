var role;
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getSupervisorInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/inventory";
}

//BUTTON ACTIONS
function addInventory(event){
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getSupervisorInventoryUrl()+"AddSub";

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,

	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
	   		document.getElementById("inventory-form").reset();
            $('#inventory-create-modal').modal('hide');
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The items were updated successfully in the inventory";
            $(".toast").toast('show');
	   },
	   error: function(response){
                  handleAjaxError(response);
       	   }
	});

	return false;
}


function updateInventory(event){
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	var url = getSupervisorInventoryUrl()+"AddSub";

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,

	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getInventoryList();
	   		document.getElementById("inventory-edit-form").reset();
            $('#inventory-edit-modal').modal('hide');
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The items were updated successfully in the inventory";
            $(".toast").toast('show');
	   },
	   error: function(response){
                  handleAjaxError(response);
       	   }
	});

	return false;
}

function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});
}

function deleteInventory(id){
	var url = getSupervisorInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();
	   },
	  error: function(response){
                        handleAjaxError(response);
             	   }
	});
}


function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml=''
		if(role=='supervisor'){
        		buttonHtml +='<button class="btn btn-dark" style="border:1px solid white;" onclick="displayEditInventory(' + e.productId + ')"> <i class="bi bi-pen"></i></button>'
       }
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.barcode + '</td>'
        + '<td>' + e.productName + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        j++;
	}
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});


}


function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$('#inventory-edit-modal').modal('toggle');
}


function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
    	    document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                        document.getElementById('toast-container').classList.add('bg-danger');
            	   		document.getElementById('my-message').innerHTML="The file data was too big. It can contain max 5000 rows";
                        $(".toast").toast('show');
                        return;
    	}
	var url = getSupervisorInventoryUrl()+"AddSub";
	uploadRows(url);
}
function uploadData(){
document.getElementById('sample-file').innerHTML='<a href="../sample/inventory.tsv" target="_blank">Download Sample</a>';
displayUploadData();
}

function getList(){
getInventoryList();
}

//INITIALIZATION CODE
function init(){
    role= $("meta[name=role]").attr("content");
    	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(uploadData);
        	$('#process-data').click(processData);
        	$('#download-errors').click(downloadErrors);
            $('#myFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getInventoryList);
