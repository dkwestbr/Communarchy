var PostArgButton = new Class({
	Extends: ArgButton,
	postArgument: function() { 
		var ed = tinyMCE.get('content');
		var context = ed.getContent();
		var title = document.getElement('input[name=title]').get('value');
		var container = $$('#new-arg-container');
		var errAppendPoint = $('post-arg-validation-errs');
		
		var jsonRequest = new Request.JSON({
			url: '/postarg', 
		    onRequest: function(){
		        container.addClass('request-waiting');
		    },
		    onSuccess: function(newArg){
		    	container.removeClass('request-waiting');
		    },
		    onFailure: function(requestObj){
		    	container.removeClass('request-waiting');
		    	
		    	var validationErrs = JSON.decode(requestObj.responseText);
		    	var errorsContainer = $('post-arg-validation-errs');
		    	errorsContainer.getChildren().each(function (el){
		    		el.destroy();
		    	});
		    	
				var errTempl = new Mooml.Template('validationErrs', function(params) {
					div({"class": 'arg-errors'},
						div({"class": 'arg-errors-header'}, params.header),
						div({"class": 'arg-errors-content'}, params.content)
					);
				});
				
				var contentTempl = new Mooml.Template('divs', function(content){
					div(content);
				});
				
				if(validationErrs.headerErrors.length > 0) {
					var headerErrs = errTempl.render({
						header: 'Problems with your discussion title:',
						content: contentTempl.render(validationErrs.headerErrors)
					});
					headerErrs.inject(errAppendPoint);
				}
				
				if(validationErrs.contextErrors.length > 0) {
					var contextErrs = errTempl.render({
						header: 'Problems with your discussion context:',
						content: contentTempl.render(validationErrs.contextErrors)
					});
					contextErrs.inject(errAppendPoint);
				}
		    }
		}).post({'context': context,'title': title});
	},
	initialize: function(id) {
		this.parent(id, this.postArgument);
	}
});
