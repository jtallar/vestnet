define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'singleViewCtrl'], function (angular, angularMocks, paw2020a, chatCtrl) {
  describe('Testing single view', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var singleViewCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var ProjectService, UserService, MessageService, AuthService, UrlService;
    var expectedEnt, expectedInv, expectedProj, expectedMsgs, button;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses, projectService,urlService, messageService, AuthenticationService) {

      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      ProjectService = projectService;
      MessageService = messageService;
      UrlService = urlService;
      AuthService = AuthenticationService;
      $scope = $rootScope.$new();
      apiResp = apiResponses;
      expectedProj = apiResponses.project.data;
      $routeParams.id = expectedProj.id;

      utilities.resolvePromise(ProjectService, 'addStat', {});

      utilities.resolvePromise(ProjectService,'getById', apiResponses.project);

      button = document.createElement('button');
      document.getElementById = jasmine.createSpy('all').and.returnValue(button);

      singleViewCtrl = $controller('singleViewCtrl', {$scope: $scope});



      $scope.start = new Date();
      $scope.clicks = 0;



      utilities.ignoreTestAside($httpBackend);


      $rootScope.$apply();

    }));

    describe('Testing project inside data', function () {


      it('single view controller should be defined', function () {
        expect(singleViewCtrl).toBeDefined();
      });

      it('should have fetch project from api', function () {
        expect(ProjectService.getById).toHaveBeenCalled();
        expect($scope.project).toEqual(expectedProj)
      });

      it('should add stat',function () {
        $scope.addStatError = false;
        $scope.$destroy();
        $scope.$digest();
        expect(ProjectService.addStat).toHaveBeenCalled()
      });

      it('should add click', function () {
        button.click();
        $rootScope.$digest();
        expect($scope.clicks).toEqual(1);
      });


    })
  })

})
