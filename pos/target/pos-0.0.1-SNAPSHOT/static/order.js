var counterId;
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}
function getSupervisorOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/supervisor/order";
}

function getCartUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/cart";
}

function getInvoiceUrl(){
  	var baseUrl = $("meta[name=baseUrl]").attr("content")
  	return baseUrl + "/api/invoice";
  }

function addOrder(event){
	var $form = $("#customer-form");
	var json = toJson($form);
	var url = getCartUrl() + 'PushToOrder';
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
            getOrderList();
            	$('#customerModal').modal('toggle');
            	$('#create-order-modal').modal('toggle');
                document.getElementById('cart-form').reset();
//            var baseUrl = $("meta[name=baseUrl]").attr("content");
//            window.location.href = baseUrl + "/ui/orders";
            document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
            document.getElementById('toast-container').classList.add('bg-success');
            document.getElementById('my-message').innerHTML="The order was created successfully";
            $(".toast").toast('show');
	   },
	   error: function(response){
              	   		 handleAjaxError(response);
              	   }
	});

	return false;
}


function placeOrder (orderId) {
	var url = getOrderUrl() + "/place/" + orderId;
    $.ajax({
    	   url: url,
    	   type: 'PUT',
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   getOrderList();
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


function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	    error: function(response){
                               	   		 handleAjaxError(response);
                               	   }
	});
}

function redirect(orderCode)
{
    var url = $("meta[name=baseUrl]").attr("content") + "/ui/orderItem/" + orderCode;
    window.location.href = url;
}

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		if(e.status=="invoiced"){
		var buttonHtml ='<button class="btn btn-success" onclick=downloadInvoice("'+e.orderCode + '")> Download Invoice <i class="bi bi-receipt-cutoff"></i></a></button>'
		}
		else{
		var buttonHtml ='<button class="btn btn-dark" onClick=redirect("'+ e.orderCode +'")><i class="bi bi-pen"></i></button>'
		buttonHtml+='&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-primary" onclick="placeOrder('+e.orderId + ')"> Place Order </a></button>'
		}
		var row = '<tr>'
		+ '<td>' + e.orderId + '</td>'
		+ '<td>' + e.customerName + '</td>'
		+ '<td>' + e.customerPhone + '</td>'
		+ '<td>' + e.orderCreateTime + '</td>'
		+ '<td>' + e.orderPlaceTime + '</td>'
		+ '<td>' + e.status + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function getCartItemList(){
	var url = getCartUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCartItemList(data);
	   },
	    error: function(response){
                                      	   		 handleAjaxError(response);
                                      	   }
	});
}

