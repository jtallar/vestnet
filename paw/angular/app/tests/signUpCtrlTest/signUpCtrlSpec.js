define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'signUpCtrl'], function (angular, angularMocks, paw2020a, signUpCtrl) {
  describe('Testing sign up', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var signUpCtrl;
    var LocationService, UserService;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var expectedCountries, expectedStates, expectedCities;
    var apiResp;
    var ut;

    beforeEach(inject(function (_$controller_, locationService,userService, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses) {
      $controller = _$controller_;
      LocationService = locationService;
      UserService = userService;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      expectedCountries = apiResponses.countries.data;
      expectedCities = apiResponses.cities.data;
      expectedStates = apiResponses.states.data;
      $scope = {};
      apiResp = apiResponses;
      ut = utilities;

      utilities.resolvePromise(LocationService, 'getCountryList', apiResponses.countries);
      utilities.resolvePromise(LocationService, 'getStateList', apiResponses.states);
      utilities.resolvePromise(LocationService, 'getCityList', apiResponses.cities);




      signUpCtrl = $controller('signUpCtrl', {$scope: $scope, userService: UserService});




      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing sign up form', function () {
      it('signUpCtrl should be defined', function () {
        expect(signUpCtrl).toBeDefined();
      });

      it('should retrieve countries', function () {
        expect(LocationService.getCountryList).toHaveBeenCalled();
        expect($scope.countriesArray).toEqual(expectedCountries);
      });

      it('should retrieve states when country is changed', function () {
        $scope.countryChange();
        expect(LocationService.getStateList).toHaveBeenCalled();
        expect($scope.statesArray).toEqual(expectedStates);
      });

      it('should retrieve cities when country is changed', function () {
        $scope.countryChange();
        expect(LocationService.getCityList).toHaveBeenCalled();
        expect($scope.citiesArray).toEqual(expectedCities);
      });

      it('should retrieve cities when state is changed', function () {
        $scope.stateChange();
        expect(LocationService.getStateList).toHaveBeenCalled();
        expect($scope.citiesArray).toEqual(expectedCities);
      });

      it('should reject sign up because of form errors', function () {
        ut.rejectPromise(UserService, 'createUser', apiResp.formErrorResponse);
        $scope.createUser({});
        $rootScope.$digest();
        expect(UserService.createUser).toHaveBeenCalled();
        expect($scope.serverFormErrors).toEqual(true);
      });

      it('should reject sign up because of existing user', function () {
        ut.rejectPromise(UserService, 'createUser', apiResp.userExistingError);
        $scope.createUser({});
        $rootScope.$digest();
        expect(UserService.createUser).toHaveBeenCalled();
        expect($scope.userExistsError).toEqual(true);
      });


    })


  })

})
