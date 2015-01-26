var pat_form_mod = (function() {

function init_submit( evt ) {
    var f1 = document.querySelector("#f1");
    f1.addEventListener('submit', function(event) {
        // prevent the form submit
        event.preventDefault();

        // get the input fields
        var name = document.querySelector('#f1 input[name="student-name"]');
        var number = document.querySelector('#f1 input[name="student-number"]');
        var address = document.querySelector('#f1 input[name="student-address"]');
        var email = document.querySelector('#f1 input[name="student-email"]');

        // construct a patient object
        // field names must match Patient.java
        var p = {
            name: name.value,
            number: number.value,
            address: address.value,
            email: email.value
        };
        console.log( p ); 
        var json = JSON.stringify( p );

        // send the update to the server
        local_ajax_mod.ajax_request( {
            method : "POST",
            link: window.location.pathname,
            doc : json,
            mime : 'application/json',
            ok_fn :  function( req ) {
               try {
                    var pReply = JSON.parse( req.responseText );
                    console.log( pReply );
                }
                catch( e ) {
                    console.log( e );
                }
            }
        } );
    } );
}

window.addEventListener('load', init_submit );

}());
