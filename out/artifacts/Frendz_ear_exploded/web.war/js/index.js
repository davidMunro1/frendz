/**
 * Created by davidmunro on 08/01/2016.
 */

var global_userName = '';
var global_password = '';

sinchClient = new SinchClient({
    applicationKey: 'da09f365-18e7-47d0-ad41-e3419bca7ac0',
    applicationSecret: '70qsOKjHXUiGykDsGZIDiw==',
    capabilities: {messaging: true},
});

var handleError = function(error){
    console.log("Error")
    console.log(error.message);
}

$('#login').on('click',function(event){
    var username = 'David';
    var password = 'password';

    $.post('http://localhost:8082',
        {username: username, password: password},
        function(authTicket) {
            console.log("comes here")
            sinchClient.start(authTicket)
                .then(function() {
                    // Handle successful start, like showing the UI
                    console.log("sucess");
                })
                .fail(function(error) {
                    handleError(error);
                });
        },
        function(error) {
            // Handle application server error
            handleError(error);
        });

    var messageClient = sinchClient.getMessageClient();

    var username = 'David';
    var password = 'password';
    sinchClient.start({username: username, password: password}, function() {
        global_userName = username;
        console.log("Logged in!");
        var message = messageClient.newMessage('David', 'Hello David from ' +username);
        messageClient.send(message);
    }).fail(handleError);


    var eventListener = {
        onIncomingMessage: function(message) {
            if (message.senderId == global_userName) {
                $('div#chatArea').append('<div>' + message.textBody + '</div>');
            } else {
                $('div#chatArea').append('<div style="color:red;">' + message.textBody + '</div>');
            }
        }
    }

    messageClient.addEventListener(eventListener);

});



