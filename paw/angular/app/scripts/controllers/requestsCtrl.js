    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('requestsCtrl', function($scope) {

      $scope.pages = 1;

      $scope.animate = function (id, start, end, duration) {
        console.log('animate llamado');
        if (start === end) return;
        var range = end - start;
        var current = start;
        var increment = end/500;
        var stepTime = Math.abs(Math.floor(duration / range));
        var obj = document.getElementById(id);
        var timer = setInterval(function() {
          current += increment;
          obj.innerHTML = current;
          if (current === end) {
            clearInterval(timer);
          }
        }, stepTime);
      }

      $scope.$on('$viewContentLoaded', function() {
        $scope.animate("invested", 0, 2500, 5000);
      });

      $scope.messages = [
        {
          'pid': 2,
          'project': {
            'name': 'Mate Electrico'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': '220K',
            'interest': '33% de todo'
          },
          'publishDate': '22/08/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 0
        },
        {
          'pid': 2,
          'project': {
            'name': 'Superchero'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': '3000K',
            'interest': '10% de la empresa'
          },
          'publishDate': '03/09/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 0
        },
        {
          'pid': 2,
          'project': {
            'name': 'Cerberus'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': '500K',
            'interest': '100K mensuales'
          },
          'publishDate': '31/04/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 0
        },
        {
          'pid': 2,
          'project': {
            'name': 'BED'
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
            'name': 'Otro'
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
        }
      ];

    });

});
