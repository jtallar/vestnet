    'use strict';

define(['paw2020a', 'services/messageService', 'services/sampleService', 'services/PathService', 'directives/pagination'],
  function(paw2020a) {

  paw2020a.controller('messagesCtrl', ['messageService', 'sampleService', 'PathService', '$scope', '$rootScope', '$routeParams', function(messageService, sampleService, PathService, $scope, $rootScope, $routeParams) {

    var _this = this;
    $scope.noMessagesFound = false;

    var param = parseInt($routeParams.p);
    if (isNaN(param) || param <= 0) param = 1;
    $scope.page = param; $scope.lastPage = param;

    this.setMaxPage = function (linkHeaders) {
      var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
      var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
      if (isNaN(maxPage)) maxPage = page;
      $scope.lastPage = maxPage;
    };

    $scope.messages = [];
    this.processMessages = function (messages) {
      $scope.messages = messages;
      var map = {};
      for(var i = 0; i < $scope.messages.length; i++) {
        map[$scope.messages[i].id] = i;
        $scope.messages[i].ownerUrl = PathService.get().user($scope.messages[i].ownerId).path;
        $scope.messages[i].projectUrl = PathService.get().singleProject($scope.messages[i].projectId).path;
        $scope.messages[i].chatUrl = PathService.get().chat($scope.messages[i].projectId).path;

        // TODO: Ver si con un cambio en el back puedo ahorrarme esta mamushka de llamadas
        sampleService.get($scope.messages[i].project, $scope.messages[i].id.toString()).then(function (project) {
          $scope.messages[map[project.data.route]].projectName = project.data.name;
        }, function (err) {
          console.log("No project found");
        });

        sampleService.get($scope.messages[i].owner, $scope.messages[i].id.toString()).then(function (user) {
          $scope.messages[map[user.data.route]].ownerFirstName = user.data.firstName;
          $scope.messages[map[user.data.route]].ownerLastName = user.data.lastName;
          $scope.messages[map[user.data.route]].imageExists = user.data.imageExists;
          if(user.data.imageExists){
            sampleService.get(user.data.image, user.data.route).then(function (image) {
              $scope.messages[map[image.data.route]].ownerImage = image.data.image;
            }, function (err) {
              console.log("No image");
            });
          }
        }, function (err) {
          console.log("No user found");
        });
      }
    };

    this.fetchChatList = function () {
      messageService.getInvestorChatList($scope.page).then(function (response) {
        if (response.data.length === 0) {
          $scope.noMessagesFound = true;
          $scope.messages = [];
          return;
        }
        _this.setMaxPage(response.headers().link);
        _this.processMessages(response.data);
      }, function (errorResponse) {
        console.error(errorResponse);
      });
    };
    this.fetchChatList();

    $scope.getToPage = function (page) {
      $scope.page = page;
      PathService.get().setParamsInUrl({p:$scope.page});
      _this.fetchChatList();
    };

    $scope.goToChat = function (message) {
      if (!message.seen) $rootScope.$emit('messageRead');
      PathService.get().setFullUrl(message.chatUrl).go();
    };

  }]);
});
