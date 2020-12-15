  'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('requestPasswordCtrl', ['userService', 'PathService', '$scope', function(userService, PathService, $scope) {
    $scope.invalidEmail = false;

    $scope.requestNewPassword = function (mail) {
      $scope.invalidEmail = false;
      userService.requestPassword(mail).then(function () {
        PathService.get().login().go();
        // TODO: Ver como le pasamos este valor a login
        $scope.valor = 9;
      }, function (errorResponse) {
        if (errorResponse.status === 404) {
          $scope.invalidEmail = true;
          return;
        }
        console.error(errorResponse);
      })
    }
  }]);

});
