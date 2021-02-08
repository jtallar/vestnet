'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService', 'services/userService'],
  function(paw2020a) {
    paw2020a.controller('loginCtrl', ['PathService', 'AuthenticationService', 'userService', '$scope', '$routeParams', '$rootScope', function(PathService, AuthenticationService, userService, $scope, $routeParams, $rootScope) {
      var code = parseInt($routeParams.code);
      $scope.code = (isNaN(code)) ? 0 : code;

      var redirectUrl = $routeParams.url;
      if (!redirectUrl) redirectUrl = PathService.get().projects().path;

      $scope.loading = false;

      $scope.login = function (user) {
        $scope.loading = true;
        AuthenticationService.login(user).then(function () {
          $scope.loading = false;
          $rootScope.$emit('credentialsChanged');
          PathService.get().setFullUrl(redirectUrl).go();
        }, function (errorResponse) {
          $scope.loading = false;
          if (errorResponse.data.errorCode === 12) {
            $scope.code = 2;
            userService.requestVerification(user.username).then(function (response) {
              // Do nothing
            }, function (errorResponse) {
              console.error(errorResponse);
            });
            return;
          }
          $scope.code = 1;
        })
      }
    }]);

});
