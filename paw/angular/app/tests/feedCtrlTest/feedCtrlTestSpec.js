'use strict'

define(['angular','paw2020a','angularMocks', 'restangular', 'feedCtrl'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('feedCtrl', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;

    beforeEach(inject(function(_$controller_){
      $controller = _$controller_;
    }));

    describe('$scope.ID', function() {
      it('Check the scope object', function() {
        var $scope = {};
        var controller = $controller('feedCtrl', {$scope: $scope});
        expect($scope.page).toEqual(1);
      });
    });
  })
});
