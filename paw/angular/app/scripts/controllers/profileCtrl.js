    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('profileCtrl', function($scope) {
      $scope.user = {
        'firstName': 'Pablo',
        'lastName': 'Perez',
        'email': 'pabloperez@gmail.com',
        'phone': '+54 11 62878065',
        'birthday': '03/09/1997',
        'country': 'Austria',
        'city': 'Una ciudad de Austria',
        'state': 'Un estado de esa ciudad de Austria'
      };
      $scope.projects = [
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1
        },
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1
        },
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1
        }
      ];
    });

});
