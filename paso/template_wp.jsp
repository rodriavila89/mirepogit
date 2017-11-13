<%@ include file="../elements/CC_Common_Libraries.jsp" %>

<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="net.sf.json.JSONArray" %>
<%@page import="net.sf.json.JSONObject" %>
<%@page import="net.sf.json.JSONException" %>



<%

	//String jspPath = "/storage/cmsmedios/static/sitemaps/";
	//String fileName = "json_mercados.json";
	//String txtFilePath = jspPath + fileName;


%>

<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script>
	
	var dominio = "<%= URLSITIOCERTIFICATE %>";

	var url = dominio + "/_static_rankings/jsonElecciones/jsonPaso.json";
		
		$.getJSON(url, callbackFuncWithData);

		function callbackFuncWithData(response){

			var uno = new Array();
			var dos = new Array();

			for(i = 0; i < response.length; i++){

				uno.push(sortJSON(response[i], 'porcentaje', 'desc'));
				
			}

			b(uno);

			c(uno);

			d(uno);

		}

		


		function b(arrayList){


				for (var x = 0; x < 5; x++) {

			    		var classList = "dipcaba" + x;
						var classBar = "energy-bar-dipcaba" + x;
						var classValue = "energy-" + classList;
						var classLabel = "label" + classList
						var classTitle = "title" + classList
						var classPath = "dipcabapath" + x;
						var classActiveInt = "activeint" + x;
					
						if(arrayList[0][x].path == "filmus"){

							document.getElementById(classActiveInt).style.display = null;

							createBlock(x, arrayList);

						}

						document.getElementById(classPath).src = "/arte/elecciones/candidatos_paso/" + arrayList[0][x].path + ".jpg";
						
						document.getElementById(classList).className += arrayList[0][x].class;
						var barra = arrayList[0][x].porcentaje + "%";

						document.getElementById(classBar).style.width = barra;
						
						document.getElementById(classLabel).innerText = arrayList[0][x].candidato;
						document.getElementById(classTitle).innerText = arrayList[0][x].partido;
						document.getElementById(classValue).innerText = arrayList[0][x].porcentaje + "%";
			    }

		}



		function c(list1){

			for (var z = 0; z < 5; z++) {
					
					var classList = "senagba" + z;
					var classBar = "energy-bar-" + classList;
					var classValue = "energy-" + classList;
					var classLabel = "label" + classList
					var classTitle = "title" + classList
					var classPath = "senagbapath" + z;

					document.getElementById(classPath).src = "/arte/elecciones/candidatos_paso/" + list1[1][z].path + ".jpg";

					document.getElementById(classList).className += list1[1][z].class;
					var barra = list1[1][z].porcentaje + "%";

					document.getElementById(classBar).style.width = barra;

					document.getElementById(classLabel).innerText = list1[1][z].candidato;
					document.getElementById(classTitle).innerText = list1[1][z].partido;
					document.getElementById(classValue).innerText = list1[1][z].porcentaje + "%";

			}
		}




		function d(list1){

			for (var z = 0; z < 5; z++) {
					
					var classList = "dipgba" + z;
					var classBar = "energy-bar-" + classList;
					var classValue = "energy-" + classList;
					var classLabel = "label" + classList
					var classTitle = "title" + classList
					var classPath = "dipgbapath" + z;

					document.getElementById(classPath).src = "/arte/elecciones/candidatos_paso/" + list1[2][z].path + ".jpg";

					document.getElementById(classList).className += list1[2][z].class;
					var barra = list1[2][z].porcentaje + "%";

					document.getElementById(classBar).style.width = barra;

					document.getElementById(classLabel).innerText = list1[2][z].candidato;
					document.getElementById(classTitle).innerText = list1[2][z].partido;
					document.getElementById(classValue).innerText = list1[2][z].porcentaje + "%";

			}
		}



		function createBlock(data, list){

			var id = "bloque" + data;
			var ref1 = document.getElementById(id);
			var li = '<div class="partido-graph ciudadana"  id="int0"><div class="line-partido"><h2 class="graph-text" id="titleint0">Vamos Juntos</h2><div class="interna"><div class="int-candidato"><figure><img class="img-candidato" id="intpath0" src="/arte/elecciones/candidatos_paso/'+list[3][0].path+'.jpg" width="50" height="50" alt="#"/></figure><div class="datos-candidatos"><span class="graph-text" id="labelint0">'+list[3][0].candidato+'</span><div class="content-bar"><div class="percent-bar" id="energy-bar-int0" style="width: '+list[3][0].porcentaje+'%;"><span class="percent-text" id="energy-int0">'+list[3][0].porcentaje+'%</span></div></div></div></div><div class="int-candidato"><figure><img class="img-candidato" id="intpath1" src="/arte/elecciones/candidatos_paso/'+list[3][1].path+'.jpg" width="50" height="50" alt="#"/></figure><div class="datos-candidatos"><span class="graph-text" id="labelint1">'+list[3][1].candidato+'</span><div class="content-bar"><div class="percent-bar" id="energy-bar-int1" style="width: '+list[3][1].porcentaje+'%;"><span class="percent-text" id="energy-int1">'+list[3][1].porcentaje+'%</span></div></div></div></div></div></div></div>';

			$("#"+id).append(li);

		}


        function sortJSON(data, key, orden) {
            return data.sort(function (a, b) {
                var x = a[key],
                y = b[key];

                if (orden === 'asc') {
                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                }

                if (orden === 'desc') {
                    return ((x > y) ? -1 : ((x < y) ? 1 : 0));
                }
            });
        }
