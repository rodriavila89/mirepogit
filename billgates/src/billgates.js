
$(document).ready(function() {
	$("#calcular").click(function(){
		calcularTiempo();
		mostrar();
	});
});

function calcularTiempo(){
	var sueldo = $("#sueldoN").val();
	var dolar = 16.05;
	var tiempo = (75000000000 / (sueldo/dolar))/12;
	$("#resultado").text(Math.floor(tiempo));
}

function mostrar(){
	if ($("#resultadobox").hasClass('no-display')){
		$("#resultadobox").removeClass('no-display');
	}
}
