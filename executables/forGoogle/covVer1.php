<?php
if (isset($_POST['runNow'])){
	$inputA = $_POST['valA'];
	$inputK = $_POST['valK'];
	$inputB = $_POST['valB'];
	$inputv = $_POST['valv'];
	$inputQ = $_POST['valQ'];
	$inputC = $_POST['valC'];
	$inputM = $_POST['valM'];
	$inputX = $_POST['valX'];
	$inputE = $_POST['valE'];
	$inputR = $_POST['valR'];
	exec("./covVer1.sh $inputA $inputK $inputB $inputv $inputQ $inputC $inputM $inputX $inputE $inputR");
}

if (isset($_POST['deleteData'])){
	exec("./covVer1ClearData.sh");
}

?>

<html>
	<head>
		<title>Covariance work, Version 1, January 2015</title>
	</head>
	<body>

		<form name='covVer1' method='post' action='covVer1.php'>

			<div style='width: 400px; margin-left: auto; margin-right: auto; border: 1px solid blue; margin-top: 10px' >
				<table style='width: 100%;'>
					<tr style='background-color: #003f87; color: white;'>
						<td colspan=2 style='padding: 2px;'>Input</td>
					</tr>

					<tr>
						<td>Value of A</td>
						<td><input type="Text" name = "valA" size="5" value="0"> </td>
					</tr>

					<tr>
						<td>Value of K</td>
						<td><input type="Text" name = "valK" size="5" value="1"></td>
					</tr>

					<tr>
						<td>Value of B</td>
						<td><input type="Text" name = "valB" size="5" value="3"></td>
					</tr>

					<tr>
						<td>Value of v</td>
						<td><input type="Text" name = "valv" size="5" value="0.5"></td>
					</tr>

					<tr>
						<td>Value of Q</td>
						<td><input type="Text" name = "valQ" size="5" value="0.5"></td>
					</tr>

					<tr>
						<td>Value of C</td>
						<td><input type="Text" name = "valC" size="5" value="1"></td>
					</tr>

					<tr>
						<td>Value of M</td>
						<td><input type="Text" name = "valM" size="5" value="1"></td>
					</tr>

					<tr>
						<td>Value of X</td>
						<td><input type="Text" name = "valX" size="5" value="-2"> X-axis lower</td>
					</tr>

					<tr>
						<td>Value of E</td>
						<td><input type="Text" name = "valE" size="5" value="4"> X-axis upper</td>
					</tr>

					<tr>
						<td>Value of R</td>
						<td><input type="Text" name = "valR" size="5" value="0.2"> Increment</td>
					</tr>

				</table>

			</div>

			<div style='width: 400px; margin-left: auto; margin-right: auto; border: 1px solid blue; margin-top: 10px;'>

				<table style='width: 100%;'>
					<tr style='background-color: #003f87; color: white;'>
						<td style='padding: 2px;'>Invoke</td>
					</tr>

					<tr>
						<td><input type="Submit" name="runNow" valuue="Run">&nbsp;
						<input type="Submit" name="deleteData" value = "Reset"></td>
					</tr>

				</table>
			</div>

			<div style='width: 400px; margin-left: auto; margin-right: auto; border: 1px solid blue; margin-top: 10px;'>

				<table style='width: 100%;'>
					<tr style='background-color: #003f87; color: white;'>
						<td style='padding: 2px;'>Output</td>
					</tr>

					<tr>
						<td>
							<?php
							if (file_exists("covVer1Plot.png")){
								#echo "value of var is $inputR";
								echo "<img src='covVer1Plot.png' width='390px'>";
							}else{
								echo "No data yet";
							}
							?>

						</td>
					</tr>
				</table>
			</div>

			<div style='width: 400px; margin-left: auto; margin-right: auto; border: 1px solid blue; margin-top: 10px'>

				<table style='width: 100%;'>

					<tr style='background-color: #003f87; color: white;'>
						<td colspan=2 style='padding: 2px;'>About</td>
					</tr>
					<tr>
						<td>Sean Beseler, Filip Jagodzinki<br/>
							More details here<br/>
							<a href="covVer1.sh" target="_blank">covVer1.sh</a><br/>
							<a href="covVer1.java" target="_blank">covVer1.java</a><br/>

						</td>
					</tr>
				</table>
			</div>


		</form>
	</body>
</html>



