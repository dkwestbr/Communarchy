var MainPanel = new Class({
	initialize: function(id) {
		var createArgBtn = new ArgButton('create_arg', function(){
			var slide = new ArgToCreateSlide('dynamic-arg-div');
			slide.argFeedToCreateArg(createArgBtn);
		});
		
		var genArgFeed = new Mooml.Template('argFeedTpl', function() {
			// Insert mooml template for single argument within feed.
			// Include variable for arg num.  Cache next page.
		});
	}
});