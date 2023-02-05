var newBrands = [];
var newCategs = [];
var reportData;
var fileName="";
function getBasicUrl()
{
    var baseUrl = $("meta[name=baseUrl]").attr("content")
     	return baseUrl + "/api/report/";
}

function getBrandsList()
{
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    var url = baseUrl+"/api/brand";
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
	    document.getElementById("download-report").style.display = "block";
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
        var j=1;
        for(var i in data){
        		var e = data[i];
        		var row = '<tr>'
        		+ '<td>' + j + '</td>'
        		+ '<td>' + e.brand + '</td>'
        		+ '<td>'  + e.category + '</td>'
        		+ '<td>'  + e.productName + '</td>'
        		+ '<td>'  + e.quantity + '</td>'
        		+ '</tr>';
                $tbody.append(row);
                total_quantity+=e.quantity;

        	}
        	var row = '<tr>'
                                    		+ '<td colspan=4>' + "<font size='+2'><b>Total</b></font>"+ '</td>'

                                    		+ '<td>'  +  "<b><font size='+2'>"+total_quantity +  "</b></font>"+'</td>'

                                    		+ '</tr>';
                                    		$tbody.append(row);
}

function getFilteredInventoryReport(event)
{
    var $form = $("#filter-brand-category-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"inventory-brand-category";
        var data_arr = JSON.parse(json);
        fileName = "inventory_report_"+data_arr.brand+"_"+data_arr.category+".tsv";
        console.log(fileName);
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   	$('#inventory-report-filter-modal').modal('hide');
    	    document.getElementById("filter-brand-category-form").reset();
    	   reportData=data;
    	   displayInventoryReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
                   	       	   	$('#inventory-report-filter-modal').modal('show');

    	   }
    	});

    	return false;
}

function downloadReport(){
	writeReportData(reportData,fileName);
}


function displayDailySalesReportList(data)
{
if(data.length>0){
	    document.getElementById("dailySales-report-table-container").style.display = "block";
	    document.getElementById("download-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There were no invoiced orders in given date range";
                $(".toast").toast('show');
	}

    var $tbody = $('#dailySales-report-table').find('tbody');
        $tbody.empty();
        var j=1;
        for(var i in data){
        		var e = data[i];

                    var invoiceDate = dateToISOLikeButLocal(new Date(e.invoiceDate*1000));
//        		var invoiceDate = e.date.dayOfMonth +"-"+e.date.monthValue+"-"+e.date.year;
        		var row = '<tr>'
        		+ '<td>' + j + '</td>'
        		+ '<td>' + invoiceDate + '</td>'
        		+ '<td>'  + e.invoicedItemsCount + '</td>'
        		+ '<td>'  + e.invoicedOrderCount + '</td>'

        		+ '<td>'  + e.totalRevenue.toFixed(2) + '</td>'
        		+ '</tr>';
                $tbody.append(row);
        	}
}

function getFilteredDailySalesReport(event)
{
    var $form = $("#filter-date-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"dailyReportFilter";
    	        var data_arr = JSON.parse(json);
                fileName = "daily_sales_report_"+data_arr.start+"_to_"+data_arr.end+".tsv";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
            $('#daily-sales-report-filter-modal').modal('hide');

    	   reportData=data;
    	   displayDailySalesReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

function displayRevenueProductList(data)
{
if(data.length>0){
	    document.getElementById("revenue-report-table-container").style.display = "block";
	    document.getElementById("download-report").style.display = "block";
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
    var j=1;
    var total_prod_revenue=0;
    for(var i in data){
    		var e = data[i];
    		console.log(e);
    		var row = '<tr>'
    		+ '<td>' + j + '</td>'
    		+ '<td>' + e.brand + '</td>'
    		+ '<td>' + e.category + '</td>'
    		+ '<td>'  + e.productName + '</td>'
    		+ '<td>'  + e.quantity + '</td>'
    		+ '<td>'  + e.total.toFixed(2) + '</td>'
    		+ '</tr>';
            $tbody.append(row);
            total_prod_quantity+=e.quantity;
            total_prod_revenue+=e.total;
    	}
    	var row = '<tr>'
            		+ '<td colspan=4>' + "<font size='+2'><b>Total</b></font>"+ '</td>'
            		+ '<td>'  +  "<b><font size='+2'>"+total_prod_quantity +  "</b></font>"+'</td>'
            		+ '<td>'  + "<b><font size='+2'>&#8377;"+total_prod_revenue.toFixed(2) +"</b></font>"+ '</td>'
            		+ '</tr>';
            		$tbody.append(row);
}


function getFilteredRevenueReport(event)
{
    var $form = $("#filter-date-brand-category-form");
    	var json = toJson($form);
    	var url = getBasicUrl()+"revenue-brand-category";
    	        var data_arr = JSON.parse(json);
                fileName = "revenue_report_"+data_arr.start+"_to_"+data_arr.end+"_"+data_arr.brand+"_"+data_arr.category+".tsv";
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
           $('#revenue-report-filter-modal').modal('hide');
    	   reportData=data;
    	   displayRevenueProductList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}



function displayBrandReportList(data)
{
if(data.length>0){
	    document.getElementById("brand-report-table-container").style.display = "block";
	    document.getElementById("download-report").style.display = "block";
	}
	else{
                document.getElementById('toast-container').classList.remove('bg-warning','bg-danger','bg-success');
                document.getElementById('toast-container').classList.add('bg-danger');
                document.getElementById('my-message').innerHTML="There is no such existing brand-category combination";
                $(".toast").toast('show');
	}
	var j=1;
    var $tbody = $('#brand-report-table').find('tbody');
        $tbody.empty();
        for(var i in data){
        		var e = data[i];
        		var row = '<tr>'
        		+ '<td>' + j + '</td>'
        		+ '<td>' + e.brand + '</td>'
        		+ '<td>'  + e.category + '</td>'
        		+ '</tr>';
                $tbody.append(row);
                j++;
        	}
}



function getFilteredBrandReport(event)
{
//document.getElementById("brand-report-table").style.display="block";
    var $form = $("#filter-brand-category-form");
    	var json = toJson($form);
    	var data_arr = JSON.parse(json);
    	fileName = "brand_report_"+data_arr.brand+"_"+data_arr.category+".tsv";
    	var url = getBasicUrl()+"brand-category";

    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data,fileName) {
           $('#brand-report-filter-modal').modal('hide');
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

function writeReportData(arr,fileName){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
    console.log(fileName);
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, fileName);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', fileName);
    tempLink.click();
}

function dateToISOLikeButLocal(date) {
//Iska koi kaam ni h : method sahi h but return hi kr ra h ISt me
//    console.log(date);
    const offsetMs = date.getTimezoneOffset() * 60 * 1000;
//    console.log(offsetMs);
    const msLocal =  date.getTime() - offsetMs;
//    console.log(msLocal);
    const dateLocal = new Date(msLocal);
//    console.log(dateLocal);
    const iso = dateLocal.toISOString();
//    console.log(iso);
    const isoLocal = iso.slice(0, 10);
    return isoLocal;
}

function init()
{
    $('#download-report').click(downloadReport);
    $('#get-inventory-report').click(getFilteredInventoryReport);
    $('#get-dailySales-report').click(getFilteredDailySalesReport);
    $('#get-brand-report').click(getFilteredBrandReport);
    $('#get-revenue-report').click(getFilteredRevenueReport);
}

$(document).ready(init);
$(document).ready(getBrandsList);