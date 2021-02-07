define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'services/PathService'], function (angular, angularMocks, paw2020a,apiResponses,utilities, PathService) {
  describe('Testing Configuration', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var loginCtrl;
    var $httpBackend, $routeParams, $rootScope, $location, $service;
    var $scope;
    var apiResp;
    var paths;
    var AuthService, PathService;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_,_$location_, utilities,apiResponses, AuthenticationService, _PathService_) {
      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      $location =_$location_;
      $scope = {};
      apiResp = apiResponses;
      AuthService =  AuthenticationService;
      PathService = _PathService_;


      spyOn($location, 'path').and.callThrough();



      paths = {
        login: function() {
          return PathService.get().login().absolutePath();
        },
        feed: function() {
          return PathService.get().projects().absolutePath();
        },
        index: function(){
          return PathService.get().index().absolutePath();
        },
        error: function() {
          return PathService.get().error().absolutePath();
        },
        investorsRoute: function () {
          return PathService.get().requests().absolutePath(); //example of investors route
        },
        entrepreunerRoute: function () {
          return PathService.get().dashboard().absolutePath();
        },
        freeRoute: function () {
          return PathService.get().users().absolutePath();
        },
        authRoute: function () {
          return PathService.get().profile().absolutePath();
        },
        noAuthRoute: function () {
          return PathService.get().signUp().absolutePath();
        },
        logout: function () {
          return PathService.get().logout().absolutePath();
        }

      };


      utilities.ignoreTestAside($httpBackend);


    }));

    describe('when not logged in and go to a page which requires auth', function() {
      beforeEach(function() {
        spyOn(AuthService, 'isLoggedIn').and.returnValue(false);
        $rootScope.$apply(function() {
          $location.path(paths.authRoute());
          $location.url = function () {
            return paths.authRoute();
          }
        });
      });

      it('should redirect to login page', function() {
        expect($location.path).toHaveBeenCalledWith(paths.login());
      });
    });

    describe('when not logged in and go to logout page', function() {
      beforeEach(function() {
        spyOn(AuthService, 'isLoggedIn').and.returnValue(false);
        $rootScope.$apply(function() {
          $location.path(paths.logout());
          $location.url = function () {
            return paths.logout();
          }
        });
      });

      it('should redirect to index', function() {
        expect($location.path).toHaveBeenCalledWith(paths.index());
      });
    });

    describe('when logged in and go to a page for no authenticated users', function () {
      beforeEach(function() {
        spyOn(AuthService, 'isLoggedIn').and.returnValue(true);
        $rootScope.$apply(function() {
          $location.path(paths.noAuthRoute());
          $location.url = function () {
            return paths.noAuthRoute();
          }
        });
      });

      it('should redirect to login page', function() {
        expect($location.path).toHaveBeenCalledWith(paths.feed());
      });


    });


    describe('when logged in as investor and go to a page for entrepreuners', function() {
      beforeEach(function() {
        spyOn(AuthService, 'isLoggedIn').and.returnValue(true);
        spyOn(AuthService, 'isInvestor').and.returnValue(true);
        $rootScope.$apply(function() {
          $location.path(paths.entrepreunerRoute());
          $location.url = function () {
            return paths.entrepreunerRoute();
          }
        });
      });

      it('should redirect to error page', function() {
        expect($location.path).toHaveBeenCalledWith(paths.error());
      });
    });

    describe('when logged in as entrepreuner and go to a page for investors', function() {
      beforeEach(function() {
        spyOn(AuthService, 'isLoggedIn').and.returnValue(true);
        spyOn(AuthService, 'isInvestor').and.returnValue(false);
        $rootScope.$apply(function() {
          $location.path(paths.investorsRoute());
          $location.url = function () {
            return paths.investorsRoute();
          }
        });
      });

      it('should redirect to error page', function() {
        expect($location.path).toHaveBeenCalledWith(paths.error());
      });
    });




  })

})

