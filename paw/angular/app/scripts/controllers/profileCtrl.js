    'use strict';

    define(['paw2020a', 'services/userService', 'services/sampleService'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService','sampleService','$scope', function(userService,sampleService, $scope) {

      var id = '16'

      userService.getUser(id).then(function (userApi) {
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


      //
      // userService.getUserProjects(id).then(function (response) {
      //   console.log(response)
      // })

    //TODO favs projects

      //
      // $scope.projects = [
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
    }]);

});
