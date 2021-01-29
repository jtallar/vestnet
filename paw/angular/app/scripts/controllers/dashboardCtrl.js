'use strict';

// TODO: Ver de agregar la badge tanto afuera en el icono de mensajes (/notifications/project/{project_id}?) como dentro de un chat.
define(['paw2020a', 'directives/toggle',  'services/projectService', 'services/messageService', 'services/userService', 'services/sampleService', 'services/imageService', 'services/PathService', 'directives/pagination'], function(paw2020a) {
  paw2020a.controller('dashboardCtrl', ['projectService', 'messageService','userService','sampleService','imageService', 'PathService', '$scope', '$routeParams', '$rootScope', function(projectService, messageService,userService,sampleService,imageService, PathService, $scope, $routeParams, $rootScope) {

    var _this = this;

    var pageSize = 4;
    var param = parseInt($routeParams.p);
    if (isNaN(param) || param <= 0) param = 1;
    $scope.page = param; $scope.lastPage = param;

    $scope.funded = !!($routeParams.f);

    $scope.projects = null; $scope.fundedProjects = null;

    $scope.getDate = function(date){
      if(date !== undefined)
        return date.toString().match(/.+?(?=T)/);

      var today = new Date();
      return (today.getFullYear() + '-' + (today.getMonth()+1) + '-' + today.getDate());
    };
    $scope.getHour = function(date){
      if(date !== undefined)
        return date.toString().match(/(?<=T).*?(?=\.|-)/);

      var today = new Date();
      return (today.getHours() + ':' + today.getMinutes());
    };

    $scope.millisToMinSec = function (millis) {
      if(isNaN(millis)) return 0;
      var minutes = Math.floor(millis / 60000);
      if(minutes===0) minutes = '00';
      var seconds = ((millis % 60000) / 1000).toFixed(0);
      return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    }

    this.setMaxPage = function (linkHeaders, funded) {
      var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
      var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
      if (isNaN(maxPage)) maxPage = page;
      $scope.lastPage = maxPage;
      if($scope.funded) $scope.fundedLast = maxPage;
      else $scope.notFundedLast = maxPage;
    };

    this.updatePathParams = function () {
      if ($scope.funded) PathService.get().setParamsInUrl({p:$scope.page, f:true});
      else PathService.get().setParamsInUrl({p:$scope.page});
    };

    $scope.getToPage = function (page) {
      $scope.page = page;
      _this.updatePathParams();
      _this.fetchProjects(true);
    };

    $scope.loadingProjects = true; $scope.loadingFunded = true;
    $scope.messages = []; $scope.fundedMsgs = [];

    this.getNotFundedProjects = function (nextPage) {
      if($scope.projects === null || nextPage) {
        userService.getLoggedProjects(false, $scope.page, pageSize).then(function (response) {
          _this.setMaxPage(response.headers().link, false);
          $scope.projects = response.data;
          $scope.loadingProjects = false;
          var map = {};
          for (var i = 0; i < $scope.projects.length; i++) {
            map[$scope.projects[i].id] = i;
            $scope.projects[i].firstFecthed = false;
            $scope.projects[i].firstFecthedOffers = false;
            $scope.projects[i].editUrl = PathService.get().editProject($scope.projects[i].id).path;
            if ($scope.projects[i].portraitExists) {
              sampleService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
                $scope.projects[map[image.data.route]].image = image.data.image;
              }, function (err) {
                console.log("No image");
              });
            }
            messageService.projectNotificationCount($scope.projects[i].id).then(function (response) {
              $scope.projects[map[response.data.route]].msgCount = response.data.unread;
            }, function (err) {
              console.error(err);
            });
          }
        });
      }
    };

    this.getFundedProjects = function (nextPage) {
      if($scope.fundedProjects === null || nextPage) {
        userService.getLoggedProjects(true, $scope.page, pageSize).then(function (response) {
          _this.setMaxPage(response.headers().link);
          $scope.fundedProjects = response.data;
          $scope.loadingFunded = false;
          var map = {};
          for (var i = 0; i < $scope.fundedProjects.length; i++) {
            map[$scope.fundedProjects[i].id] = i;
            $scope.fundedProjects[i].editUrl = PathService.get().editProject($scope.fundedProjects[i].id).path;
            if ($scope.fundedProjects[i].portraitExists) {
              sampleService.get($scope.fundedProjects[i].portraitImage, $scope.fundedProjects[i].id.toString()).then(function (image) {
                $scope.fundedProjects[map[image.data.route]].image = image.data.image
              }, function (err) {
                console.log("No image")
              });
            }
          }
        });
      }
    };

    this.fetchProjects = function (nextPage) {
      if ($scope.funded === true) _this.getFundedProjects(nextPage);
      else _this.getNotFundedProjects(nextPage);
    };

    this.fetchProjects(false);

    $scope.fetchStats = function(id, index){
      if($scope.projects[index].openStats === undefined) $scope.projects[index].openStats = false;
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
      if($scope.projects[index].openOffers === undefined) $scope.projects[index].openOffers = false;
      $scope.projects[index].openOffers = !$scope.projects[index].openOffers;
      if($scope.projects[index].openOffers === true) {
        console.log("loading offers from ", index)
        $scope.fundedMsgs[index] = [];
        messageService.getOffers(id.toString(), true, 1).then(function (response) {
          _this.updateNextPageOffers(response.headers().link, index);
          $scope.fundedMsgs[index] = response.data;
        }, function (error) {
          console.error(error)
        })
      }
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
      if($scope.projects[index].firstFecthed === false) {
        $scope.projects[index].firstFecthed = true;
        $scope.messages[index] = [];
        messageService.getOffers(id.toString(), false, 1).then(function (response) {
          _this.updateNextPageMessages(response.headers().link, index);
          $scope.messages[index] = response.data;
          for (var i = 0; i < response.data.length; i++) {
            $scope.messages[index][i].chatUrl = PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path;
            // TODO: Chequear esta condicion de notification
            $scope.messages[index][i].notification = (!$scope.messages[index][i].seen && $scope.messages[index][i].direction) || (!$scope.messages[index][i].seenAnswer && !$scope.messages[index][i].direction && $scope.messages[index][i].accepted != null);
          }
        }, function (error) {
          console.error(error)
        });
      }
    };

    $scope.viewMore = function (id, index) {
      sampleService.get($scope.projects[index].nextPageMessages).then(function (response) {
        _this.updateNextPageMessages(response.headers().link, index);
        $scope.messages[index] = $scope.messages[index].concat(response.data);
        for (var i = 0; i < response.data.length; i++) {
          $scope.messages[index][i].chatUrl = PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path;
          // TODO: Chequear esta condicion de notification
          $scope.messages[index][i].notification = (!$scope.messages[index][i].seen && $scope.messages[index][i].direction) || (!$scope.messages[index][i].seenAnswer && !$scope.messages[index][i].direction && $scope.messages[index][i].accepted != null);
        }
      }, function (error) {
        console.error(error)
      })
    };

    $scope.viewMoreOffers = function (id, index) {
      sampleService.get($scope.projects[index].nextPageOffer).then(function (response) {
        _this.updateNextPageOffers(response.headers().link, index);
        $scope.fundedMsgs[index] = $scope.fundedMsgs[index].concat(response.data);
      }, function (error) {
        console.error(error)
      })
    };

    $scope.toggleChange = function () {
      $scope.getToPage(1);
      _this.updatePathParams();
      if ($scope.funded) $scope.lastPage = $scope.fundedLast;
      else $scope.lastPage = $scope.notFundedLast;
    };

    $scope.getList = function () {
      if($scope.funded === true) return $scope.fundedProjects;
      else return $scope.projects;
    };

    $scope.isLoading = function () {
      if($scope.funded === true) return $scope.loadingFunded;
      else return $scope.loadingProjects;
    };

    $scope.goToChat = function (message) {
      if (message.notification) $rootScope.$emit('messageRead');
      PathService.get().setFullUrl(message.chatUrl).go();
    }

  }]);
});
