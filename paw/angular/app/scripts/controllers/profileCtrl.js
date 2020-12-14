    'use strict';

    define(['paw2020a', 'services/userService'], function(paw2020a) {
    paw2020a.controller('profileCtrl',['userService', function(userService) {

      var _this = this

      userService.getUser('8').then(function (userApi) {
        _this.user = {
          'firstName': userApi.firstName,
          'lastName': userApi.lastName,
          'email': userApi.email,
          'phone': userApi.phone,
          'birthday': userApi.birthDate,
          'country': 'Austria', // TODO
          'city': 'Una ciudad de Austria',
          'state': 'Un estado de esa ciudad de Austria'
        };


      })

    //TODO projects

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