function displayCartItemList(data){
	if(data.length>0){
	    document.getElementById("create-new-order").style.display = "block";
	    document.getElementById("empty-cart").style.display = "block";
	    document.getElementById("cartItem-table").style.display = "block";
	}
	else{
//	Ye else part isliye taki jb koi manually sare dleete kre ya empty cart kre
		    document.getElementById("create-new-order").style.display = "none";
    	    document.getElementById("empty-cart").style.display = "none";
    	    document.getElementById("cartItem-table").style.display = "none";
	}
	var $tbody = $('#cartItem-table').find('tbody');
	$tbody.empty();
    var j=1;
	for(var i in data){
		var e = data[i];
		if(e.quantity==0 || e.counterId!=counterId){
		continue;
		}
		var buttonHtml ='<button class="btn btn-warning" onclick="displayEditCartItem(' + e.cartItemId + ')" >Edit </button>'
		buttonHtml += '&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-danger" onclick="deleteCartItem(' + e.cartItemId + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' +j + '</td>'
		+ '<td>' + e.productName + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice+ '</td>'
		+ '<td>' + e.counterId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        j++;
	}
	}

	function addItemToCart(event){
    	//Set the values to update
    	var $form = $("#cart-form");
    	var json = toJson($form);
    	var url = getCartUrl();
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		getCartItemList();
    	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-success');
                document.getElementById('my-message').innerHTML="The item was adeed to cart";
                $(".toast").toast('show');
    	   },
    	    error: function(response){
                                          	   		 handleAjaxError(response);
                                          	   }
    	});

    	return false;
    }

    function emptyCart(event){

    	var url = getCartUrl() + "Flush";
    	$.ajax({
    	   url: url,
    	   type: 'DELETE',
    	   success: function(data) {
    	   		getCartItemList();
    	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                                document.getElementById('toast-container').classList.add('bg-success');
                                document.getElementById('my-message').innerHTML="The cart was emptied";
                                $(".toast").toast('show');

                document.getElementById("create-new-order").style.display = "none";
                document.getElementById("empty-cart").style.display = "none";
                document.getElementById("cartItem-table").style.display = "none";

    	   },
    	    error: function(response){
                                          	   		 handleAjaxError(response);
                                          	   }
    	});

    	return false;
    }


    function updateCart(event){
$('#edit-cart-modal').modal('toggle');
    	var id = $("#cart-edit-form input[name=cartItemId]").val();
    	var url = getCartUrl() + "/" + id;
    	var $form = $("#cart-edit-form");
    	var json = toJson($form);
    var handleBarcode = JSON.parse(json);
    handleBarcode.barcode="abcd";
    json=JSON.stringify(handleBarcode);
    //    The form is converted to JSON and a PUT request (update) is called and thus details are updated
    	$.ajax({
    	   url: url,
    	   type: 'PUT',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(response) {
    	   		getCartItemList();
    	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                                document.getElementById('toast-container').classList.add('bg-success');
                                document.getElementById('my-message').innerHTML="The cart item was updated";
                                $(".toast").toast('show');
    	   },
    	   error: function(response){
                                         	   		 handleAjaxError(response);
                                         	   }
    	});

    	return false;
    }



function deleteCartItem(id){
	var url = getCartUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getCartItemList();
	   		document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
           document.getElementById('toast-container').classList.add('bg-success');
           document.getElementById('my-message').innerHTML="The item in cart was deleted";
           $(".toast").toast('show');
	   },
	    error: function(response){
                                      	   		 handleAjaxError(response);
                                      	   }
	});
}

function displayEditCartItem(id){
	var url = getCartUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayCartItem(data);
	   },
	   error: function(response){
                                     	   		 handleAjaxError(response);
                                     	   }
	});
}

function displayCartItem(data){
	$("#cart-edit-form input[name=sellingPrice]").val(data.sellingPrice);
	$("#cart-edit-form input[name=quantity]").val(data.quantity);
	$("#cart-edit-form input[name=cartItemId]").val(data.cartItemId);
	$("#cart-edit-form input[name=barcode]").val("abcd");
	document.getElementById("edit-cart-modal").style.zIndex = "9999";
	document.getElementById("edit-cart-modal").style.backgroundColor = "rgba(0, 0, 0, 0.4)";
	$('#edit-cart-modal').modal('toggle');
}

function createNewOrder(){
    document.getElementById("customerModal").style.zIndex = "9999";
	document.getElementById("customerModal").style.backgroundColor = "rgba(0, 0, 0, 0.4)";
	$('#customerModal').modal('toggle');
}

function getAllOrders(){
	var url = getSupervisorOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	    error: function(response){
             handleAjaxError(response);
        }
	});
}

function downloadInvoice(orderCode)
{
    var url = getInvoiceUrl() + "/" + orderCode;
    console.log(url);
    window.location.href = url;
}


function init(){
counterId = $("meta[name=counterId]").attr("content")
    $('#update-cart').click(updateCart);
	$('#create-order').click(addOrder);
	$('#add-cartItem').click(addItemToCart);
	$('#empty-cart').click(emptyCart);
    $('#create-new-order').click(createNewOrder);
	$('#refresh-data').click(getOrderList);
    $('#show-all-orders').click(getAllOrders);
    $('#show-my-orders').click(getOrderList);
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getCartItemList);