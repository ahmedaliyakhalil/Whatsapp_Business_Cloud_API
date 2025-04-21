<html xmlns:th="http://www.thymeleaf.org">
	<head>
		<meta charset="utf-8">
	
		<link th:href="@{/styles/cssandjs/main.css}" rel="stylesheet" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" crossorigin="anonymous">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
		<title>login page</title>
		<>
		<style>
			*{
				padding: 0;
				margin : 0;
				box-sizing: border-box;
			}
			body{
				background-color: rgb(4, 120, 54);
			}
			.row{
				background-color: white;
				border-radius: 30px;
				box-shadow: 12px 12px 22px rgb(16, 15, 15);
			}
			img{
				image-orientation: initial ;
				margin-top: 10px;
				margin-bottom: 10px;
				border-bottom-left-radius: 30px;
				border-top-right-radius: 30px;
				border-top-left-radius: 30px;
				border-bottom-right-radius: 30px;
				max-width: 100%;
            	height: auto;
				
			}
			.btn{
				border: none;
				outline: none;
				height: 50px;
				width: 100%;
				background-color: rgb(4, 120, 54);
				color: white;
				border-radius: 4px;
				font-weight: 200;
			}
			form{
				margin : 30px;
			}
		</style>

  	</head>
  	<body background="http://www.inzign.com/wp-content/uploads/2016/08/ws_WhatsApp_Background_1680x1050.jpg">
		<section class="form my-5 mx-6">

			<div class="container" >
				<div class="row no-gutters">
						<div>
							<div class="">
							<img src="https://4.bp.blogspot.com/-nQ2WsXOuoB8/XVFc_r3dqJI/AAAAAAAAHNE/aAKMTlj1uVosNktBF_xyRBkcIZpa1hp8gCLcBGAs/s1600/whatsapp-fb-awe.jpg" alt="whatsapp image2" style="float:right;width:700px;height:450px;">
						</div>
						

						<form th:action="@{/login}" th:object="${user}" method="post"> 	
							<div class=>
								<h1 class= "font-weight-bold py-2 ">WHATSAPP MESSAGING</h1>
								<h5 class >SENDING MESSAGE</h5>
							</div>
	
							<div class="form-row">
								<div class="col-lg-7 mt-3 mb-3 ">
									<label>CONTACT NUMBER :</label>
									<input type = "text" th:field="*{contact number}" name = "contact number" placeholder= "CONTACT NUMBER" class= "form-control" my-3 p-4>		
								</div>
							</div>
							<div class="form-row">
								<div class=" col-lg-7"> 
									<label>MESSAGE </label>
									<label for="quantity">(250 words):</label>
									<input type = "text" th:field="*{message}" name= "message" placeholder = "type here" class="form-control"my-3 p-4><br>
								</div>	
							</div>
							<div class="form-row">
								<div class=" col-lg-7"> 
									<br>NOTE: Currently the template data will be sent.<br>
								</div>	
							</div>
							<div class="form-row">
								<div class=" col-lg-7"> 
									<button type="submit" class="btn btn-outline-dark">SEND</button><br>
								</div>	
							</div>	
						</form>
					</div>
				</div>
			</div>
		</section>
		<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.5/dist/umd/popper.min.js" integrity="sha384-Xe+8cL9oJa6tN/veChSP7q+mnSPaj5Bcu9mPX5F5xIGE0DVittaqT5lorf0EI7Vk" crossorigin="anonymous"></script>
		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.min.js" integrity="sha384-kjU+l4N0Yf4ZOJErLsIcvOU2qSb74wXpOhqTvwVx3OElZRweTnQ6d31fXEoRD1Jy" crossorigin="anonymous"></script>
  	</body>
</html>

