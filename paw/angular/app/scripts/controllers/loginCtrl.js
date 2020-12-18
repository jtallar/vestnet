'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('loginCtrl', ['PathService', 'AuthenticationService', '$scope', '$routeParams', '$log', function(PathService, AuthenticationService, $scope, $routeParams, $log) {
      var code = $routeParams.code;
      $scope.code = (code === undefined) ? 0 : code;
      $scope.loading = false;

      $scope.login = function (user) {
        $scope.loading = true;
        AuthenticationService.login(user).then(function () {
          // TODO: Ver como hacer para que vaya a donde estaba yendo
          PathService.get().projects().go();
        }, function (errorResponse) {
          $log.info('Response status: ' + errorResponse.status);
          $scope.code = 1;
          $scope.loading = false;
        })
      }
    }]);

});
