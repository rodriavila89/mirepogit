$(document).ready(function() {
	var industrias = [{"INDUSTRIA":"Hotel & Catering"},{"INDUSTRIA":"IT"},{"INDUSTRIA":"Medicina y Ciencia"},{"INDUSTRIA":"Industrial"},{"INDUSTRIA":"Ingeniería, Técnica, Petróleo y Minería"},
	{"INDUSTRIA":"Ventas & Marketing"},{"INDUSTRIA":"Finanzas & Legales"},{"INDUSTRIA":"Oficina"}];
	//Carga el select de las industrias.
	for (var i = 0; i<industrias.length; i++){
		var valor = industrias[i].INDUSTRIA;
		$("#industria").append("<option value='" + valor +  "'>" + valor +	"</option>");
	}   


	//Carga el select de las posiciones en base a la industria que eligio el usuario. Actualmente estoy usando un html estatico que hice en base
	//a los datos suministrados, pero despues de probarlo vi que la ventaja en performance es insignficante
	cargarSelectPosicion();

	$("#industria").change(function(){
		cargarSelectPosicion();
	});

	var sueldos={};

	//TODO: aparentemente no esta andando bien apretar enter en este input

	$(document).keypress(function(e) {
	  if (e.keyCode == 13) {
		e.preventDefault();
		$("#calcular").click();
	  }
	});


	//Busca el sueldo para la posicion elegida, y despues manipula el dom 
	//para mostrarle un mensaje al usuario con el porcentaje que tiene sobre o por debajo
	//de la media
	$("#calcular").click(function(){
		$("#cuadroform").addClass('no-display');
		buscarSueldosYGenerarMensaje();
		//el html del resultado ya esta cargado en la pagina, solo que esta oculto, asi que le saco la propiedad
		// que lo oculta
		mostrarResultado();
		//esto es para mobile, mueve la pantalla hacia el resultado cuando aprieta calcular
		desplazarAResultado();
		console.log(sueldos);
	});

	$("#buscarDeNuevo").click(function(){
		$("#resultado").addClass('no-display');
		$("#cuadroform").removeClass('no-display');
	});

});

function cargarSelectPosicion(){
	var industriaValue = $("#industria").val();
	var optionsSelect = htmlCargos[industriaValue];
	$("#posicion").html(optionsSelect);
}

function buscarSueldosYGenerarMensaje(){
		$("#porcentaje").html("");
		$("#emoji").html("");
		var valoresIngresados = {};
		valoresIngresados.INDUSTRIA = $("#industria").val();
		valoresIngresados.PUESTO = $("#posicion").val();
		valoresIngresados.REGION = $("#region").val();
		valoresIngresados.tamanio = $("#tamaño").val();
		valoresIngresados.sueldo = Number($('#sueldo').val());
		sueldos = buscarSueldos(valoresIngresados, datosExcel);
		$("#minimo").text(Number(sueldos.minimo));
		$("#medio").text(Number(sueldos.medio));
		$("#maximo").text(Number(sueldos.maximo));
		if  (!!valoresIngresados.sueldo){
			var porcentaje;
			if (valoresIngresados.sueldo < sueldos.minimo) {
					porcentaje = Math.floor(((sueldos.minimo - valoresIngresados.sueldo)/ valoresIngresados.sueldo) *100);
					$("#porcentaje").text('Estás un ' + porcentaje + '% abajo');
					$("#emoji").append('<img src="dist/disappointed-but-relieved-face.png" class="img-responsive">');
				}
				else if ((sueldos.minimo<valoresIngresados.sueldo) && (valoresIngresados.sueldo<sueldos.maximo)) {
					$("#porcentaje").text('Estás en la media');
					$("#emoji").append('<img src="dist/winking-face.png" class="img-responsive">');
				} else if (valoresIngresados.sueldo>=sueldos.maximo) {
					porcentaje = Math.floor(((valoresIngresados.sueldo - sueldos.maximo) / sueldos.maximo)*100);
					$("#porcentaje").text('¡Estás un ' + porcentaje +'% arriba!');
					$("#emoji").append('<img src="dist/Money_Face_Emoji.png" class="img-responsive">');
				}
			}
		if (isNaN(Number(sueldos.minimo)) || (Number(sueldos.minimo) === 0)){
			if (!($("#valoressueldo").hasClass("no-display"))) {
				$("#valoressueldo").addClass("no-display");		
			}
			if ($("#noresultados").hasClass("no-display")) {
				$("#noresultados").removeClass("no-display");		
			}
			//$("#resultado").append("<div id='noresultados'>No hay datos con las opciones seleccionadas</div>")
		} else {
			if ($("#valoressueldo").hasClass("no-display")) {
				$("#valoressueldo").removeClass("no-display");		
			}
			if (!($("#noresultados").hasClass("no-display"))) {
				$("#noresultados").addClass("no-display");		
			}
		}
}


//busqueda normal con un for
function buscarSueldos(valoresIngresados, pruebaBusqueda){
	for (var i = 0; i < pruebaBusqueda.Hoja1.length; i++) {
		if (valoresIngresados.INDUSTRIA == pruebaBusqueda.Hoja1[i].INDUSTRIA && valoresIngresados.PUESTO == pruebaBusqueda.Hoja1[i].PUESTO && valoresIngresados.REGION == pruebaBusqueda.Hoja1[i].REGION) {
			var datosBusqueda = pruebaBusqueda.Hoja1[i];
			sueldos = {};
			DeepTrim(datosBusqueda);
			switch(valoresIngresados.tamanio){
				case "pequeña":
					sueldos.minimo = datosBusqueda.pMin;
					sueldos.medio = datosBusqueda.pMed;
					sueldos.maximo = datosBusqueda.pMax;
					break;
				case "mediana":
					sueldos.minimo = datosBusqueda.mMin;
					sueldos.medio = datosBusqueda.mMed;
					sueldos.maximo = datosBusqueda.mMax;
					break;
				case "grande":
					sueldos.minimo = datosBusqueda.gMin;
					sueldos.medio = datosBusqueda.gMed;
					sueldos.maximo = datosBusqueda.gMax;
					break;	
			}
			break;
		}
	}
	return sueldos;
}

function mostrarResultado(){
	if ($("#resultado").hasClass("no-display")) {
				$("#resultado").removeClass("no-display");		
		}
}

function desplazarAResultado(){
	$('html, body').animate({
		     scrollTop: $("#target").offset().top
	});
}


function DeepTrim(obj) {
    for (var prop in obj) {
        var value = obj[prop], type = typeof value;
        if (value !== null && (type == "string" || type == "object") && obj.hasOwnProperty(prop)) {
            if (type == "object") {
                DeepTrim(obj[prop]);
            } else {
                obj[prop] = obj[prop].trim();
                obj[prop] = removeCommas(obj[prop]);
            }
        }
    }
}

function removeCommas(str) {
    while (str.search(",") >= 0) {
        str = (str + "").replace(',', '');
    }
    return str;
}