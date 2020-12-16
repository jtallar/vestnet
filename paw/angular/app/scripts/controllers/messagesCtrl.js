    'use strict';

define(['paw2020a'], function(paw2020a) {

  paw2020a.controller('messagesCtrl', function($scope) {

      $scope.messages = [
        {
          'user': 'Julián Vuoso',
          'project': 'Superchero',
          'image': 'images/jmv-avatar.jpg',
          'last': 'Me interesa preguntarte lo siguiente, mensaje largo se viene asi que agarrate',
          'unread' : 2,
          'offer' : '',
          'exch' : '',
          'pid': 1,
          'uid': 1,
          'date': '10:32 | 15/12/2020'
        },
        {
          'user': 'Wonder Woman',
          'project': 'Cerberus',
          'image': 'images/wonder-icon.png',
          'last': 'A mi me interesa preguntarte lo siguiente',
          'unread' : 0,
          'offer' : '',
          'exch' : '',
          'pid': 1,
          'uid':1,
          'date': '10:32 | 15/12/2020'
        },
        {
          'user': 'Mr Vestnet',
          'project': 'Vestnet',
          'image': 'images/projectNoImage.png',
          'last': 'Y a mi me interesa preguntarte lo siguiente',
          'unread' : 1,
          'offer' : '',
          'exch' : '',
          'pid': 1,
          'uid':1,
          'date': '10:32 | 15/12/2020'
        }
      ];

      $scope.enabled = true;
      $scope.onOff = false;
      $scope.yesNo = true;
      $scope.disabled = true;

    });
});
