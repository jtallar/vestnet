'use strict'

define(['angular','paw2020a','angularMocks', 'restangular', 'feedCtrl', 'apiResponses',], function (angular, paw2020a, angularMocks, restangular, feedCtrl,apiResponses){



  describe('Testing feed', function() {



    beforeEach(angular.mock.module("paw2020a"));


    var $controller;
    var feedCtrl;
    var $scope;
    var AuthenticationFactory;
    var ProjectService;
    var expectedProjects;
    var $rootScope;
    var $routeParams;
    var $httpBackend;
    var UserService;



    beforeEach(inject(function( _$controller_, AuthenticationService, _userService_ , _projectService_ ,apiResponses, $q, _$rootScope_, _$routeParams_, _$httpBackend_){
      $controller = _$controller_;
      $httpBackend = _$httpBackend_;
      $scope = {};
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      AuthenticationFactory = AuthenticationService;
      UserService = _userService_;
      ProjectService = _projectService_;
      expectedProjects = apiResponses.projects.data;



      spyOn(AuthenticationFactory, 'isInvestor').and.callFake(function () {
        return apiResponses.isInvestor;
      });
      //
      spyOn(UserService, 'getFavorites').and.callFake(function (){
        var deferred = $q.defer();
        deferred.resolve(apiResponses.favs);
        return deferred.promise;
      })

      spyOn(ProjectService, 'getPage').and.callFake(function () {
        var deferred = $q.defer();
        deferred.resolve(apiResponses.projects);
        return deferred.promise;
      });

      spyOn(ProjectService, 'getCategories').and.callFake(function () {
        var deferred = $q.defer();
        deferred.resolve(apiResponses.categories);
        return deferred.promise;
      });



      $httpBackend.whenGET(/views.*/).respond(200, '');




      feedCtrl = $controller('feedCtrl', {$scope: $scope,AuthenticationService: AuthenticationFactory, projectService: ProjectService, userService: UserService});

      $rootScope.$apply();

    }));

      describe('feed controller testing', function() {
        it('feed controller should be defined', function() {
          expect(feedCtrl).toBeDefined();
        });

      it('Should be an investor', function () {
        expect($scope.isInvestor).toEqual(true);

      })

      it('Projects must be fetched from api', function () {
        expect(ProjectService.getPage).toHaveBeenCalled();
        expect($scope.projects).toEqual(expectedProjects);
      })

    });
  })
});
