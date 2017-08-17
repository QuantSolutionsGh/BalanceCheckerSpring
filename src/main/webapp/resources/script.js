/**
 * 
 */
var oTable;
var dTable;

jQuery(document).ready(function() {

	$("#searchValue").keyup(function(event) {
		if (event.keyCode == 13) {
			$("#btn-search").click();
		}
	});

	oTable = $('#jsontable').DataTable({
		responsive : true,
		autoWidth : false,
		searching : false,
		ordering : false
	});

	dTable = $('#fund-jsontable').DataTable({
		responsive : true,
		searching : false,
		ordering : false,
		paging : false,
		bInfo : false
	});

});

$('#btn-search').on('click', function(e) {

	// Prevent the form from submitting via the browser.
	//<p><i class="fa fa-spin fa-spinner"></i> Loading...</p>
	e.preventDefault();

	searchViaAjax();
	$.blockUI({
		css : {
			border : 'none',
			padding : '15px',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			opacity : .2,
			color : '#fff'
		}
	});

});

$('#jsontable').on('click', 'tbody .btn', function() {

	var data_row = oTable.row($(this).closest('tr')).data();
	$.blockUI({
		css : {
			border : 'none',
			padding : '15px',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			opacity : .2,
			color : '#fff'
		}
	});

	getMutualFunds(data_row[0], data_row[9]);
});

$('#jsontable').on('click', 'tbody .issAcc', function() {

	var data_row = oTable.row($(this).closest('tr')).data();
	
	
	
	var search = {}
	search["docRef"] = data_row[10];
	search["sourceSystem"] = 'SV';
	var link="http://10.179.253.225:8080/DocMan/getdocs?sourceSystem=SV&docRef="+data_row[10];
	
	
	
	window.open(link, "_blank", "scrollbars=yes,resizable=yes,width=1000,height=600");
	
	
});


$('#jsontable').on('click', 'tbody .milesAcc', function() {

	var data_row = oTable.row($(this).closest('tr')).data();
	
	
	
	var search = {}
	
	search["sourceSystem"] = 'JB';
	var link="http://10.179.253.225:8080/DocMan/getdocs?sourceSystem=JB&docRef="+data_row[8];
	
	
	
	window.open(link, "_blank", "scrollbars=yes,resizable=yes,width=1000,height=600");
	
	
});




function getMutualFunds(accName, accno) {

	$.ajax({

		type : "GET",
		// contentType : "application/json",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8", // this
		// is
		// the
		// default
		// value,
		// so
		// it's
		// optional
		url : "getAccountDetails/" + accno + "/",
		// data : JSON.stringify(search),
		dataType : 'json',
		timeout : 100000,
		success : function(s) {
			$.unblockUI();
			
			$("#modal-header").html(accName);
			$("#myModal").modal('show');
			dTable.clear().draw();
			for (var i = 0; i < s.length; i++) {
				dTable.row.add(
						[ s[i]["mutualFund"], formatNumber(s[i]["units"]),
								formatNumber(s[i]["balance"]) ]).draw();
			} // End For

		},
		error : function(e) {
			console.log("ERROR: ", e);
			$.unblockUI();

		}

	});

};

function formatNumber(yourNumber) {
	// Seperates the components of the number
	yourNumber=yourNumber.toFixed(2);
	
	var n = yourNumber.toString().split(".");
	// Comma-fies the first part
	n[0] = n[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	// Combines the two sections
	
	return n.join(".");
};

function searchViaAjax() {

	var search = {}
	search["searchValue"] = $("#searchValue").val();
	search["searchOption"] = $("#searchOption").val();

	$
			.ajax({
				type : "GET",
				// contentType : "application/json",
				contentType : "application/x-www-form-urlencoded; charset=UTF-8", // this
				// is
				// the
				// default
				// value,
				// so
				// it's
				// optional
				url : "getCustList"+"/"+search["searchOption"]+"/"+search["searchValue"],
				// data : JSON.stringify(search),
				//data : search,
				dataType : 'json',
				timeout : 100000,
				success : function(s) {
					$.unblockUI();
					console.log(s);
					oTable.clear().draw();
					for (var i = 0; i < s.length; i++) {

						oTable.row
								.add(
										[ s[i]["name"], 
											"<a href='#' class='issAcc'>"+s[i]["issAcc"]+"</a>",
											"<a href='#' class='milesAcc'>"+s[i]["milesAcc"]+"</a>",
												
												s[i]["mobilePhone"],
												s[i]["maidenName"],
												s[i]["address1"],
												s[i]["email"],
												"<button class='btn btn-success' >Account Details</button>",s[i]["docRef"],s[i]["milesAcc"],s[i]["issAcc"] ])
								.draw();
					} // End For

				},
				error : function(requestObject, error, errorThrown) {
					console.log("ERROR: ", error);
				
					$.unblockUI();
					
					BootstrapDialog.show({
			            title: 'ERROR',
			            message: requestObject.responseText,
			           // message : 'Kindly crosscheck the account number entered.',
			          
			            type : BootstrapDialog.TYPE_DANGER
			        });

				}
			});
}