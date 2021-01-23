'use strict';

// TODO: Ver de agregar la badge tanto afuera en el icono de mensajes (/notifications/project/{project_id}?) como dentro de un chat.
define(['paw2020a', 'directives/toggle',  'services/projectService', 'services/messageService', 'services/userService', 'services/sampleService', 'services/imageService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('dashboardCtrl', ['projectService', 'messageService','userService','sampleService','imageService', 'PathService', '$scope', '$rootScope', function(projectService, messageService,userService,sampleService,imageService, PathService, $scope, $rootScope) {

    var _this = this;

    $scope.loadingProjects = true; $scope.loadingFunded = true;
    $scope.messages = []; $scope.fundedMsgs = [];

    var map = {};
    userService.getLoggedProjects(false).then(function (response) {
      $scope.projects = response.data;
      $scope.loadingProjects = false;
      for(var i = 0; i < $scope.projects.length; i++) {
        map[$scope.projects[i].id] = i;
        $scope.projects[i].firstFecthed = false;
        $scope.projects[i].firstFecthedOffers = false;
        $scope.projects[i].editUrl= PathService.get().editProject($scope.projects[i].id).path;
        if ($scope.projects[i].portraitExists) {
          sampleService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
            $scope.projects[map[image.data.route]].image = image.data.image;
          }, function (err) {
            console.log("No image");
          });
        }
      }
    });

    userService.getLoggedProjects(true).then(function (response) {
      $scope.fundedProjects = response.data;
      $scope.loadingFunded = false;
      var map = {};
      for(var i = 0; i < $scope.fundedProjects.length; i++) {
        map[$scope.fundedProjects[i].id] = i;
        $scope.fundedProjects[i].editUrl= PathService.get().editProject($scope.fundedProjects[i].id).path;
        if ($scope.fundedProjects[i].portraitExists) {
          sampleService.get($scope.fundedProjects[i].portraitImage, $scope.fundedProjects[i].id.toString()).then(function (image) {
            $scope.fundedProjects[map[image.data.route]].image = image.data.image
          }, function (err) {
            console.log("No image")
          });
        }
      }
    });

    $scope.fetchStats = function(id, index){
      projectService.getStats(id.toString()).then(function (response) {
        $scope.projects[index].clicksAvg = response.data.clicksAvg;
        $scope.projects[index].secondsAvg = response.data.secondsAvg;
        $scope.projects[index].seen = response.data.seen;
        $scope.projects[index].contactClicks = response.data.contactClicks;
        $scope.projects[index].investorsSeen = response.data.investorsSeen;
        $scope.projects[index].lastSeen = response.data.lastSeen;
      });
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
      if($scope.projects[index].firstFecthedOffers === false) {
        $scope.projects[index].firstFecthedOffers = true;
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

    $scope.funded = false;
    $scope.getList = function () {
      if($scope.funded === true) return $scope.fundedProjects;
      else return $scope.projects;
    };

    $scope.isLoading = function () {
      if($scope.funded === true) return $scope.loadingFunded;
      else return $scope.loadingProjects;
    };

    $scope.goToChat = function (message) {
      if (!message.seen) $rootScope.$emit('messageRead');
      PathService.get().setFullUrl(message.chatUrl).go();
    }

  }]);
});
