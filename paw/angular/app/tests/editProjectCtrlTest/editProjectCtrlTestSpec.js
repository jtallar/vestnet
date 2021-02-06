'use strict'

define(['angular','paw2020a','angularMocks', 'restangular', 'editProjectCtrl', 'apiResponses','utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl,apiResponses,utilities){



  describe('Test edit project', function() {



    beforeEach(angular.mock.module("paw2020a"));


    var $controller;
    var editProjectCtrl;
    var $scope;
    var ProjectService, UrlService;
    var $rootScope;
    var $routeParams;
    var $httpBackend;

    var projectId, expectedProject, expectedProjCategories, expectedCats;





    beforeEach(inject(function( _$controller_, _projectService_ ,_urlService_,apiResponses,utilities, _$rootScope_, _$routeParams_, _$httpBackend_){
      $controller = _$controller_;
      $httpBackend = _$httpBackend_;
      $scope = {};
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      ProjectService = _projectService_;
      UrlService = _urlService_;
      expectedProject = apiResponses.project.data;
      expectedCats = apiResponses.categories.data;
      expectedProjCategories = apiResponses.projectCategories.data;
      projectId = expectedProject.id;


      $routeParams.id = projectId;

      utilities.resolvePromise(ProjectService, 'getById', apiResponses.project);
      utilities.resolvePromise(ProjectService, 'getCategories', apiResponses.categories);
      utilities.resolvePromise(UrlService, 'get', apiResponses.projectCategories);


      utilities.ignoreTestAside(_$httpBackend_);

      var cat = document.createElement('select');
      var sel = document.createElement('select')

      var i = 0;
      while(i < expectedCats.length){
        var option = document.createElement('option')
        option.setAttribute('value', expectedCats[i].id)
        cat.appendChild(option);
        i++;
      }


      document.getElementById = jasmine.createSpy('all-categories').and.returnValues(cat, sel);

      editProjectCtrl = $controller('editProjectCtrl', {$scope: $scope});

      $rootScope.$apply();

    }));

    describe('edit project controller content testing', function() {
      it('edit project controller should be defined', function() {
        expect(editProjectCtrl).toBeDefined();
      });

      it('should fetch project', function () {
        expect(ProjectService.getById).toHaveBeenCalled();
        expect($scope.project).toEqual(expectedProject)
      })

      it('should fetch all categories', function () {
        expect(ProjectService.getCategories).toHaveBeenCalledWith();
        expect($scope.categories).toEqual(expectedCats);
      });

      it('should fetch project categories', function () {
        //expect(UrlService.get).toHaveBeenCalledWith($scope.project.categories);
        var idsArray = [];
        expectedProjCategories.map(function (cat) {
          idsArray.push({id : cat.id});
        })

        expect(editProjectCtrl.selectedCategories.map(a => a.id).sort()).toEqual(idsArray.map(a => a.id).sort());
      });



      // it('should set errors because of no categories', function () {
      //   expect(UrlService.get).toHaveBeenCalledWith($scope.project.categories);
      //   $scope.project.categories.splice(0,$scope.project.categories.length);
      //   $scope.updateProject($scope.project);
      //   expect($scope.categoryCountError).toEqual(true);
      // });



    });


  })
});
