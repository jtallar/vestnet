'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('loginCtrl', ['PathService', 'AuthenticationService', '$scope', '$routeParams', '$log', '$rootScope', function(PathService, AuthenticationService, $scope, $routeParams, $log, $rootScope) {
      var code = parseInt($routeParams.code);
      $scope.code = (isNaN(code)) ? 0 : code;

      var redirectUrl = $routeParams.url;
      if (!redirectUrl) redirectUrl = PathService.get().projects().path;

      $scope.loading = false;

      $scope.login = function (user) {
        $scope.loading = true;
        AuthenticationService.login(user).then(function () {
          $rootScope.$emit('credentialsChanged');
          PathService.get().setFullUrl(redirectUrl).go();
        }, function (errorResponse) {
          $log.info('Response status: ' + errorResponse.status);
          $scope.code = 1;
          $scope.loading = false;
        })
      }
    }]);

});
