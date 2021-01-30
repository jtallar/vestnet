'use strict';
var thisPath = window.location.pathname;
define(['paw2020a', 'directives/headerCtrl', 'directives/footer'], function(paw2020a) {

  paw2020a.controller('IndexCtrl', function($scope) {
    $scope.welcomeText = 'Welcome to your paw2020a page';
  });

});

// angular.module('paw2020a', ['ui.toggle']);

