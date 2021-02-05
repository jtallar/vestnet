define(['angular','paw2020a','angularMocks', 'restangular', 'newProjectCtrl', 'apiResponses', 'utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing new project', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var newProjectCtrl;
    var $scope;
    var $httpBackend, $routeParams, $rootScope;
    var ProjectService;
    var expectedCats;

    beforeEach(inject(function(_$controller_, _projectService_, apiResponses,utilities, _$rootScope_,_$routeParams_, _$httpBackend_){
      $controller = _$controller_;
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      $httpBackend = _$httpBackend_;
      ProjectService = _projectService_;
      expectedCats = apiResponses.categories.data;

      $scope = {};
      utilities.resolvePromise(ProjectService, 'getCategories', apiResponses.categories);
      utilities.ignoreTestAside($httpBackend);
      newProjectCtrl = $controller('newProjectCtrl', {$scope: $scope, projectService: ProjectService});




      $rootScope.$apply();
    }));

    describe('Testing form', function() {
      it('New project controller should be defined', function() {
        expect(newProjectCtrl).toBeDefined();
      });

      it('Scopes Equality', function () {
        expect(ProjectService.getCategories).toHaveBeenCalled();
        expect($scope.categories).toEqual(expectedCats)
      })

      it('should add category', function () {

        var initialCat = document.createElement('select');
        var finalCats = document.createElement('select');
        var i = 0;

        while(i < $scope.categories.length){
          var option = document.createElement('option')
          option.setAttribute('value', $scope.categories[i].id)
          initialCat.appendChild(option);
          i++;
        }

        initialCat.setAttribute('selectedIndex')
        document.getElementById = jasmine.createSpy('all-categories').and.returnValue(initialCat);
        document.getElementById = jasmine.createSpy('final-categories').and.returnValue(finalCats);



        $scope.addCategory();
      });


    });
  })
});
