<?php
$json = file_get_contents('https://eccwebapp01.azurewebsites.net/rp/json');


?>

<!doctype html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Barras Chart</title>
	 <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">

    <script src="https://code.jquery.com/jquery-3.2.0.min.js"></script>
    <script src="../../dist/Chart.bundle.js"></script>
    <script src="../utils.js"></script>
	<link rel="stylesheet" type="text/css" href="styles-graph.css" />
	<link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,700" rel="stylesheet"/>
    <script src="script.js"></script>
</head>

<body>
    <div id="parseame" style="display:none;"><?php echo htmlspecialchars($json);?></div>
	<div class="content-graph">
		<div class="header-graph">
			<div class="btn-menu">
				<span class="line-btn"></span>
				<span class="line-btn"></span>
				<span class="line-btn"></span>
			</div>
			<h2 class="title-graph">Sondeos Elecciones 2017</h2>
			<div class="content-btn-graph" id="datosgrafico">
			</div>
		</div>
		<div class="content-data">
			<h2 class="tit-cons"></h2>
			<ul class="referencias" id="referencias">
			</ul>
		</div>
		<div id="canvas-holder">
			<canvas id="chart-area" />
		</div>
	</div>
	
	<script>
    $(document).ready(function(){
        var nandemonaiya = JSON.parse($("#parseame")[0].innerHTML)
    
        var loopcount = 0;
        for (var hoja of nandemonaiya) {
            var encuestadora = hoja[0].encuestadora;
            $("#datosgrafico").append("<button class='btn-graph' id='" + loopcount + "'>" + encuestadora + "</button>");
            loopcount++;
        }

    var dataPrimera = [];
    var colorFondo = [  'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 99, 132, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 22, 22, 1)'];
    var colorBorde = [
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255,99,132, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(255, 22, 22, 1)',
                ];
    var colorFondo2 =   [
                    'rgba(255, 206, 86, 0.40)',
                    'rgba(54, 162, 235, 0.40)',
                    'rgba(255, 99, 132, 0.40)',
                    'rgba(75, 192, 192, 0.40)',
                    'rgba(255, 22, 22, 0.40)'];     
    var coloresExtra = [];
    var labelsdin = [];
    for (elementoArray of nandemonaiya[0]) {
        dataPrimera.push(elementoArray.intencionDeVoto);// statement
        labelsdin.push("");
        if (dataPrimera.length > colorFondo.length){
            var colorRandomizadoSinParametroFinal = 'rgba(' + Math.floor(Math.random() * 255) + ',' + Math.floor(Math.random() * 255) + ',' + Math.floor(Math.random() * 255) + ','
            var parametroFinal = '1)';
            var parametroFinalFondo = '0.40)';
            var randomColor = colorRandomizadoSinParametroFinal + parametroFinal;
            var randomBackgroundColor = colorRandomizadoSinParametroFinal + parametroFinalFondo;
            var randomColorBorde = 'rgba(' + Math.floor(Math.random() * 255) + ',' + Math.floor(Math.random() * 255) + ',' + Math.floor(Math.random() * 255) + ',' + 1 + ')';
            colorFondo.push(randomColor);
            coloresExtra.push(randomColor);
            colorBorde.push(randomColorBorde);
            colorFondo2.push(randomBackgroundColor);
        }
    }

    //console.log(colorFondo);
    //console.log(dataPrimera);
    var config = {
        type: 'bar',
        data: {
            datasets: [{
                data: dataPrimera
                ,
                backgroundColor: colorFondo,
                 backgroundColor2: colorFondo2,
                borderColor: colorBorde,
            }],
            labels: labelsdin,
        },
        options: {
            responsive: true,
            legend: {
                display: false,
                labels: {
                    fontColor: 'rgb(255, 99, 132)',
                    
                }
            },
            scales: {
        xAxes: [{
            ticks: {
                fontSize: 12
            }
        }]
    }
        }
    };

    var seleccionarGrafico = function(encuestadoraEnArray){
        //('.btn-graph:first-child').addClass('activo');
        var candidatosCambiantes = [];
        for (candidato of nandemonaiya[encuestadoraEnArray]) {
            candidatosCambiantes.push(candidato.intencionDeVoto);// statement
        }

        //console.log(candidatosCambiantes);

        config.data.datasets.forEach(function(dataset) {
            dataset.data = candidatosCambiantes;
        });
        window.myPie.update();
        
        var list = document.getElementById('referencias');

        while (list.hasChildNodes()) {   
            list.removeChild(list.firstChild);
        }
        
        for(var llave in nandemonaiya[encuestadoraEnArray] ){
            config.data.datasets[0].backgroundColor[llave];
            config.data.datasets[0].backgroundColor2[llave];
            var unElemento = nandemonaiya[encuestadoraEnArray][llave];
            var ref = document.querySelector('.referencias');
            var span = document.createElement('span');
            var h2 =  document.querySelector('.tit-cons');
            var li = document.createElement('li');
            ref.appendChild(li);
            li.innerText = unElemento.candidato + " " + unElemento.intencionDeVoto + "%";
            h2.innerText = unElemento.encuestadora;
            li.appendChild(span);
            li.style.backgroundColor = config.data.datasets[0].backgroundColor2[llave];
            span.style.backgroundColor = config.data.datasets[0].backgroundColor[llave];
            
        }
    }

    function cargarGraph() {
        var ctx = document.getElementById("chart-area").getContext("2d");
        window.myPie = new Chart(ctx, config);
        seleccionarGrafico(0);
        $('.btn-graph')[0];
        $('.btn-graph:first-child').addClass('activo');
        
    };


    $(".btn-graph").click(function(){
          $('.btn-graph.activo').removeClass('activo');
         $(this).addClass('activo');
        //var encuestadoraEnArray = this.id;
        ////console.log(encuestadoraEnArray);
        seleccionarGrafico(this.id)
        
    });
    


    cargarGraph();

    var colorNames = Object.keys(window.chartColors);

    })
    </script>
</body>

</html>
