$('#statusId').on('change', function() {
  if ( this.value == '3')
    $("#notify").show();
  else
    $("#notify").hide();
}).trigger("change");