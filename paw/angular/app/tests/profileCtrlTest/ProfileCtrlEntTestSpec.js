define(['angular','paw2020a','angularMocks', 'restangular', 'profileCtrl', 'apiResponses', 'utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing dashboard', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var profileCtrl;
    var $scope;
    var $httpBackend, $routeParams, $rootScope;
    var AuthService, UserService, UrlService;
    var expectedUser, expectedLocation,expectedFavs, index, projectIndex;
    beforeEach(inject(function(_$controller_, _projectService_, apiResponses,utilities, _$rootScope_,_$routeParams_, _$httpBackend_, userService, AuthenticationService, urlService){
      $controller = _$controller_;
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      UserService = userService;
      AuthService = AuthenticationService;
      UrlService = urlService;
      $httpBackend = _$httpBackend_;
      expectedUser = apiResponses.userInvestor.data;
      expectedLocation = apiResponses.invLocation.data;
      expectedFavs = apiResponses.profileFavs.data;
      index = 1;
      projectIndex = expectedFavs[index];
      $scope = {};

      spyOn(AuthService, 'isInvestor').and.callFake(function () {
        return false;
      });

      utilities.resolvePromise(UserService, 'getLoggedUser', apiResponses.userInvestor);
      utilities.resolvePromise(UserService,'putFavorite', {});
      utilities.resolvePromise(UrlService, 'get', apiResponses.invLocation);
      utilities.resolvePromise(UserService, 'getProfileFavorites', apiResponses.profileFavs);
      profileCtrl = $controller('profileCtrl', {$scope: $scope});

      utilities.ignoreTestAside($httpBackend);
      $rootScope.$apply();
    }));

    describe('Testing form', function() {
      it('New project controller should be defined', function() {
        expect(profileCtrl).toBeDefined();
      });

      it('should be an entrepreuner', function () {
        expect(AuthService.isInvestor).toHaveBeenCalled();
        expect($scope.isInvestor).toEqual(false);
      });

      it('should not try to fetch favs', function () {
        expect(UserService.getProfileFavorites).not.toHaveBeenCalled();
        expect($scope.allFavs).toEqual([]);
        expect($scope.showFavs).toEqual([]);
      })

      it('should not try to remove favs even if it not showed in the html', function () {
        $scope.removeFav(projectIndex.id);
        expect(UserService.putFavorite).not.toHaveBeenCalled();
      })





    });
  })
});
