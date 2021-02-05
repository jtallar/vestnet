define(['angular','paw2020a','angularMocks', 'restangular', 'newProjectCtrl', 'apiResponses', 'utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing new project', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var newProjectCtrl;
    var $scope;
    var $httpBackend, $routeParams, $rootScope;
    var ProjectService;
    var expectedCats;
    var indexUsed;

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

      var cat = document.createElement('select'); //this is just a test setting it is not the way we use it in our application
      indexUsed = 3;
      var i = 0;
      while(i < expectedCats.length){
        var option = document.createElement('option')
        option.setAttribute('value', expectedCats[i].id)
        cat.appendChild(option);
        i++;
      }

      cat.selectedIndex = indexUsed;
      document.getElementById = jasmine.createSpy('all-categories').and.returnValue(cat);




      $rootScope.$apply();
    }));

    describe('Testing form', function() {
      it('New project controller should be defined', function() {
        expect(newProjectCtrl).toBeDefined();
      });

      it('Scope category is equal to expectedCats', function () {
        expect(ProjectService.getCategories).toHaveBeenCalled();
        expect($scope.categories).toEqual(expectedCats)
      })

      it('should add category', function () {
        $scope.addCategory();
        expect(document.getElementById).toHaveBeenCalled();
        expect(newProjectCtrl.selectedCategories).toContain({id: $scope.categories[indexUsed].id})
      });

      it('should remove category', function () {
        var j = 0;
        while (j < $scope.categories.length){ //we supose every category is inside selectedCategories
          newProjectCtrl.selectedCategories.push({id: $scope.categories[j].id});
          j++;
        }

        expect(newProjectCtrl.selectedCategories).toContain({id: $scope.categories[indexUsed].id}); //here it is inside the array
        $scope.delCategory();
        expect(newProjectCtrl.selectedCategories).not.toContain({id: $scope.categories[indexUsed].id}); //here it is not

      })


    });
  })
});
