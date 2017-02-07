$(document).ready(function () {
	var anioActual = new Date().getFullYear();
	$('#calendar').calendar({
		maxDate: new Date(anioActual, 11, 31),
		minDate: new Date(anioActual, 0, 1),
		language: "es",
		        mouseOnDay: function(e) {
            if(e.events.length > 0) {
                var content = '';
                
                for(var i in e.events) {
                    content += '<div class="event-tooltip-content">' + '<div class="event-name" style="color:' + e.events[i].color + '">' + e.events[i].name + '</div>' + '</div>';
                }
            
                $(e.element).popover({ 
                    trigger: 'manual',
                    container: 'body',
                    html:true,
                    content: content
                });
                
                $(e.element).popover('show');
            }
        },
        mouseOutDay: function(e) {
            if(e.events.length > 0) {
                $(e.element).popover('hide');
            }
        },
		dataSource: [
			{
				id:0,
				name:'Año nuevo',
				startDate: new Date(anioActual, 0, 1),
				endDate: new Date(anioActual, 0, 1),
				tipo: 'inamovible'
			},
			{
				id:1,
				name:'Carnaval',
				startDate: new Date(anioActual, 1, 27),
				endDate: new Date(anioActual, 1, 28),
				tipo: 'inamovible'
			},
			{
				id:2,
				name:'Día nacional de la memoria por la Verdad y la Justicia',
				startDate: new Date(anioActual, 2, 27),
				endDate: new Date(anioActual, 2, 27),
				tipo: 'transladable'
			},
			{
				id:3,
				name: 'Día del veterano y los caidos en la Guerra de Malvinas',
				startDate: new Date(anioActual, 3, 2),
				endDate: new Date(anioActual, 3, 2),
				tipo: 'inamovible'
			},
			{
				id:4,
				name: 'Jueves y Viernes Santo',
				startDate: new Date(anioActual, 3, 13),
				endDate: new Date(anioActual, 3, 14),
				tipo: 'inamovible'
			},
			{
				id:5,
				name: 'Día del trabajador',
				startDate: new Date(anioActual, 4, 1),
				endDate: new Date(anioActual, 4, 1),
				tipo: 'inamovible'
			},
			{
				id:6,
				name: 'Día de la revolución de Mayo',
				startDate: new Date(anioActual, 4, 25),
				endDate: new Date(anioActual, 4, 25),
				tipo: 'inamovible'
			},
			{
				id:7,
				name: 'Día del paso a la Inmortalidad de Güemes',
				startDate: new Date(anioActual, 5, 17),
				endDate: new Date(anioActual, 5, 17),
				tipo: 'inamovible'
			},
			{
				id:8,
				name: 'Día del paso a la inmortalidad de Belgrano',
				startDate: new Date(anioActual, 5, 19),
				endDate: new Date(anioActual, 5, 19),
				tipo: 'transladable'
			},
			{
				id:9,
				name: 'Día de la independencia',
				startDate: new Date(anioActual, 6, 9),
				endDate: new Date(anioActual, 6, 9),
				tipo: 'inamovible'
			},
			{
				id:10,
				name: 'Dia del paso a la inmortalidad de San Martín',
				startDate: new Date(anioActual, 7, 21),
				endDate: new Date(anioActual, 7, 21),
				tipo: 'transladable'
			},
			{
				id:11,
				name: 'Día del Respeto a la Diversidad Cultural',
				startDate: new Date(anioActual, 9, 16),
				endDate: new Date(anioActual, 9, 16),
				tipo:'transladable'
			},
			{
				id:12,
				name: 'Día de la Soberanía Nacional',
				startDate: new Date(anioActual, 10, 20),
				endDate: new Date(anioActual, 10, 20),
				tipo: 'inamovible'
			},
			{
				id: 13,
				name: 'Día de la Inmaculada Concepción',
				startDate: new Date(anioActual, 11, 8),
				endDate: new Date(anioActual, 11, 8),
				tipo: 'inamovible'
			},
			{
				id:14,
				name:'Navidad',
				startDate: new Date(anioActual, 11, 25),
				endDate: new Date(anioActual, 11, 25),
				tipo: 'inamovible'
			},
			{
				id:15,
				name: 'prueba',
				startDate: new Date(anioActual, 11, 23),
				endDate: new Date(anioActual, 11, 23)
			}
		]
	});
	console.log($('#calendar').data('calendar').getMaxDate());
});
