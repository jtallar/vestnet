define(['angular','paw2020a','angularMocks', 'restangular', 'profileCtrl', 'apiResponses', 'utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing profile', function() {
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
        return true;
      });

      utilities.resolvePromise(UserService, 'getLoggedUser', apiResponses.userInvestor);
      utilities.resolvePromise(UserService,'putFavorite', {})
      utilities.resolvePromise(UrlService, 'get', apiResponses.invLocation);
      utilities.resolvePromise(UserService, 'getProfileFavorites', apiResponses.profileFavs);
      profileCtrl = $controller('profileCtrl', {$scope: $scope});

      utilities.ignoreTestAside($httpBackend);
      $rootScope.$apply();
    }));

    describe('Testing profile entrepreuner data', function() {
      it('Profile controller should be defined', function() {
        expect(profileCtrl).toBeDefined();
      });

      it('Should be an investor', function () {
          expect(AuthService.isInvestor).toHaveBeenCalled();
          expect($scope.isInvestor).toEqual(true);
      });

      it('should bring logged investor', function () {
        expect(UserService.getLoggedUser).toHaveBeenCalled();
        expect($scope.user).toEqual(expectedUser);
      })

      it('should fetch profile location', function () {
        expect(UrlService.get).toHaveBeenCalledWith(expectedUser.location);
        expect($scope.user.country).toEqual(expectedLocation.country);
        expect($scope.user.state).toEqual(expectedLocation.state);
        expect($scope.user.city).toEqual(expectedLocation.city);
      });

      it('should fetch favs', function () {
        expect(UserService.getProfileFavorites).toHaveBeenCalled();
        expect($scope.allFavs).toEqual(expectedFavs);
      });

      it('should set correctly fav percentage', function () {
        var per = expectedFavs[index].fundingCurrent / expectedFavs[index].fundingTarget;
        expect($scope.allFavs[index].percentage).toEqual(per);
      })

      it('should remove favorite', function () {
        $scope.removeFav(projectIndex.id);
        $rootScope.$digest();
        expect(UserService.putFavorite).toHaveBeenCalledWith(projectIndex.id, false);
        expect($scope.allFavs).not.toContain(projectIndex);
        expect($scope.showFavs).not.toContain(projectIndex);
      });




    });
  })
});
