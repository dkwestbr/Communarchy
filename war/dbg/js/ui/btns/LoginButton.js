var LoginButton = new Class({
	initialize: function(id, inputTitle) {		
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
			click: function(){
				var user = document.getElement('input[name=userId]').get('value');
				var container = $('container');
				
				var myRequest = new Request({
				    url: '/Subjective/login?dbg=true',
				    method: 'post',
				    data: {'user': user},
				    onRequest: function(){
				        container.addClass('request-waiting');
				    },
				    onSuccess: function(responseText){
				    	container.removeClass('request-waiting');
				    	window.location = responseText;
				    },
				    onFailure: function(){
				    	container.removeClass('request-waiting');
				    }
				});
				myRequest.send();
			}
		});
	}
});