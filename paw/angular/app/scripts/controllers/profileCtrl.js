    'use strict';

    define(['paw2020a', 'services/userService', 'services/urlService', 'services/imageService','services/AuthenticationService','services/PathService', 'directives/customOnChange'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','urlService','imageService','AuthenticationService','PathService','$scope','$routeParams', function(userService,urlService,imageService,AuthenticationService,PathService, $scope, $routeParams) {

      $scope.maxImageSizeMB = 2;
      var maxImageSize = 2097152, pageSize = 6; // 2 * 1024 * 1024

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
        urlService.get($scope.user.location).then(function (response) {
          $scope.user.country = response.data.country;
          $scope.user.city = response.data.city;
          $scope.user.state = response.data.state;
        }, function (errorResponse) {
          // 404 should never happen
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
      });

      if($scope.isInvestor) {
        userService.getProfileFavorites().then(function (response) {
          $scope.allFavs = response.data;
          $scope.allFavs.forEach(function (fav){
            fav.percentage = parseInt((fav.fundingCurrent/fav.fundingTarget)*100);
          });
          $scope.loadingFavs = false;
          $scope.viewMoreFavs();
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

      $scope.removeFav = function (id) {
        if (!$scope.isInvestor) return;
        userService.putFavorite(id, false).then(function () {
          $scope.allFavs = $scope.allFavs.filter(function (el) { return el.id !== id });
          $scope.showFavs = $scope.showFavs.filter(function (el) { return el.id !== id });
        },function (error) {
          console.error(error);
        });
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

    }]);
});
