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
          'name': 'Superchero',
          'target': 1000,
          'current': 900,
          'image': 'images/mate.jpg',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'percentage': 90,
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
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'La otra cuestion que queria consultarle es para que fecha tiene estimado conseguir esa lista',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Es super estimativo pero lo ideal seria para Abril del año entrante',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Y me gustaría cerrar con ustedes en los próximos días.',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '200K',
        'exchange': '20%',
        'comment': 'Entiendo, intentaré todo de mí... ¿Eres único socio en todo esto, no?',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '220K',
        'exchange': '15%',
        'comment': 'Sí, único socio.',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
    ];

    $scope.extraChats = [
      {
        'from': 'Mario',
        'to': 'Me',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '350K',
        'exchange': '55%',
        'comment': 'Mensajitos nuevoooos',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      },
      {
        'from': 'Me',
        'to': 'Mario',
        'picture': 'images/jmv-avatar.jpg',
        'offer': '88K',
        'exchange': '50%',
        'comment': 'jajajjajajajja seee buenisimo buenisimo buenisimo. bue ni si mo.',
        'date': '11:42 | 13/12/2020',
        'exp': '4'
      }];

    $scope.n_chats = $scope.chats.length;
    $scope.enabled = true;
    $scope.onOff = false;
    $scope.yesNo = true;
    $scope.disabled = true;

    $scope.viewMoreChat = function () {
      var element = document.getElementById("chatbox-scroll");
      element.scrollTop = 0;
      $scope.extraChats.reverse();
      // $scope.extraChats.forEach(msg => $scope.chats.unshift(msg));
      $scope.extraChats.forEach(function (msg){
        $scope.chats.unshift(msg);
      });
    }

    // TODO: ver si se puede hacer que ande asi arranca al fondo del chat
    $scope.init = function () {
      var element = document.getElementById("chatbox-scroll");
      element.scrollTop = element.scrollHeight;
    }

    // var myDiv = document.getElementById("chatbox-scroll");
    // myDiv.scrollTop = myDiv.scrollHeight;

    });
});
