define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'chatCtrl'], function (angular, angularMocks, paw2020a, chatCtrl) {
  describe('Testing chat', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var chatCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var ProjectService, UserService, MessageService, AuthService, UrlService;
    var expectedEnt, expectedInv, expectedProj, expectedMsgs;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses, projectService,urlService, messageService, AuthenticationService) {

      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      ProjectService = projectService;
      MessageService = messageService;
      UrlService = urlService;
      AuthService = AuthenticationService;
      $scope = {};
      apiResp = apiResponses;
      expectedEnt = apiResponses.userEntrepreuner.data;
      expectedProj = apiResponses.project.data;
      expectedMsgs = apiResponses.messages.data;
      $routeParams.id1 = apiResponses.project.data.id;
      $routeParams.id2 = apiResponses.userInvestor.data.id;
      
      spyOn(AuthService, 'isInvestor').and.callFake(function () {
        return true;
      });

      spyOn(AuthService, 'isEntrepreneur').and.callFake(function () {
        return false;
      });



      utilities.resolvePromise(UrlService, 'get', apiResponses.userEntrepreuner);
      utilities.resolvePromise(ProjectService, 'getById', apiResponses.project);
      utilities.resolvePromise(MessageService, 'setSeen', {status:200, data: {}});
      utilities.resolvePromise(MessageService, 'getChat', apiResponses.messages);

      var div = document.createElement('select');
      document.getElementById = jasmine.createSpy('chatbox-scroll').and.returnValue(div); //to mock chatbox-scroll div

      chatCtrl = $controller('chatCtrl', {$scope: $scope, AuthenticationService: AuthService, projectService: ProjectService, messageService: MessageService});

      utilities.ignoreTestAside($httpBackend);

      spyOn(chatCtrl, 'handleChatResponse').and.callThrough();

      $rootScope.$apply();

    }));

    describe('Testing chat investor', function () {


      it('chatCtrl should be defined', function () {
        expect(chatCtrl).toBeDefined();
      });


      it('should have checked role', function () {
        expect(AuthService.isInvestor).toHaveBeenCalled();
      });

      it('should fetch chats', function () {
        expect(MessageService.getChat).toHaveBeenCalled();
        expect($scope.chats).toEqual(expectedMsgs.reverse());
      });


      it('should have fetch project from api', function () {
        expect(ProjectService.getById).toHaveBeenCalled();
        expect($scope.project).toBe(expectedProj); //$scope.project contains every attribute of expected project but not the other way around so we cannot use equal
      });

      it('should have saved entrepreuner as user', function () {
        expect(ProjectService.getById).toHaveBeenCalled();
        expect(UrlService.get).toHaveBeenCalled();
        expect($scope.user).toEqual(expectedEnt);
      });

      it('rootscope should emit signal of read message', function () {
        spyOn($rootScope, '$emit');
        chatCtrl.setChatAsSeen({});
        $rootScope.$digest()
        expect($rootScope.$emit).toHaveBeenCalledWith('notificationChange');
      });

      it('should enable offer', function () {
        $scope.lastMessage.expInDays = 0;
        $scope.lastMessage.answered = false;
        expect($scope.offerEnabled).toEqual(true);
      })

      it('should not enable offer as last offer is not expired', function () {
        delete $scope.chats[0].accepted; //this is to add expInDays
        // expect($scope.chats[0].hasOwnProperty('accepted')).toEqual(false); //
        chatCtrl.handleChatResponse($scope.chats);
        expect($scope.offerEnabled).toEqual(false);
      })

    })
  })

})
