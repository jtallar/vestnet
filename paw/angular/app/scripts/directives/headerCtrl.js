'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'], function(paw2020a) {
  paw2020a.directive('vnheader', ['PathService', 'AuthenticationService', function(PathService, AuthenticationService) {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'views/directives/header.html',
      controller: function ($scope, $rootScope) {
        $scope.eDropdown = ['dashboard', 'users'];
        // $scope.eIcons = ['home-icon', 'user-icon'];
        $scope.iDropdown = ['requests', 'messages', 'users'];
        $scope.gOptions = ['welcome', 'login', 'signUp'];
        $scope.gIcons = ['home-icon', 'login-icon', 'signup-icon'];
        // TODO: Update notifications periodically
        $scope.notifications = true ;
        $scope.userid = 2;

        $rootScope.$watch('role', function (newVal) {
          $scope.valor = newVal.value;
        });

        $scope.logout = function () {
          AuthenticationService.logout();
          PathService.get().index().go();
          $rootScope.$emit('credentialsChanged');
        }
      }
    };
  }]);

});
