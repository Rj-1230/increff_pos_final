var orderId;
var status;
var customerName;
function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderItem";
}

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//After creation of an order, one can add more items to that order
function addOrderItem(event){
	var $form = $("#orderItem-form");
	var json = toJson($form);
	var url = getOrderItemUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getOrderItemList();
            location.reload();
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The item was added to order";
            $(".toast").toast('show');
	   },
	    error: function(response){
                     	   		 handleAjaxError(response);
                     	   }
	});

	return false;
}

function getOrder()
{
    var url = getOrderUrl() + "/" + orderId;
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   customerName=data.customerName;
    	   status = data.status;
           document.getElementById('customerName').innerHTML = customerName;
           document.getElementById('customerName2').innerHTML = customerName;
    	   disableButtons();
    	   getOrderItemList();
    	   },
    	   error: handleAjaxError
    	});

    	return false;
}

function disableButtons(){
      if(status=='invoiced'){
	    var x = document.getElementsByClassName("hideUponInvoiced");
                var i;
                for (i = 0; i < x.length; i++) {
                    x[i].style.display = 'none';
                }
                    document.getElementById('customer-container').classList.remove('alignleftt');
                    document.getElementById('customer-container').classList.add('text-center');

      }
}

function placeOrder () {
	var url = getOrderUrl() + "/invoice/" + orderId;
    $.ajax({
    	   url: url,
    	   type: 'PUT',
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   window.location.href="../orders"
    	   document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                       document.getElementById('toast-container').classList.add('bg-success');
                       document.getElementById('my-message').innerHTML="The order was placed successfully";
                       $(".toast").toast('show');
    	   },
    	  error: function(response){
                        	   		 handleAjaxError(response);
                        	   }
    	});

    	return false;

}

//Getting the list of all order items with a given orderId
function getOrderItemList(){
	var url = getOrderItemUrl() + "s/" + orderId;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItemList(data);

	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   }
	});
}

//Deleting a particular order-item
function deleteOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderItemList();
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The item was deleted from order";
            $(".toast").toast('show');
	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   }
	});
}

//To display all the order-items under a given order ID
function displayOrderItemList(data){
	var $tbody = $('#orderItem-table').find('tbody');
	$tbody.empty();
	var j=1;
	var totalRevenue=0;
		var totalQuantity=0;
	for(var i in data){
		var e = data[i];
		var buttonHtml='';
		if(status=='created'){
		buttonHtml ='<button class="btn btn-dark" style="border:1px solid white;" onclick="displayEditOrderItem(' + e.orderItemId + ')"> <i class="bi bi-pen"></i></button>'
        buttonHtml += '&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-danger" onclick="deleteOrderItem(' + e.orderItemId + ')"><i class="bi bi-trash"></i></button>'
        }
        else{
	   		document.getElementById('invoiced-action').innerHTML = 'Total';
	   		buttonHtml+= (e.quantity*e.sellingPrice).toFixed(2);
	   		totalQuantity+=e.quantity;
	   		totalRevenue+=(e.quantity*e.sellingPrice);
        }
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.productName + '</td>'
        + '<td>' + e.sellingPrice.toFixed(2)+ '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        j++;
	}
	if(status=='invoiced'){
	var row = '<tr>'
    		+ '<td colspan=4>' + "<font size='+2'><b>Total</b></font>"+ '</td>'
            + '<td>'+  "<b><font size='+2'>"+ totalQuantity +  "</b></font>"+'</td>'
    		+ '<td>'+  "<b><font size='+2'>"+ totalRevenue.toFixed(2) +"</b></font>"+'</td>'
    		+ '</tr>';
  $tbody.append(row);
  }
}

function displayEditOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItem(data);
	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   }
	});
}

function updateCustomerDetails(){
	var url = getOrderUrl() + "/" + orderId;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   console.log(data);
	   		displayCustomerDetails(data);
	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   }
	});
}


function displayCustomerDetails(data){
	$("#customer-edit-form input[name=customerName]").val(data.customerName);
	$("#customer-edit-form input[name=customerPhone]").val(data.customerPhone);
	$('#edit-customer-modal').modal('toggle');
}


function displayOrderItem(data){
	$("#orderItem-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$("#orderItem-edit-form input[name=quantity]").val(data.quantity);
	$("#orderItem-edit-form input[name=orderItemId]").val(data.orderItemId);
	$("#orderItem-edit-form input[name=orderId]").val(data.orderId);
	$("#orderItem-edit-form input[name=barcode]").val(data.barcode);
		console.log(data.productName);
	$("#orderItem-edit-form input[name=productName]").val(data.productName);

	$('#edit-orderItem-modal').modal('toggle');
}

function updateOrderItem(event){
	$('#edit-orderItem-modal').modal('toggle');
	var id = $("#orderItem-edit-form input[name=orderItemId]").val();
	var url = getOrderItemUrl() + "/" + id;
    var $form = $("#orderItem-edit-form");
	var json = toJson($form);
	console.log(json);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getOrderItemList();
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The item was updated in the order";
            $(".toast").toast('show');
	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   		 	   	$('#edit-orderItem-modal').modal('toggle');
                            	   }
	});

	return false;
}

function updateCustomer (event) {
	$('#edit-customer-modal').modal('toggle');
	var url = getOrderUrl() + "/" + orderId;
	var $form = $("#customer-edit-form");
	var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   location.reload()
	   document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
       document.getElementById('toast-container').classList.add('bg-success');
       document.getElementById('my-message').innerHTML="The customer details were successfully updated";
       $(".toast").toast('show');
	   },
	   error: function(response){
                            	   		 handleAjaxError(response);
                            	   }
	});

	return false;
}



//INITIALIZATION CODE
function init(){
	$('#add-orderItem').click(addOrderItem);
	orderId= $("meta[name=orderId]").attr("content");
	document.getElementById('inputOrderId').value=orderId;
    $('#updateCustomerDetails').click(updateCustomerDetails)
	$('#update-customer').click(updateCustomer)
    $('#update-order-item').click(updateOrderItem);
	$('#refresh-data').click(getOrderItemList);
	$('#invoice-order').click(placeOrder);
}
$(document).ready(init);
$(document).ready(getOrder);
