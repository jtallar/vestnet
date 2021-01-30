  'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.controller('errorCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    var code = $routeParams.code;
    $scope.eCode = (code) ? code : 404;
  }]);

});
