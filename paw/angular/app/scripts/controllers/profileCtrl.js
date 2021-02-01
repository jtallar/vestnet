    'use strict';

    define(['paw2020a', 'services/userService', 'services/sampleService', 'services/imageService','services/AuthenticationService','services/PathService', 'directives/customOnChange'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','sampleService','imageService','AuthenticationService','PathService','$scope','$routeParams', function(userService,sampleService,imageService,AuthenticationService,PathService, $scope, $routeParams) {

      var maxImageSize = 2097152, pageSize = 6;

      $scope.isInvestor = AuthenticationService.isInvestor();
      $scope.loadingFavs = true; $scope.uploadingImage = false;
      $scope.imageSizeError = false;
      $scope.allFavs = []; $scope.showFavs = [];

      // $scope.formatPrice = function(number) {
      //   var formatter = new Intl.NumberFormat(navigator.language, { style: 'currency', currency: 'USD', minimumFractionDigits: 0, });
      //   return formatter.format(number);
      // }

      // $scope.toLocaleDateString = function(date) {
      //   var aux;
      //   if(date !== undefined)
      //     aux = new Date(date);
      //   else aux = new Date();
      //   return (aux.toLocaleDateString(navigator.language));
      // };

      userService.getLoggedUser().then(function (userApi) {
        $scope.user = userApi.data;
        sampleService.get($scope.user.location).then(function (response) {
          $scope.user.country = response.data.country;
          $scope.user.city = response.data.city;
          $scope.user.state = response.data.state;
        }, function (errorResponse) {
          // 404 should never happen
          console.error(errorResponse);
        });

        if ($scope.user.imageExists) {
          sampleService.get(userApi.data.image).then(function (response) {
            $scope.user.image = response.data.image;
          }, function (errorResponse) {
            console.error("No img", errorResponse);
          });
        }
      });

      if($scope.isInvestor) {
        userService.getProfileFavorites().then(function (response) {
          $scope.allFavs = response.data;
          $scope.allFavs.forEach(function (fav){
            fav.percentage = parseInt((fav.fundingCurrent/fav.fundingTarget)*100);
          });
          $scope.loadingFavs = false;
          $scope.viewMoreFavs();
          // Paginar a mano los favs
        }, function (errorResponse) {
          // 404 should never happen
          console.error(errorResponse);
        })
      }

      $scope.viewMoreFavs = function () {
        if ($scope.allFavs.length === $scope.showFavs.length) return;
        var currLength = $scope.showFavs.length;
        $scope.showFavs = $scope.showFavs.concat($scope.allFavs.slice(currLength, currLength + pageSize));
      };

      $scope.fileboxChange = function (event) {
        if (event.target.files.length === 0) return;
        if (event.target.files[0].size >= maxImageSize) {
          event.target.files = null;
          $scope.$apply(function () {
            $scope.imageSizeError = true;
          });
        } else {
          var r = new FileReader();
          r.onloadend = function (ev) {
            imageService.setProfileImage(ev.target.result).then(function (imageResponse) {
              $scope.uploadingImage = false;
              $scope.user.image = imageService.blobToBase64(ev.target.result);
              $scope.user.imageExists = true;
            }, function (imageErrorResponse) {
              // 404 should never happen
              console.error(imageErrorResponse);
            });
          };
          r.readAsArrayBuffer(event.target.files[0]);
          $scope.$apply(function () {
            $scope.imageSizeError = false;
            $scope.uploadingImage = true;
          });
        }
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
