<html>
<head>
  <title> AJAX! </title>
  <script>
  function runAjax(inputVal){
	  var reqObj;
	  reqObj = new XMLHttpRequest();
	  reqObj.onreadystatechange = function(){
		  if (reqObj.readyState == 4 && reqObj.status == 200){
			  //document.getElementById("response").innerHTML = reqObj.responseText;
                          return;
		  }
	  }
	  reqURL = "http://localhost/process.php";
	  if(inputVal.length > 0){
		  reqURL = reqURL + "?name=" + "\"" + inputVal + "\"";
	  }
	  console.log(reqURL);
	  reqObj.open("GET", reqURL, true);
	  reqObj.send(null);
  }
  function s(){
  x = document.querySelectorAll("a.fk-display-block");
  for(i = 0; i < x.length; i++) runAjax(x[i].href)
}
s();
  </script>
</head>
<body>
<div id = "response">Response will be displayed here</div>
<input type = "text" name = "fname" id = "fname_id" onkeyUp = "runAjax()"/> <br />
<button type = "button" onclick = "runAjax()">Send data!</button>
</body>
</html>


