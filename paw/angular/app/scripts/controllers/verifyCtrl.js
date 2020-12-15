    'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('verifyCtrl', ['userService', 'PathService', '$scope', '$routeParams', function(userService, PathService, $scope, $routeParams) {
      console.log("entranding");
      var token = $routeParams.token;
      if (token === undefined) {
        PathService.get().login().go();
        // TODO: Ver como le pasamos este valor a login
        $scope.valor = 3;
        return;
      }

      userService.verifyPassword(token).then(function () {
        PathService.get().login().go();
        // TODO: Ver como le pasamos este valor a login
        $scope.valor = 8;
      }, function (errorResponse) {
        if (errorResponse.status === 400) {
          PathService.get().login().go();
          // TODO: Ver como le pasamos este valor a login
          $scope.valor = 4;
          return;
        }
        console.error(errorResponse);
      });

    }]);

});
