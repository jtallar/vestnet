  'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('requestPasswordCtrl', ['userService', 'PathService', '$scope', function(userService, PathService, $scope) {
    $scope.invalidEmail = false;

    $scope.requestNewPassword = function (mail) {
      $scope.invalidEmail = false;
      userService.requestPassword(mail).then(function () {
        PathService.get().login().go({code: 9});
      }, function (errorResponse) {
        if (errorResponse.status === 404 || errorResponse.status === 400) {
          $scope.invalidEmail = true;
          return;
        }
        console.error(errorResponse);
      })
    }
  }]);

});
