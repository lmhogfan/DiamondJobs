$(document).ready(function() {
    var table = $('#test').DataTable( );

	$('#test tbody').on( 'click', 'tr', function () {
		$(this).toggleClass('selected');
	} );

	$('#button').click( function () {
		alert( table.rows('.selected').data().length +' row(s) selected' );
	} );
} );

//window.location = $(this).data("href")