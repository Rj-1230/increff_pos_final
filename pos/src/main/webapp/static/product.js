var role;
var newBrands = {};
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function getSupervisorProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/product";
}


 function getBrandOption() {
        selectElement = document.querySelector('#inputBrand');
        output = selectElement.options[selectElement.selectedIndex].value;
        return output;
    }

 function getCategoryOption() {
        selectElement = document.querySelector('#inputCategory');
        output = selectElement.options[selectElement.selectedIndex].value;
        return output;
    }

//BUTTON ACTIONS
function addProduct(event){
   var b = getCategoryOption();
	var $form = $("#product-form");
	if (!validateForm($form[0])){
	return ;
	}
	var json = toJson($form);
	var url = getSupervisorProductUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();     //...
	   		document.getElementById("product-form").reset();
	   		$('#create-product-modal').modal('hide');
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The product was added successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
           handleAjaxError(response);
	   }
	});

	return false;
}


function updateProduct(event){
	var id = $("#product-edit-form input[name=productId]").val();
	var url = getSupervisorProductUrl() + "/" + id;
	var $form = $("#product-edit-form");
	var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductList();
	   		$('#edit-product-modal').modal('hide');
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The product was updated successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
                  handleAjaxError(response);
       	   }
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});
}

function getBrandsList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandsList(data);
	   		displayCategoryList();
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});
}




function deleteProduct(id){
	var url = getSupervisorProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});
}

////UI DISPLAY METHODS

//All the products are listed with the delete and edit button having event handlers upon click
function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var j=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml=''
		if(role=='supervisor'){
		buttonHtml +='<button class="btn btn-dark" style="border:1px solid white;" onclick="displayEditProduct(' + e.productId + ')"> <i class="bi bi-pen"></i></button>'
		}
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.mrp.toFixed(2) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        j++;
	}
}

function displayCategoryList()
{
    var $elC = $("#inputCategory");
    $elC.empty();
    var a = getBrandOption();
    for(var i=0; i<newBrands[a].length; i++)
    {
        $elC.append($("<option></option>")
            .attr("value", newBrands[a][i]).text(newBrands[a][i]));
    }
}
function displayBrandsList(data)
{
    for(var i in data)
    {
        var a = data[i].brand;
        var b = data[i].category;
        if(!newBrands.hasOwnProperty(a))
            Object.assign(newBrands, {[a]:[]});
        newBrands[a].push(b);
    }

    var $elB = $("#inputBrand");

    $elB.empty();

    $.each(newBrands, function(key,value) {
          $elB.append($("<option></option>")
             .attr("value", key).text(key));
        });

}


//As the edit button is clicked in the table , its id is passed to this method and the method is called
function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);     //...
	   },
	   error: function(response){
                         handleAjaxError(response);
              	   }
	});
}

function displayProduct(data){
	$("#product-edit-form input[name=barcode]").val(data.barcode);
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=brand]").val(data.brand);
		$("#product-edit-form input[name=mrp]").val(data.mrp);
			$("#product-edit-form input[name=category]").val(data.category);
	$("#product-edit-form input[name=productId]").val(data.productId);
	$('#edit-product-modal').modal('toggle');
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
    	var title = Object.keys(fileData[0]);
            	if(title[0]!='barcode' || title[1]!='brandName' || title[2]!='categoryName' || title[3]!='mrp' || title[4]!='name' || title.length!=5){
                        incorrectTSV();
                    return;
            	}
	var url = getSupervisorProductUrl();
	uploadRows(url);
}
function uploadData(){
document.getElementById('sample-file').innerHTML='<a href="../sample/products.tsv" target="_blank">Download Sample</a>';
displayUploadData();
}

function getList(){
getProductList();
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
    $('#inputBrand').change(displayCategoryList);
	$('#upload-data').click(uploadData);
    $('#process-data').click(processData);
    $('#download-errors').click(downloadErrors);
    $('#myFile').on('change', updateFileName)
        $('#myFile').on('change', renewUpload)

    role= $("meta[name=role]").attr("content");

}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandsList);
