var ArgToCreateSlide = new Class({
	Extends: Fx.Slide,
	onCreateArgIn: function() {
		$('ArgPanel').getFirst().setStyle("height", "");
	},
	onArgFeedOut: function() {
		this.createArgPanel = new CreateArgPanel('ArgPanel');
		this.onComplete = this.onCreateArgIn;
		this.slideIn();
		this.actionPupil.switchClick(this.createArgPanel.postButton.postArgument);
	},
	argFeedToCreateArg: function(actionPupilBtn) {
		this.slideOut();
		this.actionPupil = actionPupilBtn;
	},	
	initialize: function(id) {
		var options = {
			duration: 400,
			onComplete: this.onArgFeedOut	
		};
		this.parent(id, options);
	}
});