//var newBrands = [];
//var newCategs = [];
//
//function getBrandUrl()
//{
//    var baseUrl = $("meta[name=baseUrl]").attr("content")
//     	return baseUrl + "/api/brand";
//}
//
//function getBasicUrl()
//{
//    var baseUrl = $("meta[name=baseUrl]").attr("content")
//     	return baseUrl + "/api/";
//}
//
//function displayBrandReportList(data)
//{
//    var $tbody = $('#brand-report-table').find('tbody');
//        $tbody.empty();
//        for(var i in data){
//        		var e = data[i];
//        		var row = '<tr>'
//        		+ '<td>' + e.brand + '</td>'
//        		+ '<td>'  + e.category + '</td>'
//        		+ '</tr>';
//                $tbody.append(row);
//        	}
//}
//
//
//function getBrandsList()
//{
//    var url = getBrandUrl();
//    $.ajax({
//    	   url: url,
//    	   type: 'GET',
//    	   success: function(data) {
//    	   		displayBrandsList(data);
//    	   }
//    	});
//   }
//
//   function displayBrandsList(data)
//   {
////       newBrands.push("All");
////       newCategs.push("All");
//       for(var i in data)
//       {
//           var a = data[i].brand;
//           var b = data[i].category;
//               if(!newBrands.includes(a)){
//                newBrands.push(a);
//           }
//            if(!newCategs.includes(b)){
//                 newCategs.push(b);
//            }
//       }
//       for(var i = 0; i<newBrands.length; i++){
//            var option = document.createElement("option");
//            option.text = newBrands[i];
//            option.value = newBrands[i];
//            var select = document.getElementById("inputFilterBrand");
//            select.appendChild(option);
//       }
//
//         for(var i = 0; i<newCategs.length; i++){
//                   var option = document.createElement("option");
//                   option.text = newCategs[i];
//                   option.value = newCategs[i];
//                   var select = document.getElementById("inputFilterCategory");
//                   select.appendChild(option);
//              }
//
//   }
//
//function applyBrandCategoryFilter(event)
//{
//    var $form = $("#filter-brand-category-form");
//    	var json = toJson($form);
//    	var url = getBasicUrl()+"brand-category";
//
//    	$.ajax({
//    	   url: url,
//    	   type: 'POST',
//    	   data: json,
//    	   headers: {
//           	'Content-Type': 'application/json'
//           },
//    	   success: function(data) {
//    	   displayBrandReportList(data);
//    	   },
//    	   error: function(response){
//               handleAjaxError(response);
//    	   }
//    	});
//
//    	return false;
//}
//
//function init()
//{
//    $('#apply-brand-category-filter').click(applyBrandCategoryFilter);
//}
//
//$(document).ready(init);
//$(document).ready(getBrandsList);