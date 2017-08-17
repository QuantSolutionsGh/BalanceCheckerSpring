<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="webjars/bootstrap-dialog/1.34.6/dist/css/bootstrap-dialog.min.css"
	rel="stylesheet" />
<link href="webjars/datatables/1.10.15/css/jquery.dataTables.min.css"
	rel="stylesheet" />


<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Instant Balance Checker</title>
</head>
<body>


	<div class="container">

		<div class="panel panel-success">
			<div class="panel-heading">CLIENT LOOKUP</div>
			<div class="panel-body">

				<div class="form-horizontal">
					<div class="form-group">

						<label class="control-label col-sm-2">Search Option</label>

						<div class="col-sm-2">
							<select class="form-control" id="sourceSystem">
								<option value="JB">JB Source System</option>
								<option value="SV">Signature Verication System</option>
								<option value="MILES">Miles</option>

							</select>

						</div>

						<div class="col-sm-5">

							<input type="text" id="searchValue" class="form-control"
								placeholder="Enter the document ref here">
						</div>

						<div class="col-sm-2">

							<button type="button" id="btn-search" class="btn btn-success">SEARCH</button>

						</div>

					</div>
				</div>



			</div>
		</div>

		<div id="search-Results">

			<table id="jsontable" class="display table table-bordered">
				<thead>
					<tr>
						<th>Name</th>
						<th>ISS Account #</th>
						<th>Miles Account #</th>
						<th>Telephone</th>
						<th>Maiden Name</th>
						<th>Cust Address</th>
						<th>Email</th>
						<th></th>
					</tr>
				</thead>

			</table>



		</div>


	</div>





	<script src="webjars/jquery/1.9.1/jquery.min.js"></script>

	<script src="webjars/jquery-blockui/2.65/jquery.blockUI.js"></script>

	<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<script src="webjars/datatables/1.10.15/js/jquery.dataTables.min.js"></script>
	<script
		src="webjars/bootstrap-dialog/1.34.6/dist/js/bootstrap-dialog.min.js"></script>

	<script type="text/javascript">
		jQuery(document).ready(function() {
			var oTable = $('#jsontable').dataTable();

		});

		$('#btn-search').on('click', function(e) {
			
			// Prevent the form from submitting via the browser.
			e.preventDefault();

			searchViaAjax();
			
			
		});
		
		function searchViaAjax() {

			var search = {}
			search["searchValue"] = $("#searchValue").val();
			search["sourceSystem"] = $("#sourceSystem").val();

			$
					.ajax({
						type : "GET",
						//contentType : "application/json",
						contentType : "application/x-www-form-urlencoded; charset=UTF-8", // this is the default value, so it's optional
						url : "/balancechecker/getData",
						//data : JSON.stringify(search),
						data : search,
						dataType : 'json',
						timeout : 100000,
						success : function(s) {
							console.log(s);
					        oTable.fnClearTable();
					        for(var i = 0; i < s.length; i++) { 
					          oTable.fnAddData([ s[i][0], s[i][1], s[i][2], s[i][3], s[i][4],s[i][5],s[i][6],s[i][7] ]);  
					        } // End For  

						},
						error : function(e) {
							console.log("ERROR: ", e);

						}
					});

		
		
		
	</script>


</body>
</html>