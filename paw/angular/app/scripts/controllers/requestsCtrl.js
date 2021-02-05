    'use strict';

define(['paw2020a', 'services/messageService', 'services/projectService', 'services/urlService', 'services/PathService', 'directives/pagination'], function(paw2020a) {
    paw2020a.controller('requestsCtrl', ['messageService','projectService', 'urlService', 'PathService', '$scope', '$routeParams', function(messageService, projectService, urlService, PathService, $scope, $routeParams) {

      var _this = this;
      $scope.noDealsFound = false;

      var param = parseInt($routeParams.p);
      if (isNaN(param) || param <= 0) param = 1;
      $scope.page = param; $scope.lastPage = param;

      $scope.getDate = function(date){
        if(date !== undefined)
          return date.toString().match(/.+?(?=T)/);

        var today = new Date();
        return (today.getFullYear() + '-' + (today.getMonth()+1) + '-' + today.getDate());
      };

      // Cannot use scope, too many changes to digest
      this.animate = function (id, start, end, duration) {
        if (start === end) return;
        var range = end - start;
        var current = start;
        var increment = end/500;
        var stepTime = Math.abs(Math.floor(duration / range));
        var obj = document.getElementById(id);
        var timer = setInterval(function() {
          current += increment;
          obj.innerHTML = parseInt(current);
          if (current >= end) {
            clearInterval(timer);
            obj.innerHTML = end;
          }
        }, stepTime);
      };

      $scope.messages = [];

      this.updateCounter = function () {
        messageService.getInvestedAmount().then(function (response) {
          _this.animate("invested", 0, response.data.unread, 5000);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };
      this.updateCounter();

      this.setMaxPage = function (linkHeaders) {
        var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
        var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
        if (isNaN(maxPage)) maxPage = page;
        $scope.lastPage = maxPage;
      };

      this.processMessages = function (messages) {
        $scope.messages = messages;
        var map = {};
        for(var i = 0; i < $scope.messages.length; i++) {
          map[$scope.messages[i].id] = i;
          $scope.messages[i].ownerUrl = PathService.get().user($scope.messages[i].ownerId).path;
          $scope.messages[i].projectUrl = PathService.get().singleProject($scope.messages[i].projectId).path;
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
      };

      this.fetchDeals = function () {
        messageService.getInvestorDeals($scope.page).then(function (response) {
          $scope.loadingPage = false;
          if (response.data.length === 0) {
            $scope.noDealsFound = true;
            $scope.messages = [];
            return;
          }
          _this.setMaxPage(response.headers().link);
          _this.processMessages(response.data);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };
      this.fetchDeals();

      $scope.loadingPage = false;
      $scope.getToPage = function (page) {
        $scope.loadingPage = true;
        $scope.page = page;
        PathService.get().setParamsInUrl({p:$scope.page});
        _this.fetchDeals();
      };

    }]);

});
