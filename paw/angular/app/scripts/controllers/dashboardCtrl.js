'use strict';

define(['paw2020a', 'directives/toggle',  'services/projectService', 'services/messageService', 'services/userService', 'services/urlService', 'services/imageService', 'services/PathService', 'directives/pagination'], function(paw2020a) {
  paw2020a.controller('dashboardCtrl', ['projectService', 'messageService','userService','urlService','imageService', 'PathService', '$scope', '$routeParams', '$rootScope', function(projectService, messageService,userService,urlService,imageService, PathService, $scope, $routeParams, $rootScope) {
    // Start with updated notification count
    $rootScope.$emit('notificationChange');

    var _this = this;

    var pageSize = 4;
    var param = parseInt($routeParams.p);
    if (isNaN(param) || param <= 0) param = 1;
    $scope.page = param; $scope.lastPage = param;

    $scope.funded = !!($routeParams.f);

    $scope.projects = [];

    // $scope.toLocaleDateTimeString = function(date) {
    //   var aux;
    //   if(date !== undefined)
    //     aux = new Date(date);
    //   else aux = new Date();
    //   return (aux.toLocaleDateString(navigator.language) + " " + aux.toLocaleTimeString(navigator.language));
    // };

    $scope.daysAgo = function (date) {
      var diffTime = Math.abs(new Date() - new Date(date));
      var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays-1;
    };

    $scope.millisToMinSec = function (millis) {
      if(isNaN(millis)) return 0;
      var minutes = Math.floor(millis / 60000);
      if(minutes===0) minutes = '00';
      var seconds = ((millis % 60000) / 1000).toFixed(0);
      return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    };

    this.setMaxPage = function (linkHeaders) {
      var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
      var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
      if (isNaN(maxPage)) maxPage = page;
      $scope.lastPage = maxPage;
    };

    this.updatePathParams = function () {
      if ($scope.funded) PathService.get().setParamsInUrl({p:$scope.page, f:true});
      else PathService.get().setParamsInUrl({p:$scope.page});
    };

    $scope.loadingPage = false;
    $scope.getToPage = function (page) {
      $scope.page = page;
      _this.updatePathParams();
      _this.getProjects();
    };

    $scope.loadingProjects = true;
    $scope.messages = []; $scope.fundedMsgs = [];

    this.getProjects = function () {
      $scope.projects = [];
      $scope.loadingProjects = true;
      userService.getLoggedProjects($scope.funded, $scope.page, pageSize).then(function (response) {
        _this.setMaxPage(response.headers().link);
        $scope.projects = response.data;
        $scope.loadingProjects = false;
        var map = {};
        for (var i = 0; i < $scope.projects.length; i++) {
          map[$scope.projects[i].id] = i;
          $scope.projects[i].openOffers = false; $scope.projects[i].openMessages = false;
          $scope.projects[i].openStats = false;
          $scope.projects[i].editUrl = PathService.get().editProject($scope.projects[i].id).path;
          $scope.projects[i].portraitExists = false;
          urlService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
            $scope.projects[map[image.data.route]].image = image.data.image;
            $scope.projects[map[image.data.route]].portraitExists = true;
          }, function (errorResponse) {
            if (errorResponse.status === 404) {
              return;
            }
            console.error(errorResponse);
          });
          messageService.projectNotificationCount($scope.projects[i].id).then(function (response) {
            $scope.projects[map[response.data.route]].msgCount = response.data.unread;
          }, function (err) {
            console.error(err);
          });
        }
      });
    };

    this.getProjects();

    $scope.fetchStats = function(id, index){
      $scope.projects[index].openStats = !$scope.projects[index].openStats;
      if($scope.projects[index].openStats === true) {
        projectService.getStats(id.toString()).then(function (response) {
          $scope.projects[index].clicksAvg = response.data.clicksAvg;
          $scope.projects[index].secondsAvg = response.data.secondsAvg;
          $scope.projects[index].seen = response.data.seen;
          $scope.projects[index].contactClicks = response.data.contactClicks;
          $scope.projects[index].investorsSeen = response.data.investorsSeen;
          $scope.projects[index].lastSeen = response.data.lastSeen;
        });
      }
    };

    this.updateNextPageOffers = function (linkHeaders, index) {
      if (!linkHeaders) {
        $scope.projects[index].nextPageOffer = undefined;
        return;
      }
      var nextLink = linkHeaders.split(',').filter(function (el) { return el.includes('next'); });
      if (isNaN(parseInt(nextLink[0].split('p=')[1][0]))) {
        $scope.projects[index].nextPageOffer = undefined;
        return;
      }
      $scope.projects[index].nextPageOffer = nextLink[0].substring(1, nextLink[0].indexOf('>'));
    };

    $scope.fetchOffers = function(id, index){
      $scope.projects[index].openOffers = !$scope.projects[index].openOffers;
      if($scope.projects[index].openOffers === true) {
        $scope.fundedMsgs[index] = [];
        messageService.getOffers(id.toString(), true, 1).then(function (response) {
          _this.updateNextPageOffers(response.headers().link, index);
          $scope.fundedMsgs[index] = response.data;
        }, function (error) {
          console.error(error)
        })
      }
    };

    $scope.viewMoreOffers = function (id, index) {
      if (!$scope.projects[index].nextPageOffer) return;
      urlService.get($scope.projects[index].nextPageOffer).then(function (response) {
        _this.updateNextPageOffers(response.headers().link, index);
        $scope.fundedMsgs[index] = $scope.fundedMsgs[index].concat(response.data);
      }, function (error) {
        console.error(error)
      })
    };

    this.updateNextPageMessages = function (linkHeaders, index) {
      if (!linkHeaders) {
        $scope.projects[index].nextPageMessages = undefined;
        return;
      }
      var nextLink = linkHeaders.split(',').filter(function (el) { return el.includes('next'); });
      if (isNaN(parseInt(nextLink[0].split('p=')[1][0]))) {
        $scope.projects[index].nextPageMessages = undefined;
        return;
      }
      $scope.projects[index].nextPageMessages = nextLink[0].substring(1, nextLink[0].indexOf('>'));
    };

    $scope.fetchMessage = function(id, index){
      $scope.projects[index].openMessages = !$scope.projects[index].openMessages;
      if ($scope.projects[index].openMessages === true) {
        $scope.messages[index] = [];
        messageService.projectNotificationCount(id.toString()).then(function (response) {
          if ($scope.projects[index].msgCount !== response.data.unread) {
            $rootScope.$emit('notificationChange');
          }
          $scope.projects[index].msgCount = response.data.unread;
        }, function (err) {
          console.error(err);
        });
        messageService.getOffers(id.toString(), false, 1).then(function (response) {
          _this.updateNextPageMessages(response.headers().link, index);
          for (var i = 0; i < response.data.length; i++) {
            response.data[i].chatUrl = PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path;
            response.data[i].notification = (!response.data[i].seen && response.data[i].direction) || (!response.data[i].seenAnswer && !response.data[i].direction && response.data[i].accepted != null);
          }
          $scope.messages[index] = response.data;
        }, function (error) {
          console.error(error)
        });
      }
    };

    $scope.viewMore = function (id, index) {
      if (!$scope.projects[index].nextPageMessages) return;
      urlService.get($scope.projects[index].nextPageMessages).then(function (response) {
        _this.updateNextPageMessages(response.headers().link, index);
        for (var i = 0; i < response.data.length; i++) {
          response.data[i].chatUrl = PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path;
          response.data[i].notification = (!response.data[i].seen && response.data[i].direction) || (!response.data[i].seenAnswer && !response.data[i].direction && response.data[i].accepted != null);
        }
        $scope.messages[index] = $scope.messages[index].concat(response.data);
      }, function (error) {
        console.error(error)
      })
    };

    $scope.toggleChange = function () {
      $scope.getToPage(1);
      _this.updatePathParams();
      _this.getProjects();
    };

    $scope.goToChat = function (message) {
      PathService.get().setFullUrl(message.chatUrl).go();
    }

  }]);
});
