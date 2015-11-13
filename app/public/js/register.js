function register(){
	/* send register info to server */
	var email = document.getElementById("login_email").value;
	var name = document.getElementById("login_name").value;
	var pw = document.getElementById("login_pw").value;
	/* just for test */ 
	window.location.href= "user_info.html";



	var dataString = "Email=" + email + "&Name=" + name + "&pw=" + pw;
	$.ajax({
	        type: "POST",
	        url: "???",  // server file name 
	        data: dataString,
	        dataType: "json",
	        
	        //if received a response from the server
	        success: function( data, textStatus, jqXHR){ 

	            	alert("in success");
	                
	                 //set cookie
	                 var user_name = name;
	                 document.cookie = "username=" + user_name + "; success = 1";

	                 //should be here 
	                 window.location.href="user_info.html";
	        },
	        
	        //If there was no resonse from the server
	        error: function(jqXHR, textStatus, errorThrown){
	             console.log("Something really bad happened " + textStatus);
	              $("#response").html(jqXHR.responseText);
	        },
	        
	        //capture the request before it was sent to server
	        beforeSend: function(jqXHR, settings){
	            //adding some Dummy data to the request

	            settings.data += "&dummyData=whatever";
	            //disable the button until we get the response
	            $('#submit').attr("disabled", true);
	        },
	        
	        //this is called after the response or error functions are finsihed
	        //so that we can take some action
	        complete: function(jqXHR, textStatus){
	            //enable the button 
	            $('#submit').attr("disabled", false);
	        }

	    });  
	      
    }