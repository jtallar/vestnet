    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('singleViewCtrl', function($scope) {
      $scope.sent = false;    // if the mail was sent retreive from url
      $scope.userId = 1;      // user id from db

      $scope.backAction = function() {
        if (this.sent) {
          history.back();
        } else {
          history.back();
          history.back();
        }
      };

      $scope.project = {      // project infromation from db
        'name': 'Vestnet',
        'cost': 18000,
        'image': 'images/filter.png',
        'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país y en todo el mundo',
        'id': 1,
        'owner': {'firstName': 'Grupo', 'lastName': '5', 'mail': 'fchoi@itba.edu.ar', 'id': 1},
        'categories': ['Technology', 'Research'],
        'updateDate': '15/02/2019'
      };

    });

});
