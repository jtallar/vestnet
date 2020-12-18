'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService', 'services/messageService'], function(paw2020a) {
  paw2020a.directive('vnheader', ['PathService', 'AuthenticationService', 'messageService', '$interval', function(PathService, AuthenticationService, messageService, $interval) {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'views/directives/header.html',
      controller: function ($scope, $rootScope) {
        console.log("controller header");
        $scope.eDropdown = ['dashboard', 'users'];
        // $scope.eIcons = ['home-icon', 'user-icon'];
        $scope.iDropdown = ['requests', 'messages', 'users'];
        $scope.gOptions = ['welcome', 'login', 'signUp'];
        $scope.gIcons = ['home-icon', 'login-icon', 'signup-icon'];
        $scope.notifications = false;
        $scope.userid = 2;

        // TODO: Check if this works
        var checkNotif = function() {
          console.log('entrando a checkNotif');
          if ($scope.valor === 2) return;
          messageService.notificationCount().then(function (response) {
            $scope.notifications = (response.data.unread !== 0);
            console.log($scope.notifications);
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
