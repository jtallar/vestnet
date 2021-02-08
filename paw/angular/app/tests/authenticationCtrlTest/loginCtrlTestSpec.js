define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'loginCtrl'], function (angular, angularMocks, paw2020a) {
  describe('Testing Authentication', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var loginCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var ut;
    var AuthService;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses, AuthenticationService) {
      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      $scope = {};
      apiResp = apiResponses;
      AuthService = AuthenticationService;

      utilities.resolvePromise(AuthService, 'login', {});


      loginCtrl = $controller('loginCtrl', {$scope: $scope});




      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing login ctrl', function () {
      it('loginCtrl should be defined', function () {
        expect(loginCtrl).toBeDefined();
      });

      it('should emit credentials changed signal', function () {
        spyOn($rootScope, '$emit');
        $scope.login();
        $rootScope.$digest();
        expect($rootScope.$emit).toHaveBeenCalledWith('credentialsChanged');
      })



    })


  })

})
