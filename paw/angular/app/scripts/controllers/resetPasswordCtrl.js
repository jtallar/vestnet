    'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('resetPasswordCtrl', ['userService', 'PathService', '$scope', '$routeParams', function(userService, PathService, $scope, $routeParams) {
      var token = $routeParams.token;
      if (token === undefined) {
        PathService.get().login().go();
        // TODO: Ver como le pasamos este valor a login
        $scope.valor = 5;
        return;
      }

      $scope.resetPassword = function (passwordBlock) {
        passwordBlock.token = token;
        userService.resetPassword(passwordBlock).then(function () {
          PathService.get().login().go();
          // TODO: Ver como le pasamos este valor a login
          $scope.valor = 10;
        }, function (errorResponse) {
          if (errorResponse.status === 400) {
            PathService.get().login().go();
            // TODO: Ver como le pasamos este valor a login
            $scope.valor = 6;
            return;
          }
          console.error(errorResponse);
        })
      }
    }]);

});
