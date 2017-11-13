//SENADORES GBA

function c(list1){

		for (var z = 0; z < list1[1].length; z++) {
			//console.log(esto[0][x])
			//if (esto[0][x].cargo == "Diputados") {
				console.log("this")
				var classList = "senagba" + z;
				var classBar = "energy-bar-" + classList;
				var classValue = "energy-" + classList;
				var classLabel = "label" + classList
				var classTitle = "title" + classList
				var classPath = "senagbapath" + z;

				document.getElementById(classPath).src = list1[1][z].path;
				//console.log(classList)
				document.getElementById(classList).className += list1[1][z].class;
				var barra = list1[1][z].porcentaje + "px";
				//console.log(esto[0][x].candidato)
				document.getElementById(classBar).style.width = barra;
				document.getElementById(classLabel).innerText = list1[1][z].candidato;
				document.getElementById(classTitle).innerText = list1[1][z].partido;
				document.getElementById(classValue).innerText = list1[1][z].porcentaje + "%";

			//}
		}

}





