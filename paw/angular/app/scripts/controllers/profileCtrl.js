    'use strict';

    define(['paw2020a', 'services/userService', 'services/sampleService','services/AuthenticationService','services/projectService'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','sampleService','AuthenticationService','projectService','$scope','$routeParams', function(userService,sampleService,AuthenticationService,projectService, $scope, $routeParams) {


      $scope.id = parseInt($routeParams.id);
      if (isNaN($scope.id) || $scope.id <= 0) {
        PathService.get().error().go();
        return;
      }


      $scope.loggedId = AuthenticationService.getUserId()


      userService.getUser($scope.id.toString()).then(function (userApi) {
        $scope.user = {
          'firstName': userApi.data.firstName,
          'lastName': userApi.data.lastName,
          'email': userApi.data.email,
          'phone': userApi.data.phone,
          'birthday': userApi.data.birthDate,
          'locationURL': userApi.data.location,
          'favoritesURL': userApi.data.favorites,
          'linkedin': userApi.data.linkedin,
          'imageExists': userApi.data.imageExists
        };

        sampleService.get($scope.user.locationURL).then(function (response) {
          $scope.user.country = response.data.country;
          $scope.user.city = response.data.city;
          $scope.user.state = response.data.state

        })

        if($scope.user.imageExists === true){
          sampleService.get(userApi.data.image).then(function (response) {
            $scope.user.image = response.data.image
          })
        }
        // sampleService.get($scope.user.favoritesURL).then(function (response) {
        //   console.log(response)
        // })

      })

      // userService.getUserProjects(id).then(function (response) {
      //   console.log(response)
      // })

      if($scope.loggedId == $scope.id) {
        $scope.favs = []
        userService.getFavorites().then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            projectService.getById(response.data[i].projectId.toString()).then(function (response) {
              $scope.favs.push(response.data)
            })
          }

        })
      }

      ///



      // $scope.favs = [
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1
      //   },
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1
      //   },
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1
      //   }
      // ];

      //TODO favs projects

    }]);
});
