'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/userService', 'services/urlService', 'services/messageService','services/PathService', 'directives/customOnChange'], function(paw2020a) {
  paw2020a.controller('userInfoCtrl',['AuthenticationService','userService','urlService','messageService','PathService','$scope','$routeParams', function(AuthenticationService,userService,urlService,messageService,PathService, $scope, $routeParams) {

    var _this = this;

    var pageSize = 6;

    $scope.page = 1; $scope.lastPage = 1;
    $scope.isInvestor = undefined;

    $scope.id = parseInt($routeParams.id);
    if (isNaN($scope.id) || $scope.id <= 0) {
      PathService.get().error().replace();
      return;
    }

    $scope.loadingSecondTab = true;
    $scope.secondTab = [];

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

    this.getCounter = function () {
      messageService.getInvestedAmount($scope.id).then(function (response) {
        _this.animate("invested", 0, response.data.unread, 5000);
      }, function (errorResponse) {
        console.error(errorResponse);
      });
    };

    userService.getUser($scope.id.toString()).then(function (userApi) {
      $scope.user = userApi.data;
      urlService.get($scope.user.location).then(function (response) {
        $scope.user.country = response.data.country;
        $scope.user.city = response.data.city;
        $scope.user.state = response.data.state;
      }, function (errorResponse) {
        if (errorResponse.status === 404) {
          PathService.get().error().replace();
          return;
        }
        console.error(errorResponse);
      });

      $scope.user.profileImageAvailable = false;
      if ($scope.user.imageExists) {
        urlService.get(userApi.data.image).then(function (response) {
          $scope.user.image = response.data.image;
          $scope.user.profileImageAvailable = true;
        }, function (errorResponse) {
          console.error("No img", errorResponse);
        });
      }

      $scope.isInvestor = $scope.user.role === "Investor";
      if ($scope.isInvestor) {
        if (!AuthenticationService.isEntrepreneur()) {
          PathService.get().error().replace({code:403});
          return;
        }
        _this.getCounter();
      }
      _this.fetchSecondTab();
    }, function (errorResponse) {
      if (errorResponse.status === 404) {
        PathService.get().error().replace();
        return;
      }
      console.error(errorResponse);
    });

    this.setMaxPage = function (linkHeaders) {
      var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
      var maxPage = parseInt(lastLink[0].split('p=')[1]);
      if (isNaN(maxPage)) maxPage = $scope.page;
      $scope.lastPage = maxPage;
    };

    this.processResponse = function (data) {
      data.forEach(function (proj){
        proj.percentage = parseInt((proj.fundingCurrent/proj.fundingTarget)*100);
      });
      $scope.secondTab = $scope.secondTab.concat(data);
    };

    this.fetchSecondTab = function () {
      if (!$scope.isInvestor) {
        // Fetch entrepreneur current funding projects
        userService.getUserProjects($scope.user.id.toString(), false, $scope.page, pageSize).then(function (response) {
          _this.setMaxPage(response.headers().link);
          _this.processResponse(response.data);
          $scope.loadingSecondTab = false;
        }, function (errorResponse) {
          $scope.loadingSecondTab = false;
          console.error(errorResponse);
        });
      }
    };

    $scope.viewMoreProjects = function () {
      if ($scope.page >= $scope.lastPage) return;
      $scope.page++;
      _this.fetchSecondTab();
    };
  }]);
});
