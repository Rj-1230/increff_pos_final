var newBrands = [];
var newCategs = [];
var reportData;
function getBasicUrl()
{
    var baseUrl = $("meta[name=baseUrl]").attr("content")
     	return baseUrl + "/api/";
}

function getBrandsList()
{
    var url = getBasicUrl()+"brand";
    $.ajax({
    	   url: url,
    	   type: 'GET',
    	   success: function(data) {
    	   		displayBrandCategoryList(data);
    	   }
    	});
   }



   function displayBrandCategoryList(data)
   {
        for(var i in data)
              {
                  var a = data[i].brand;
                  var b = data[i].category;
                      if(!newBrands.includes(a)){
                       newBrands.push(a);
                  }
                   if(!newCategs.includes(b)){
                        newCategs.push(b);
                   }
              }
              for(var i = 0; i<newBrands.length; i++){
                   var option = document.createElement("option");
                   option.text = newBrands[i];
                   option.value = newBrands[i];
                   var select = document.getElementById("inputFilterBrand");
                   select.appendChild(option);
              }

                for(var i = 0; i<newCategs.length; i++){
                          var option = document.createElement("option");
                          option.text = newCategs[i];
                          option.value = newCategs[i];
                          var select = document.getElementById("inputFilterCategory");
                          select.appendChild(option);
                     }
   }

function displayInventoryReportList(data)
{
if(data.length>0){
	    document.getElementById("inventory-report-table-container").style.display = "block";
	    document.getElementById("download-inventory-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There is no such existing brand-category combination";
                $(".toast").toast('show');
	}

    var $tbody = $('#inventory-report-table').find('tbody');
        $tbody.empty();
        var total_quantity =0;
        for(var i in data){
        		var e = data[i];
        		var row = '<tr>'
        		+ '<td>' + e.brand + '</td>'
        		+ '<td>'  + e.category + '</td>'
        		+ '<td>'  + e.quantity + '</td>'
        		+ '</tr>';
                $tbody.append(row);
                total_quantity+=e.quantity;

        	}
        	var row = '<tr>'
                                    		+ '<td colspan=2>' + "<font size='+2'><b>Total</b></font>"+ '</td>'

                                    		+ '<td>'  +  "<b><font size='+2'>"+total_quantity +  "</b></font>"+'</td>'

                                    		+ '</tr>';
                                    		$tbody.append(row);
}

function getFilteredInventoryReport(event)
{
    var $form = $("#filter-brand-category-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"inventory-brand-category";

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   reportData=data;
    	   displayInventoryReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

function downloadInventoryReport(){
//    reportData.forEach(function(v){delete v.id;})
	writeReportData(reportData);
}


function displayDailySalesReportList(data)
{
if(data.length>0){
	    document.getElementById("dailySales-report-table-container").style.display = "block";
	    document.getElementById("download-dailySales-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There were no invoiced orders in given date range";
                $(".toast").toast('show');
	}

    var $tbody = $('#dailySales-report-table').find('tbody');
        $tbody.empty();
        for(var i in data){
        		var e = data[i];
//        		var invoiceDate = e.date.dayOfMonth +"-"+e.date.monthValue+"-"+e.date.year;
        		var row = '<tr>'
        		+ '<td>' + e.date + '</td>'
        		        		+ '<td>'  + e.invoicedItemsCount + '</td>'
        		+ '<td>'  + e.invoicedOrderCount + '</td>'

        		+ '<td>'  + e.totalRevenue + '</td>'
        		+ '</tr>';
                $tbody.append(row);
        	}
}

function getFilteredDailySalesReport(event)
{
    var $form = $("#filter-date-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"dailyReportFilter";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   reportData=data;
    	   displayDailySalesReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

function downloadDailySalesReport(){
//    reportData.forEach(function(v){delete v.id;})
	writeReportData(reportData);
}


function displayRevenueProductList(data)
{
if(data.length>0){
	    document.getElementById("revenue-report-table-container").style.display = "block";
	    document.getElementById("download-revenue-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There are no orders invoiced in given date range. So, no revenue was generated";
                $(".toast").toast('show');
	}

    var $tbody = $('#product-revenue-list-table').find('tbody');
    $tbody.empty();
    var total_prod_quantity =0;
    var total_prod_revenue=0;
    for(var i in data){
    		var e = data[i];
    		console.log(e);
    		var row = '<tr>'
    		+ '<td>' + e.productId + '</td>'
    		+ '<td>' + e.barcode + '</td>'
    		+ '<td>'  + e.name + '</td>'
    		+ '<td>'  + e.mrp + '</td>'
    		+ '<td>'  + e.quantity + '</td>'
    		+ '<td>'  + e.total + '</td>'
    		+ '</tr>';
            $tbody.append(row);
            total_prod_quantity+=e.quantity;
            total_prod_revenue+=e.total;
    	}
    	var row = '<tr>'
            		+ '<td>' + "<font size='+2'><b>Total</b></font>"+ '</td>'
            		+ '<td>' +"" + '</td>'
            		+ '<td>'  + "" + '</td>'
            		+ '<td>'  + "" + '</td>'
            		+ '<td>'  +  "<b><font size='+2'>"+total_prod_quantity +  "</b></font>"+'</td>'
            		+ '<td>'  + "<b><font size='+2'>&#8377;"+total_prod_revenue +"</b></font>"+ '</td>'
            		+ '</tr>';
            		$tbody.append(row);
}


function getFilteredRevenueReport(event)
{
    var $form = $("#filter-date-brand-category-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"revenue-brand-category";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   reportData=data;
    	   displayRevenueProductList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

function downloadRevneueReport(){
    reportData.forEach(function(v){delete v.productId;})
	writeReportData(reportData);
}



function displayBrandReportList(data)
{
if(data.length>0){
	    document.getElementById("brand-report-table-container").style.display = "block";
	    document.getElementById("download-brand-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There is no such existing brand-category combination";
                $(".toast").toast('show');
	}
    var $tbody = $('#brand-report-table').find('tbody');
        $tbody.empty();
        for(var i in data){
        		var e = data[i];
        		var row = '<tr>'
        		+ '<td>' + e.brand + '</td>'
        		+ '<td>'  + e.category + '</td>'
        		+ '</tr>';
                $tbody.append(row);
        	}
}



function getFilteredBrandReport(event)
{
//document.getElementById("brand-report-table").style.display="block";
    var $form = $("#filter-brand-category-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"brand-category";

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   reportData=data;
    	   displayBrandReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

var reportData = [];

function writeReportData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'report.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'report.tsv');
    tempLink.click();
}

function downloadBrandReport(){
    reportData.forEach(function(v){delete v.id;})
	writeReportData(reportData);
}


function init()
{
    $('#download-inventory-report').click(downloadInventoryReport);
    $('#download-dailySales-report').click(downloadDailySalesReport);
    $('#download-revenue-report').click(downloadRevneueReport);
    $('#download-brand-report').click(downloadBrandReport);
    $('#get-inventory-report').click(getFilteredInventoryReport);
    $('#get-dailySales-report').click(getFilteredDailySalesReport);
    $('#get-brand-report').click(getFilteredBrandReport);
    $('#get-revenue-report').click(getFilteredRevenueReport);
}

$(document).ready(init);
$(document).ready(getBrandsList);