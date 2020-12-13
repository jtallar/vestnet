    'use strict';

define(['paw2020a'], function(paw2020a) {

  paw2020a.controller('chatCtrl', function($scope) {

      $scope.sent = true;    // if the mail was sent retreive from url
      $scope.backAction = function() {
        if (this.sent) {
          history.back();
        } else {
          history.back();
          history.back();
        }
      };

      $scope.project = {
          'name': 'Superchero 2.0',
          'cost': 100000,
          'image': 'images/mate.jpg',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'funded': '53%',
          'hits': 6,
          'msgCount': 2
        };
    $scope.chats = [
      {
        'from': 'Mario Bolo',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Sisi, me parece una buena idea.',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'La otra cuestion que queria consultarle es para que fecha tiene estimado conseguir esa lista',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Es super estimativo pero lo ideal seria para Abril del año entrante',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Y me gustaría cerrar con ustedes en los próximos días.',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '220K',
        'exchange': '15%',
        'comment': 'Sí, único socio.',
        'date': '11:42 | 13/12/2020'
      },
    ];

    $scope.n_chats = $scope.chats.length;
    $scope.enabled = true;
    $scope.onOff = false;
    $scope.yesNo = true;
    $scope.disabled = true;

    // var myDiv = document.getElementById("chatbox-scroll");
    // myDiv.scrollTop = myDiv.scrollHeight;

    });
});
