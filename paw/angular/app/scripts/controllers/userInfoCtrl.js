    'use strict';

    define(['paw2020a', 'services/userService', 'services/sampleService','services/AuthenticationService','services/projectService'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','sampleService','AuthenticationService','projectService','$scope','$routeParams', function(userService,sampleService,AuthenticationService,projectService, $scope, $routeParams) {

      $scope.id = parseInt($routeParams.id);
      if (isNaN($scope.id) || $scope.id <= 0) {
        PathService.get().error().go();
        return;
      }

      $scope.loggedId = AuthenticationService.getUserId();
      console.log($scope.id, $scope.loggedId);

      userService.getUser($scope.id.toString()).then(function (userApi) {
        $scope.user = userApi.data;
        console.log($scope.user);

        sampleService.get($scope.user.location).then(function (response) {
          $scope.user.country = response.data.country;
          $scope.user.city = response.data.city;
          $scope.user.state = response.data.state;
        });

        if ($scope.user.imageExists) {
          sampleService.get(userApi.data.image).then(function (response) {
            $scope.user.image = response.data.image;
          });
        }
        // sampleService.get($scope.user.favorites).then(function (response) {
        //   console.log(response)
        // })

      });

      if($scope.loggedId === $scope.id) {
        $scope.favs = [];
        userService.getProfileFavorites().then(function (response) {
          console.log(response.data);
          for (var i = 0; i < response.data.length; i++) {
            projectService.getById(response.data[i].projectId.toString()).then(function (response) {
              $scope.favs.push(response.data);
            });
          }
        })
      }






    }]);
});
