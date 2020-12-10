'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('loginCtrl', ['PathService', 'AuthenticationService', function(PathService, AuthenticationService) {

      this.login = function (user) {
        console.error('Entro al loginCtrl');

        if (AuthenticationService.isLoggedIn()) {
          PathService.get().index().go();
          return;
        }
        AuthenticationService.login(user).then(function (authToken) {
          console.log(authToken);
          // TODO: Set RememberMe key if sent here
        }, function (errorResponse) {
          console.error(errorResponse);
        })
      }
    }]);

});
