var role;
var flagObj ={flag:false,id:0};
function getUserUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/user";
}

//BUTTON ACTIONS
function addUser(event){
	var $form = $("#user-form");
	var json = toJson($form);
	var url = getUserUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getUserList();
	   		document.getElementById("user-form").reset();
	   		$('#create-user-modal').modal('hide');
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
	   		document.getElementById('my-message').innerHTML="The user was added successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
           handleAjaxError(response);
	   }
	});

	return false;
}



function updateUser(event){
    var id = $("#user-edit-form input[name=id]").val();
	var url = getUserUrl() + "/" + id;
	var $form = $("#user-edit-form");
	var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getUserList();
            $('#edit-user-modal').modal('hide');
   	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The user was updated successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
//	   $('#edit-user-modal').modal('show');
           handleAjaxError(response);
	   }
	});

	return false;
}


function getUserList(){
	var url = getUserUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUserList(data);
	   },
	   error: function(response){
	   		 handleAjaxError(response);
	   }
	});
}

function deleteUser(id){
	var url = getUserUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getUserList();
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The user was deleted successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
       	   		 handleAjaxError(response);
       	   }
	});
}


function displayUserList(data){
	var $tbody = $('#user-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var showPassword = ' <button class="btn btn-dark" style="border:1px solid white;" onclick="toggle(' + e.id + ')"> <i class="bi bi-eye"></i></button>'
		var password = '********'
		if(flagObj.flag==true && flagObj.id==e.id){
		password = e.password;
		showPassword = '<button class="btn btn-dark" style="border:1px solid white;" onclick="toggle(' + e.id + ')"> <i class="bi bi-eye-slash"></i></button>'
		}
		var buttonHtml ='<button class="btn btn-dark" style="border:1px solid white;" onclick="displayEdituser(' + e.id + ')"> <i class="bi bi-pen"></i></button>'
		buttonHtml += '&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-danger" onclick="deleteUser(' + e.id + ')"><i class="bi bi-trash"></i></button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.email + '</td>'
		+ '<td>'  + password + '</td>'
        + '<td>'  + showPassword + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>'
        $tbody.append(row);
	}
	}

function toggle(id) {
if(flagObj.id!=id){
flagObj.flag=false;
}
flagObj.flag = !flagObj.flag
flagObj.id = id;
            getUserList();
        }



function displayEdituser(id){
	var url = getUserUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayUser(data);
	   },
	   error: function(response){
       	   		 handleAjaxError(response);
       	   }
	});
}

function displayUser(data){
	$("#user-edit-form input[name=email]").val(data.user);
	$("#user-edit-form input[name=password]").val(data.category);
	$("#user-edit-form input[name=id]").val(data.id);
	$('#edit-user-modal').modal('toggle');
}

function readFileDataCallback(results){
	fileData = results.data;
	var url = getUserUrl();
	uploadRows(url);
}
function uploadData(){

//sample file dynamic add krege
//document.getElementById('sample-file').innerHTML='<a th:href="[[@{/sample/user.tsv}]]" target="_blank">Download Sample</a>';
displayUploadData();
}

function getList(){
getUserList();
}

//INITIALIZATION CODE
function init(){
	$('#add-user').click(addUser);
	role= $("meta[name=role]").attr("content");
	$('#update-user').click(updateUser);
	$('#refresh-data').click(getUserList);
	$('#upload-data').click(uploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#myFile').on('change', updateFileName)
}


$(document).ready(init);
$(document).ready(getUserList);
