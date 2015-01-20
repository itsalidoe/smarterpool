function checkEmail(email) {
    //TODO: Write checkEmail method;
}

$('#signup').on('submit', function() {
   if ($('#password').val() != $('#password_verify').val()) {
       alert('Password does not match.');
       return false;
   }

   if (!checkEmail($('#email').val())) {
       return true;
   }
   return true;
});