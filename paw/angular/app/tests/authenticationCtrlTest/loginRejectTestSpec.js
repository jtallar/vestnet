define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'loginCtrl'], function (angular, angularMocks, paw2020a) {
  describe('Testing Authentication', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var loginCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var ut;
    var AuthService, UserService;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities, apiResponses, AuthenticationService, userService) {
      $controller = _$controller_;
      $httpBackend = _$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      $scope = {};
      apiResp = apiResponses;
      AuthService = AuthenticationService;
      UserService = userService;

      utilities.rejectPromise(AuthService, 'login', {data: {errorCode: 12}});
      utilities.resolvePromise(UserService, 'requestVerification', {})


      loginCtrl = $controller('loginCtrl', {$scope: $scope});


      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing login failure', function () {
      it('loginCtrl should be defined', function () {
        expect(loginCtrl).toBeDefined();
      });

      it('should set scope code 2', function () {
        var user = {username : 'fran'};

        $scope.login(user);
        $rootScope.$digest();
        expect($scope.code).toEqual(2);
        expect(UserService.requestVerification).toHaveBeenCalledWith(user.username);
      })


    })


  })
})
