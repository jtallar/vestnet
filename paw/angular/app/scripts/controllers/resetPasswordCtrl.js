    'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('resetPasswordCtrl', ['userService', 'PathService', '$scope', '$routeParams', function(userService, PathService, $scope, $routeParams) {
      var token = $routeParams.token;
      if (token === undefined) {
        PathService.get().login().go({code: 5});
        return;
      }
      $scope.loading = false;

      $scope.resetPassword = function (passwordBlock) {
        $scope.loading = true;
        passwordBlock.token = token;
        userService.resetPassword(passwordBlock).then(function () {
          $scope.loading = false;
          PathService.get().login().go({code: 10});
        }, function (errorResponse) {
          $scope.loading = false;
          if (errorResponse.status === 400) {
            PathService.get().login().go({code: 6});
            return;
          }
          console.error(errorResponse);
        })
      }
    }]);

});
