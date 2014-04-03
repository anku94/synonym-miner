function getContent(filename, element){
	var reqObj;
	reqObj = new XMLHttpRequest();
	reqObj.onreadystatechange = function(){
		if (reqObj.readyState == 4 && reqObj.status == 200){
			//document.getElementById("response").innerHTML = reqObj.responseText;
			element.innerText = reqObj.responseText;
		}
	}
	//console.log(filename);
	reqObj.open("GET", filename, true);
	reqObj.send(null);

}
function updateConsole(){
	//console.log("Updating!!");
	var codeSection = document.getElementById("code-output");
	filename="console.txt";
	var reqObj;
	reqObj = new XMLHttpRequest();
	reqObj.onreadystatechange = function(){
		if (reqObj.readyState == 4 && reqObj.status == 200){
			//document.getElementById("response").innerHTML = reqObj.responseText;
			codeSection.innerText = reqObj.responseText;
			document.getElementById("code-output").innerText = reqObj.responseText;
			document.getElementById("code-output").innerHTML = reqObj.responseText;
		}
	}
	//console.log(filename);
	reqObj.open("GET", filename, true);
	reqObj.send(null);

}
function processQuery(){
	var codeSection = document.getElementById("code-output");
	var queryField = document.getElementById('query-field');
	var phpResp = document.getElementById("php-response");

	codeSection.innerText = "";
	var query = queryField.value;
	getContent("process.php?query=\""+query+"\"", phpResp);
	//getContent("queryLog.txt", codeSection);
	//while(true){
	//}

	return false;
}
setInterval(updateConsole, 500);
