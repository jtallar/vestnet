'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService', 'services/messageService'], function(paw2020a) {
  paw2020a.directive('vnheader', ['PathService', 'AuthenticationService', 'messageService', '$interval', function(PathService, AuthenticationService, messageService, $interval) {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'views/directives/header.html',
      controller: function ($scope, $rootScope) {
        $scope.eDropdown = ['dashboard', 'profile'];
        $scope.iDropdown = ['requests', 'messages', 'profile'];
        $scope.notifications = 0;

        var checkNotif = function() {
          if ($scope.valor === 2) return;
          messageService.notificationCount().then(function (response) {
            $scope.notifications = response.data.unread;
          }, function (errorResponse) {
            console.error(errorResponse);
          })
        };

        $rootScope.$watch('role', function (newVal) {
          $scope.valor = newVal.value;
          checkNotif();
        });

        $rootScope.$on('messageRead', function (event) {
          if ($scope.notifications > 0) $scope.notifications--;
        });

        $interval(checkNotif, 60000);

        $scope.logout = function () {
          PathService.get().logout().go();
        }
      }
    };
  }]);

});
