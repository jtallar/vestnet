'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('vnheader', function() {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'views/directives/header.html',
      controller: function ($scope) {
        $scope.eDropdown = ['dashboard', 'deals', 'profile'];
        // $scope.eIcons = ['home-icon', 'user-icon'];
        $scope.iDropdown = ['requests', 'profile'];
        $scope.gOptions = ['welcome', 'login', 'signUp'];
        $scope.gIcons = ['home-icon', 'login-icon', 'signup-icon'];
      }
    };
  });

});
