var CreateArgPanel = new Class({
	initialize: function(id) {
		var argPanel = $(id);
		var container = argPanel.getFirst();
		var newArgEl = new Mooml.Template('newArgTpl', function() {
			div({"class": 'new-arg-container'},
			  div({"class": 'new-arg-header'},
			    div({"class": 'new-arg-text'},
			      'What would you like to discuss?'
			    ),
			    div({"class": 'new-arg-title'},
			      input({
			    	  type: 'text', 
			    	  name: 'title', 
			    	  "class": 'required new-arg-input', 
			    	  title: 'Title'})
			    )
			  ),
			  div({"class": 'new-arg-content'},
			    form({method: 'post', action: '#'},
			      textarea({name: 'content', style: 'width: 100%'})
			    )
			  ),
			  div({"id":'post-arg-btn', 
				   "class": 'arg-btn arg-btn-text post-arg-btn'},
			    'Post your discussion'
			  ),
			  div({"id":'post-arg-validation-errs'}),
			  div({"class":'render-header'},'Preview:'),
			  div({"id":'post-arg-render-contents', 
				   "class": 'render-area'},
			    div('Your discussion contents will show up here as you type...')
			  )
			);
		});
		container.getFirst().dispose();
		var el = newArgEl.render();
		el.inject(container);
		tinyMCE.init({
		    mode : "textareas",
		    theme : "advanced",
		    height: "200",
		    theme_advanced_buttons1 : "bold,italic,underline,|,link,unlink,|,bullist,numlist,outdent,indent,|,blockquote,undo", 
		    theme_advanced_buttons2 : "", 
		    theme_advanced_buttons3 : "",
		    setup: function(ed) {
		    	ed.onKeyUp.add(function(ed, e){
		    		var renderEl = $('post-arg-render-contents');
		    		var contents = renderEl.getFirst();
		    		contents.dispose();
		    		
		    		var contentEl = new Element('div',{'id':'post-contents','class': 'render-contents',html: ed.getContent()});
		    		contentEl.inject(renderEl);
		    	});
		    }
		});
		
		this.postButton = new PostArgButton('post-arg-btn');
	}
});