</script>





<div class="primarias-home content-paso-elecciones panel-group"  >
			<div class="mod-elecciones caba">
				<div class="mod-header">	
					<h2 class="mod-title">
						<a href="#open3" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle active">
							Diputados por la ciudad de Buenos Aires
							<span class="glyphicon glyphicon-minus">
								<span class="ver-data">ver</span>
							</span>
						</a>
					</h2>
				</div>
				<div id="open3" class="row clearfix diputados collapse in">
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph "  id="dipcaba0">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipcaba0">Vamos Juntos</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipcabapath0" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" scaleColor="transparent"  class="img-candidato"  width="50" height="50" alt="Carrio"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipcaba0">Lilita Carrió</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipcaba0">
													<span class="percent-text" id="energy-dipcaba0">0%</span>
												</div>
											</div>
                                            <h3 class="title-interna" id="activeint0" style="display:none">Ver interna</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
                        <div class="partido-interna" id="bloque0"></div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph " id="dipcaba1">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipcaba1">Unidad porteña</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" id="dipcabapath1" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipcaba1">Daniel Filmus</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipcaba1">
													<span class="percent-text" id="energy-dipcaba1">0%</span>
												</div>
											</div>
											<h3 class="title-interna" id="activeint1" style="display:none">Ver interna</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
                        <div class="partido-interna" id="bloque1"></div>
						
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipcaba2">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipcaba2">Evolución Ciudadana</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipcabapath2" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipcaba2">Martín Lousteau</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipcaba2">
													<span class="percent-text" id="energy-dipcaba2">0%</span>
												</div>
											</div>
                                            <h3 class="title-interna" id="activeint2" style="display:none">Ver interna</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
                        <div class="partido-interna" id="bloque2"></div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipcaba3">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipcaba3">1 País</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipcabapath3" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipcaba3">Matías Tombolini</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipcaba3">
													<span class="percent-text" id="energy-dipcaba3">0%</span>
												</div>
											</div>
                                            <h3 class="title-interna" id="activeint3" style="display:none">Ver interna</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
                        <div class="partido-interna" id="bloque3"></div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipcaba4">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipcaba4">fit</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipcabapath4" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipcaba4">Marcelo Ramal</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipcaba4">
													<span class="percent-text" id="energy-dipcaba4">0%</span>
												</div>
											</div>
                                            <h3 class="title-interna" id="activeint4" style="display:none">Ver interna</h3>
										</div>
									</div>
								</div>
							</div>
						</div>
                        <div class="partido-interna" id="bloque4"></div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph IzquierdaFrente" id="">	
							<div class="line-partido">
								<h2 class="graph-text">Izquierda al Frente</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Alejandro Bodart</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph ayl" id="">	
							<div class="line-partido">
								<h2 class="graph-text">AyL</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/zamora.jpg" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Luis Zamora</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="mod-elecciones provincia">
				<div class="mod-header">	
						<h2 class="mod-title">
							<a href="#collapseOne" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle active">
								Senadores por la provincia de Buenos Aires
								<span class="glyphicon glyphicon-minus">
									<span class="ver-data">ver</span>
								</span>
							</a>
						</h2>
					</div>	
				<div id="collapseOne" class="row clearfix senadores collapse in">
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph "  id="senagba0">	
							<div class="line-partido">
								<h2 class="graph-text" id="titlesenagba0">Cambiemos</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="senagbapath0" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labelsenagba0">Esteban Bullrich</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-senagba0">
													<span class="percent-text" id="energy-senagba0">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph " id="senagba1">	
							<div class="line-partido">
								<h2 class="graph-text" id="titlesenagba1">Unidad Ciudadana</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="senagbapath1" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labelsenagba1">Cristina Kirchner</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-senagba1">
													<span class="percent-text" id="energy-senagba1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="senagba2">	
							<div class="line-partido">
								<h2 class="graph-text" id="titlesenagba2">1 País</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="senagbapath2" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labelsenagba2">Sergio Massa</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-senagba2">
													<span class="percent-text" id="energy-senagba2">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="senagba3">	
							<div class="line-partido">
								<h2 class="graph-text" id="titlesenagba3">Frente Justicialista</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="senagbapath3" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labelsenagba3">Florencio Randazzo</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-senagba3">
													<span class="percent-text" id="energy-senagba3">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="senagba4">	
							<div class="line-partido">
								<h2 class="graph-text" id="titlesenagba4">fit</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="senagbapath4" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labelsenagba4">Néstor Pitrola</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-senagba4">
													<span class="percent-text" id="energy-senagba4">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph IzquierdaFrente" id="">	
							<div class="line-partido">
								<h2 class="graph-text">Izquierda al Frente</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/ripoll.jpg" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Vilma Ripoll</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph creo" id="">	
							<div class="line-partido">
								<h2 class="graph-text">creo</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/solanas.jpg" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Fernando Pino Solanas</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="mod-elecciones provincia">
				<div class="mod-header">	
					<h2 class="mod-title">
						<a href="#open2" data-toggle="collapse" data-parent="#accordion" class="accordion-toggle">
							Diputados por la provincia de Buenos Aires
							<span class="glyphicon glyphicon-plus">
								<span class="ver-data">ver</span>
							</span>
						</a>
					</h2>
				</div>
				<div id="open2" class="row clearfix diputados panel-collapse collapse">
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph "  id="dipgba0">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipgba0">Cambiemos</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipgbapath0" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipgba0">Graciela Ocaña</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipgba0">
													<span class="percent-text" id="energy-dipgba0">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
						<div  class="partido-graph " id="dipgba1">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipgba1">Unidad Ciudadana</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipgbapath1" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipgba1">Fernanda Vallejos</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipgba1">
													<span class="percent-text" id="energy-dipgba1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipgba2">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipgba2">1 País</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipgbapath2" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipgba2">Felipe Solá</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipgba2">
													<span class="percent-text" id="energy-dipgba2">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipgba3">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipgba3">Frente Justicialista</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipgbapath3" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipgba3">Eduardo Bucca</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipgba3">
													<span class="percent-text" id="energy-dipgba3">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph " id="dipgba4">	
							<div class="line-partido">
								<h2 class="graph-text" id="titledipgba4">fit</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img id="dipgbapath4" class="img-candidato" src="/arte/elecciones/candidatos_paso/ajax-loader.gif" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text" id="labeldipgba4">Nicolás del Caño</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-dipgba4">
													<span class="percent-text" id="energy-dipgba4">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph IzquierdaFrente" id="">	
							<div class="line-partido">
								<h2 class="graph-text">Izquierda al Frente</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/castineira.jpg" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Manuela Castiñeira</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-3 col-sm-4 col-xs-12">
						<div  class="partido-graph creo" id="">	
							<div class="line-partido">
								<h2 class="graph-text">creo</h2>
								<div class="interna">
									<div class="int-candidato">
										<figure>
											<img class="img-candidato" src="/arte/elecciones/candidatos_paso/micheli.jpg" width="50" height="50" alt="#"/>
										</figure>
										<div class="datos-candidatos">
											<span class="graph-text">Pablo Micheli</span>
											<div class="content-bar">
												<div class="percent-bar" id="energy-bar-camdidato1">
													<span class="percent-text" id="energy-num1">0%</span>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>