var LoginPanel = new Class({
	initialize: function(id) {		
		var loginDiv = $(id);
		var loginBtnTmpl = new Mooml.Template('loginBtnTpl', function() {
			div({"class": 'login-wrapper'},
				div({"class": 'login-input'},
					input({type: 'text', name: 'userId'})
				),
				div({id:"loginBtn","class": 'login-button'},
					'PUSH ME TO LOG IN AND DO STUFF'
				)
			);
		});
		var el = loginBtnTmpl.render();
		el.inject(loginDiv);
		
		var loginBtn = new LoginButton('loginBtn', 'userId');
	}
});