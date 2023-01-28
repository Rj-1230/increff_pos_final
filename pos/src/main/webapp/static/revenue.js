function init()
{
  var d = new Date();
    d.setUTCHours(0,0,0,0);
    var today = d.toISOString().substr(0, 10);
    var firstDay = today.substr(0,8)+"01";
        document.getElementById("inputStartDate").value = firstDay;
        document.getElementById("inputEndDate").value = today;
        inputEndDate.max=new Date().toISOString().split("T")[0];
        inputStartDate.max=new Date().toISOString().split("T")[0];

}

$(document).ready(init);