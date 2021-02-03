define(['angular','paw2020a','angularMocks', 'restangular', 'newProjectCtrl'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing new project', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var feedCtrl;
    var $scope;

    beforeEach(inject(function(_$controller_){
      $controller = _$controller_;
      $scope = {};
      feedCtrl = $controller('newProjectCtrl', {$scope: $scope});
    }));

    describe('Testing form', function() {
      it('New project controller should be defined', function() {
        expect(feedCtrl).toBeDefined();
      });




    });
  })
});
