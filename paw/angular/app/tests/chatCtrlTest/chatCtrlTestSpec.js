define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'chatCtrl'], function (angular, angularMocks, paw2020a, chatCtrl) {
  describe('Testing chat', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var chatCtrl;
    var LocationService, UserService;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses) {
      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      $scope = {};
      apiResp = apiResponses;


      chatCtrl = $controller('chatCtrl', {$scope: $scope});




      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing sign up form', function () {
      it('chatCtrl should be defined', function () {
        expect(chatCtrl).toBeDefined();
      });



    })


  })

})
