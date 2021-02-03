'use strict';

define(['paw2020a', 'services/userService', 'services/sampleService', 'services/messageService','services/PathService', 'directives/customOnChange'], function(paw2020a) {
  paw2020a.controller('userInfoCtrl',['userService','sampleService','messageService','PathService','$scope','$routeParams', function(userService,sampleService,messageService,PathService, $scope, $routeParams) {

    var _this = this;

    var pageSize = 6;

    $scope.page = 1; $scope.lastPage = 1;
    $scope.isInvestor = undefined;

    $scope.id = parseInt($routeParams.id);
    if (isNaN($scope.id) || $scope.id <= 0) {
      PathService.get().error().replace();
      return;
    }

    // $scope.formatPrice = function(number) {
    //   var formatter = new Intl.NumberFormat(navigator.language, { style: 'currency', currency: 'USD', minimumFractionDigits: 0, });
    //   return formatter.format(number);
    // }
    //
    // $scope.toLocaleDateString = function(date) {
    //   var aux;
    //   if(date !== undefined)
    //     aux = new Date(date);
    //   else aux = new Date();
    //   return (aux.toLocaleDateString(navigator.language));
    // };

    $scope.loadingSecondTab = true;
    $scope.secondTab = [];

    userService.getUser($scope.id.toString()).then(function (userApi) {
      $scope.user = userApi.data;
      sampleService.get($scope.user.location).then(function (response) {
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

      if ($scope.user.imageExists) {
        sampleService.get(userApi.data.image).then(function (response) {
          $scope.user.image = response.data.image;
        }, function (errorResponse) {
          console.error("No img", errorResponse);
        });
      }

      $scope.isInvestor = $scope.user.role === "Investor";
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
      var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
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
      if ($scope.isInvestor) {
        // Fetch investor deals
        // TODO: Que hacemos aca? No tengo toda la data que tiene el otro, que muestro?
        messageService.getInvestorDeals($scope.page, $scope.user.id).then(function (response) {
          _this.setMaxPage(response.headers().link);
          _this.processResponse(response.data);
          $scope.loadingSecondTab = false;
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      } else {
        // Fetch entrepreneur current funding projects
        userService.getUserProjects($scope.user.id.toString(), false, $scope.page, pageSize).then(function (response) {
          _this.setMaxPage(response.headers().link);
          _this.processResponse(response.data);
          $scope.loadingSecondTab = false;
        }, function (errorResponse) {
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
