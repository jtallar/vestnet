'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService', 'services/messageService'], function(paw2020a) {
  paw2020a.directive('vnheader', ['PathService', 'AuthenticationService', 'messageService', '$interval', function(PathService, AuthenticationService, messageService, $interval) {
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
        $scope.notifications = false;
        $scope.userid = 2;

        var checkNotif = function() {
          if ($scope.valor === 2) return;
          messageService.notificationCount().then(function (response) {
            $scope.notifications = (response.data.unread !== 0);
          }, function (errorResponse) {
            console.error(errorResponse);
          })
        };

        $rootScope.$watch('role', function (newVal) {
          $scope.valor = newVal.value;
          checkNotif();
        });

        $interval(checkNotif, 5 * 60000);

        $scope.logout = function () {
          AuthenticationService.logout();
          PathService.get().index().go();
          $rootScope.$emit('credentialsChanged');
        }
      }
    };
  }]);

});
