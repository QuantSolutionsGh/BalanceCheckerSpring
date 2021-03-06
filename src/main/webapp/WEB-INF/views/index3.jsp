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
<link
	href="webjars/datatables/1.10.15/css/dataTables.bootstrap4.min.css"
	rel="stylesheet" />
<link
	href="webjars/datatables-responsive/1.0.6/css/dataTables.responsive.css"
	rel="stylesheet" />
<script src="webjars/jquery/1.9.1/jquery.min.js"></script>

<script src="webjars/jquery-blockui/2.65/jquery.blockUI.js"></script>

<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script src="webjars/datatables/1.10.15/js/jquery.dataTables.min.js"></script>
<script src="webjars/datatables/1.10.15/js/dataTables.bootstrap4.min.js"></script>

<script
	src="webjars/datatables-responsive/1.0.6/js/dataTables.responsive.js"></script>
<script
	src="webjars/bootstrap-dialog/1.34.6/dist/js/bootstrap-dialog.min.js"></script>






<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Instant Balance Checker</title>
</head>
<body style="font-size: 12px">


	<div class="container">

		<div class="panel panel-warning">
			<div style="background-color: #4CAF50; color: white;"
				class="panel-heading">
				<STRONG>CLIENT LOOKUP</STRONG>
			</div>
			<div class="panel-body">

				<div class="form-horizontal">
					<div class="form-group">

						<label class="control-label col-sm-2">Search Option</label>

						<div class="col-sm-2">
							<select class="form-control" id="searchOption">
								<option value="name">Name</option>
								<option value="iss-acc">ISS Account Number</option>
								<option value="miles-acc">MILES Account Number</option>

							</select>

						</div>

						<div class="col-sm-5">

							<input type="text" id="searchValue" class="form-control"
								placeholder="Please enter search value here">
						</div>

						<div class="col-sm-2">

							<button type="button" id="btn-search" class="btn btn-warning">SEARCH</button>

						</div>

					</div>
				</div>



			</div>
		</div>

		<div class="table-responsive" id="search-Results">

			<table id="jsontable" width="100%" class="table table-striped  ">
				<thead style="background-color: #4CAF50; color: white;">
					<tr>
						<th width="28%">Name</th>
						<th width="10%">ISS Account #</th>
						<th width="12%">Miles Account #</th>
						<th width="5%">Telephone</th>
						<th width="18%">Maiden Name</th>
						<th width="12%">Cust Address</th>
						<th width="10%">Email</th>
						<th></th>
					</tr>
				</thead>

			</table>



		</div>

		<!-- Modal -->
		<div id="myModal" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Modal Header</h4>
					</div>
					<div class="modal-body">

						<div class="table-responsive" id="mutual-funds">

							<table id="fund-jsontable" width="100%"
								class="table table-striped  ">
								<thead style="background-color: #4CAF50; color: white;">
									<tr>
										<th>Mutual Fund</th>
										<th>Units</th>
										<th>Balance</th>

									</tr>
								</thead>

							</table>



						</div>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>


	</div>







	<script type="text/javascript">
		var oTable;
		jQuery(document).ready(function() {
			oTable = $('#jsontable').dataTable({
				responsive : true,
				searching : false,
				ordering : false
			});

		});

		$('#btn-search').on('click', function(e) {

			// Prevent the form from submitting via the browser.
			e.preventDefault();

			searchViaAjax();

		});

		function getMututalFunds(accName, accno) {
			$.ajax({
				
				type : "GET",
				//contentType : "application/json",
				contentType : "application/x-www-form-urlencoded; charset=UTF-8", // this is the default value, so it's optional
				url : "/getAccountDetails/"+accno,
				//data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(s) {
					console.log(s);
					oTable.fnClearTable();
					for (var i = 0; i < s.length; i++) {
						oTable
								.fnAddData([ s[i]["name"],
										s[i]["issAcc"],
										s[i]["milesAcc"],
										s[i]["mobilePhone"],
										s[i]["maidenName"],
										s[i]["address1"],
										s[i]["email"],
										"<button class='btn btn-warning' >Account Details</button>" ]);
					} // End For  

				},
				error : function(e) {
					console.log("ERROR: ", e);

				}
				
				
				
			});

		};

		function searchViaAjax() {

			var search = {}
			search["searchValue"] = $("#searchValue").val();
			search["searchOption"] = $("#searchOption").val();

			$
					.ajax({
						type : "GET",
						//contentType : "application/json",
						contentType : "application/x-www-form-urlencoded; charset=UTF-8", // this is the default value, so it's optional
						url : "/balancechecker/getCustList",
						//data : JSON.stringify(search),
						data : search,
						dataType : 'json',
						timeout : 100000,
						success : function(s) {
							console.log(s);
							oTable.fnClearTable();
							for (var i = 0; i < s.length; i++) {
								oTable
										.fnAddData([ s[i]["name"],
												s[i]["issAcc"],
												s[i]["milesAcc"],
												s[i]["mobilePhone"],
												s[i]["maidenName"],
												s[i]["address1"],
												s[i]["email"],
												"<button class='btn btn-warning' >Account Details</button>" ]);
							} // End For  

						},
						error : function(e) {
							console.log("ERROR: ", e);

						}
					});
		}
	</script>


</body>
</html>