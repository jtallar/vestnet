'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('loginCtrl', ['PathService', 'AuthenticationService', '$scope', '$log', function(PathService, AuthenticationService, $scope, $log) {

      this.login = function (user) {
        // TODO: Ver si esta condicion de abajo va
        if (AuthenticationService.isLoggedIn()) {
          PathService.get().index().go();
          return;
        }
        AuthenticationService.login(user).then(function (authToken) {
          // TODO: Ver como hacer para que vaya a donde estaba yendo
          PathService.get().index().go();
        }, function (errorResponse) {
          // TODO: Ver que responde si no esta verificado --> $scope.valor = 2;
          $log.info('Response status: ' + errorResponse.status);
          $scope.valor = 1;
        })
      }
    }]);

});
