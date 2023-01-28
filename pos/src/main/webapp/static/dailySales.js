var newBrands = [];
var newCategs = [];

function getReportUrl()
{
    var baseUrl = $("meta[name=baseUrl]").attr("content")
     	return baseUrl + "/api/dailyReportFilter";
}

function displayDailySalesReportList(data)
{
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

function applyDateFilter(event)
{
    var $form = $("#filter-date-form");
    	var json = toJson($form);
    	var url = getReportUrl();
    	$.ajax({
    	   url: url,
    	   type: 'POST',
    	   data: json,
    	   headers: {
           	'Content-Type': 'application/json'
           },
    	   success: function(data) {
    	   displayDailySalesReportList(data);
    	   },
    	   error: function(response){
               handleAjaxError(response);
    	   }
    	});

    	return false;
}

function init()
{
    $('#apply-brand-category-filter').click(applyDateFilter);
}

$(document).ready(init);
$(document).ready(getBrandsList);