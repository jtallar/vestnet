    'use strict';

    define(['paw2020a', 'services/userService', 'services/sampleService', 'services/imageService','services/AuthenticationService','services/PathService', 'directives/customOnChange'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','sampleService','imageService','AuthenticationService','PathService','$scope','$routeParams', function(userService,sampleService,imageService,AuthenticationService,PathService, $scope, $routeParams) {

      var maxImageSize = 2097152;

      $scope.isInvestor = AuthenticationService.isInvestor();
      $scope.loadingFavs = true; $scope.uploadingImage = false;
      $scope.imageSizeError = false;
      $scope.favs = [];

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
          $scope.favs = response.data;
          console.log($scope.favs);
          $scope.loadingFavs = false;
          // Paginar a mano los favs
        }, function (errorResponse) {
          // 404 should never happen
          console.error(errorResponse);
        })
      }

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
