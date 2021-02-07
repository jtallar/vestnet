'use strict'

define(['angular','paw2020a','angularMocks', 'restangular', 'feedCtrl', 'apiResponses','utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl,apiResponses,utilities){



  describe('Testing feed', function() {



    beforeEach(angular.mock.module("paw2020a"));


    var $controller;
    var feedCtrl;
    var $scope;
    var AuthenticationFactory, ProjectService, UserService;;
    var expectedProjects, expectedFavs, expectedCats;
    var $rootScope;
    var $routeParams;
    var $httpBackend;





    beforeEach(inject(function( _$controller_, AuthenticationService, _userService_ , _projectService_ ,apiResponses,utilities, _$rootScope_, _$routeParams_, _$httpBackend_){
      $controller = _$controller_;
      $httpBackend = _$httpBackend_;
      $scope = {};
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      AuthenticationFactory = AuthenticationService;
      UserService = _userService_;
      ProjectService = _projectService_;
      expectedProjects = apiResponses.projects.data;
      expectedFavs = apiResponses.favs.data;
      expectedCats = apiResponses.categories.data;


      //this will resolve api calls being tested inside controllers


      spyOn(AuthenticationFactory, 'isInvestor').and.callFake(function () {
        return apiResponses.isInvestor;
      });
      utilities.resolvePromise(UserService, 'getFavorites', apiResponses.favs);
      utilities.resolvePromise(ProjectService, 'getPage', apiResponses.projects);
      utilities.resolvePromise(ProjectService, 'getCategories', apiResponses.categories);


      utilities.ignoreTestAside(_$httpBackend_);





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

        it('should contain favorite', function () {
          expect(UserService.getFavorites).toHaveBeenCalled();
          expect($scope.containsFav(expectedFavs[1].projectId)).toEqual(true);
        });

          it('should delete favorite', function () {
            $scope.favTap(expectedFavs[0].projectId)
            expect($scope.favs).not.toContain(expectedFavs[0].projectId);
          });

          it('should add a favorite', function () {
            var i = 0, j = 0, found = false; //finding an id of a project that is not fav
            while (i < expectedProjects.length && !found){
              while (j < expectedFavs.length && !found){
                if(expectedProjects[i].id !== expectedFavs[j].projectId){
                  found = true;
                }
                j++;
              }
              if(!found){
                i++;
              }
            }
            $scope.favTap(expectedProjects[i].id);
            expect($scope.favs).toContain(expectedProjects[i].id);
          });

          it('should set error of min cost being higher than max cost', function () {
            $scope.maxCost = 50;
            $scope.minCost = 100;
            $scope.applyFilter();
            expect($scope.costRangeError).toEqual(true);
          });

          it('should set error of min percentage being higher than max percentage', function () {
            $scope.maxPercentage = 50;
            $scope.minPercentage = 3300;
            $scope.applyFilter();
            expect($scope.percentageRangeError).toEqual(true);
          });

      });


  })
});
