  'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('requestPasswordCtrl', ['userService', 'PathService', '$scope', function(userService, PathService, $scope) {
    $scope.invalidEmail = false; $scope.serverRetryError = false;
    $scope.loading = false;

    $scope.requestNewPassword = function (mail) {
      $scope.loading = true;
      $scope.invalidEmail = false; $scope.serverRetryError = false;
      userService.requestPassword(mail).then(function () {
        $scope.loading = false;
        PathService.get().login().go({code: 9});
      }, function (errorResponse) {
        $scope.loading = false;
        if (errorResponse.status === 404 || errorResponse.status === 400) {
          $scope.invalidEmail = true;
          return;
        } else if (errorResponse.status === 503) {
          $scope.serverRetryError = true;
          return;
        }
        console.error(errorResponse);
      })
    }
  }]);

});
