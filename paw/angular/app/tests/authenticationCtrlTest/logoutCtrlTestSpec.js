define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'logoutCtrl'], function (angular, angularMocks, paw2020a) {
  describe('Testing Authentication', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var logoutCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var AuthService;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses, PathService) {
      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      $scope = $rootScope.$new();
      apiResp = apiResponses;


      // spyOn(PathService, 'get')
      logoutCtrl = $controller('logoutCtrl', {$scope: $scope});


      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing logout ctrl', function () {
      it('logoutCtrl should be defined', function () {
        expect(logoutCtrl).toBeDefined();
      });

      // it('should emit credentials changed signal',function () {
      //   spyOn($rootScope, '$emit');
      //   expect($rootScope.$emit).toHaveBeenCalledWith('credentialsChanged');
      // })


    })


  })

})
