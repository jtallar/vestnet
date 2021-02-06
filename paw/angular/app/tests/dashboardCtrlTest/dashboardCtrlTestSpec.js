define(['angular','paw2020a','angularMocks', 'restangular', 'dashboardCtrl', 'apiResponses', 'utilities'], function (angular, paw2020a, angularMocks, restangular, feedCtrl){

  describe('Testing dashboard', function() {
    beforeEach(angular.mock.module("paw2020a"));

    var $controller;
    var dashboardCtrl;
    var $scope;
    var $httpBackend, $routeParams, $rootScope;
    var MessageService,UserService, ProjectService;
    var ProjectService;
    var expectedMsgCount, expectdProjects, projectId, index;
    beforeEach(inject(function(_$controller_, _projectService_, apiResponses,utilities, _$rootScope_,_$routeParams_, _$httpBackend_, messageService, userService, projectService){
      $controller = _$controller_;
      $rootScope = _$rootScope_;
      $routeParams = _$routeParams_;
      MessageService = messageService;
      UserService = userService;
      ProjectService = projectService;
      $httpBackend = _$httpBackend_;
      expectdProjects = apiResponses.notFundedProjects.data;
      expectedMsgCount = apiResponses.msgCount.data.unread;
      projectId = apiResponses.msgCount.data.route;

      var j = 0;
      while(j < expectdProjects.length && index === undefined){
        index = expectdProjects[j].id === projectId ? j : undefined;
        j++;
      }

      $scope = {};


      utilities.resolvePromise(UserService, 'getLoggedProjects', apiResponses.notFundedProjects);
      utilities.resolvePromise(MessageService, 'projectNotificationCount', apiResponses.msgCount);

      dashboardCtrl = $controller('dashboardCtrl', {$scope: $scope, projectService: ProjectService});

      utilities.ignoreTestAside($httpBackend);
      $rootScope.$apply();
    }));

    describe('Testing form', function() {
      it('Dashboard controller should be defined', function() {
        expect(dashboardCtrl).toBeDefined();
      });

      it('should get two days difference', function () { //this is the way we calculate the difference of days, today does not count
        var date = new Date();
        date.setDate(date.getDate() - 2);
        date.setHours(date.getHours() - 1);
        expect($scope.daysAgo(date)).toEqual(2);
      });
      
      it('should fetch projects from get logged projects', function () {
        expect(UserService.getLoggedProjects).toHaveBeenCalled();
        expect($scope.projects).toEqual(expectdProjects);
        
      })

      it('should return a minute', function () {
        var minute = 1000 * 60;
        expect($scope.millisToMinSec(minute)).toEqual('1:00');
      });

      it('should map and set msgCount', function () {
        expect(MessageService.projectNotificationCount).toHaveBeenCalled();
        expect($scope.projects[index].msgCount).toEqual(expectedMsgCount);
      })

      it('should trigger notification change', function () {
        spyOn($rootScope, '$emit');
        $scope.projects[index].openMessages = false;
        $scope.projects[index].msgCount = 0; //different from what api is sending
        $scope.fetchMessage(projectId, index);
        expect(MessageService.projectNotificationCount).toHaveBeenCalled();
        $rootScope.$digest()
        expect($rootScope.$emit).toHaveBeenCalledWith('notificationChange');
      })



    });
  })
});
