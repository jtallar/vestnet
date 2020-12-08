    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('requestsCtrl', function($scope) {

      $scope.messages = [
        {
          'pid': 2,
          'project': {
            'name': 'VestNet'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': 5000,
            'interest': '33% de todo'
          },
          'publishDate': '31/04/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 0
        },
        {
          'pid': 2,
          'project': {
            'name': 'VestNet'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': 5000,
            'interest': '33% de todo'
          },
          'publishDate': '31/04/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 1
        },
        {
          'pid': 2,
          'project': {
            'name': 'VestNet'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': 5000,
            'interest': '33% de todo'
          },
          'publishDate': '31/04/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 2
        }
      ];

    });

});
