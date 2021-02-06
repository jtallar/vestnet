define(['angular', 'angularMocks', 'paw2020a', 'apiResponses', 'utilities', 'chatCtrl'], function (angular, angularMocks, paw2020a, chatCtrl) {
  describe('Testing chat', function () {
    beforeEach(angular.mock.module('paw2020a'));

    var $controller;
    var chatCtrl;
    var $httpBackend, $routeParams, $rootScope;
    var $scope;
    var apiResp;
    var ProjectService, UserService, MessageService, AuthService, UrlService;
    var expectedInv, expectedProj, expectedMsgs;

    beforeEach(inject(function (_$controller_, _$httpBackend_, _$routeParams_, _$rootScope_, utilities,apiResponses, projectService, userService,urlService, messageService, AuthenticationService, $q) {

      $controller = _$controller_;
      $httpBackend =_$httpBackend_;
      $routeParams = _$routeParams_;
      $rootScope = _$rootScope_;
      ProjectService = projectService;
      MessageService = messageService;
      UserService = userService;
      AuthService = AuthenticationService;
      $scope = {};
      apiResp = apiResponses;
      expectedInv = apiResponses.userInvestor.data;
      expectedProj = apiResponses.project.data;
      expectedMsgs = apiResponses.messages.data;
      $routeParams.id1 = apiResponses.project.data.id;
      $routeParams.id2 = apiResponses.userInvestor.data.id;

      spyOn(AuthService, 'isInvestor').and.callFake(function () {
        return false;
      });

      spyOn(AuthService, 'isEntrepreneur').and.callFake(function () {
        return true;
      });

      utilities.resolvePromise(MessageService, 'setStatus', apiResponses.accepted);
      utilities.resolvePromise(MessageService, 'setSeen', {status:200, data: {}});
      utilities.resolvePromise(UserService, 'getUser', apiResponses.userInvestor);
      utilities.resolvePromise(ProjectService, 'getById', apiResponses.project);
      utilities.resolvePromise(MessageService, 'getChat', apiResponses.messages);

      // spyOn(MessageService, 'setStatus').and.callFake(function () {
      //   return $q.resolve({});
      // })


      var div = document.createElement('select');
      document.getElementById = jasmine.createSpy('chatbox-scroll').and.returnValue(div); //to mock chatbox-scroll div

      chatCtrl = $controller('chatCtrl', {$scope: $scope, AuthenticationService: AuthService, projectService: ProjectService, userService: UserService, messageService: MessageService});

      utilities.ignoreTestAside($httpBackend);

      // $httpBackend.whenPUT(/api.*/).respond(200, '');


      $rootScope.$apply();

    }));

    describe('Testing chat entrepreuner', function () {


      it('chatCtrl should be defined', function () {
        expect(chatCtrl).toBeDefined();
      });


      it('should have checked role', function () {
        expect(AuthService.isInvestor).toHaveBeenCalled();
      });

      it('should have saved investor as user', function () {
        expect(UserService.getUser).toHaveBeenCalled();
        expect($scope.user).toEqual(expectedInv);
      });

      it('should not enable offer', function () {
        $scope.chats[0].accepted = true; //this will set exp in days to 0
        chatCtrl.handleChatResponse($scope.chats);
        expect($scope.offerEnabled).toEqual(false);
      });

      it('should enable response', function () {
        delete $scope.chats[0].accepted;
        chatCtrl.handleChatResponse($scope.chats);
        expect($scope.responseEnabled).toEqual(true);
      });

      it('should calculate new funding current', function () {
        var sum = $scope.project.fundingCurrent + $scope.lastMessage.offer;
        $scope.acceptOffer();
        $rootScope.$digest();
        expect(MessageService.setStatus).toHaveBeenCalled();
        expect($scope.project.fundingCurrent).toEqual(sum);
      });

      it('should calculate new funding current', function () {
        var sum = $scope.project.fundingCurrent + $scope.lastMessage.offer;
        $scope.acceptOffer();
        $rootScope.$digest();
        expect(MessageService.setStatus).toHaveBeenCalled();
        expect($scope.project.fundingCurrent).toEqual(sum);
      });



      it('should calculate new percentage', function () {
        var per = parseInt($scope.project.fundingCurrent * 100 / $scope.project.fundingTarget);
        $scope.acceptOffer();
        $rootScope.$digest();
        expect(MessageService.setStatus).toHaveBeenCalled();
        expect($scope.project.percentage).toEqual(per);
      });



    })
  })

})
