var ArgButton = new Class({
	switchClick: function (newClickEvent) {
		$(this.id).removeEvents('click');
		$(this.id).addEvent('click', newClickEvent);
	},
	initialize: function(id, clickEvent, mouseUp, mouseDown) {
		
		this.id = id;
		
		$(id).addEvents({
			mouseenter: function(){
				this.setStyle('background-color','#3658DE');
			},
			mouseleave: function(){
				this.setStyle('background-color','#DEB236');
			},
			mousedown: function(){
				this.setStyle('background-color','#99CCFF');
			},
			mouseup: function(){
				this.setStyle('background-color','#3658DE');
			},
			click: clickEvent
		});
	}
});
