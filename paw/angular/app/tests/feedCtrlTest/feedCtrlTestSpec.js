'use strict'

define(['angular','paw2020a','angularMocks', 'restangular', 'feedCtrl', 'apiResponses'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){



  describe('Testing feed', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var feedCtrl;
    var $scope;
    var AuthenticationFactory;
    var ProjectFactory;
    var UserFactory;

    beforeEach(inject(function(_$controller_, AuthenticationService,userService,projectService,apiResponses){
      $controller = _$controller_;
      $scope = {};
      ProjectFactory = projectService;
      AuthenticationFactory = AuthenticationService;
      UserFactory = userService;

      spyOn(AuthenticationFactory, 'isInvestor').and.callFake(function () {
        return apiResponses.isInvestor;
      });

      feedCtrl = $controller('feedCtrl', {$scope: $scope, AuthenticationService: AuthenticationFactory});
    }));

    describe('feed controller testing', function() {
      it('feed controller should be defined', function() {
        expect(feedCtrl).toBeDefined();
      });

      it('Should be an investor', function () {
        expect($scope.isInvestor).toEqual(true);
      })



    });
  })
});
