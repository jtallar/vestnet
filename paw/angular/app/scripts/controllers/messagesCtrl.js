    'use strict';

define(['paw2020a', 'services/messageService', 'services/urlService', 'services/PathService', 'directives/pagination'],
  function(paw2020a) {

  paw2020a.controller('messagesCtrl', ['messageService', 'urlService', 'PathService', '$scope', '$rootScope', '$routeParams', function(messageService, urlService, PathService, $scope, $rootScope, $routeParams) {
    // Start with updated notification count
    $rootScope.$emit('notificationChange');

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
        $scope.messages[i].notification = (!$scope.messages[i].seen && !$scope.messages[i].direction) || (!$scope.messages[i].seenAnswer && $scope.messages[i].direction && $scope.messages[i].accepted != null);
        $scope.messages[i].projectPortraitExists = false;
        urlService.get($scope.messages[i].projectPortraitImage, $scope.messages[i].id.toString()).then(function (image) {
          $scope.messages[map[image.data.route]].projectImage = image.data.image;
          $scope.messages[map[image.data.route]].projectPortraitExists = true;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            return;
          }
          console.error(errorResponse);
        });
      }
      $scope.loading = false;
    };
    $scope.loading = true;

    this.fetchChatList = function () {
      messageService.getInvestorChatList($scope.page).then(function (response) {
        $scope.loadingPage = false;
        if (response.data.length === 0) {
          $scope.noMessagesFound = true;
          $scope.messages = [];
          return;
        }
        _this.setMaxPage(response.headers().link);
        _this.processMessages(response.data);
      }, function (errorResponse) {
        $scope.loading = false;
        $scope.loadingPage = false;
        console.error(errorResponse);
      });
    };
    this.fetchChatList();

    $scope.loadingPage = false;
    $scope.getToPage = function (page) {
      $scope.loadingPage = true;
      $scope.page = page;
      PathService.get().setParamsInUrl({p:$scope.page});
      _this.fetchChatList();
    };

    $scope.goToChat = function (message) {
      PathService.get().setFullUrl(message.chatUrl).go();
    };

  }]);
});
