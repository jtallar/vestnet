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


        var cat = document.createElement('select');
        // var sel = document.createElement('select');
        var i = 0;

        while(i < $scope.categories.length){
          var option = document.createElement('option')
          option.setAttribute('value', $scope.categories[i].id)
          cat.appendChild(option);
          i++;
        }

        // var indexToAdd = 3;

        cat.selectedIndex = 5;

        cat.setAttribute("selectedIndex", 2)

        console.log(newProjectCtrl.objectFromOption(cat.options[cat.selectedIndex]))

        console.log(cat.selectedIndex)

        document.getElementById = jasmine.createSpy('all-categories').and.returnValue(cat);
        // document.getElementById = jasmine.createSpy('final-categories').and.returnValue(sel);



        $scope.addCategory();

        // console.log($scope.cat.selectedIndex)

        // console.log($scope.cat);

        // console.log($scope.cat.selectedIndex)
      });


    });
  })
});
