    'use strict';

define(['paw2020a', 'services/messageService', 'services/userService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('requestsCtrl', ['messageService', 'userService', 'PathService', '$scope', function(messageService, userService, PathService, $scope) {

      $scope.page = 1; $scope.lastPage = 1;

      $scope.animate = function (id, start, end, duration) {
        if (start === end) return;
        var range = end - start;
        var current = start;
        var increment = end/500;
        var stepTime = Math.abs(Math.floor(duration / range));
        var obj = document.getElementById(id);
        var timer = setInterval(function() {
          current += increment;
          obj.innerHTML = parseInt(current);
          if (current >= end) {
            clearInterval(timer);
            obj.innerHTML = end;
          }
        }, stepTime);
      };

      $scope.$on('$viewContentLoaded', function() {
        var total = 0;
        $scope.messages.forEach(function (msg){
          // console.log(msg.project.offer)
          total += msg.content.offer;
        });
        console.log(total)
        $scope.animate("invested", 0, total, 5000);
      });



      $scope.messages = [
        {
          'pid': 2,
          'project': {
            'name': 'Mate Electrico'
          },
          'content': {
            'message': 'Mensaje de prueba',
            'offer': 220,
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
            'offer': 3000,
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
            'offer': 500,
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
            'offer': 8,
            'interest': '33% de todo'
          },
          'publishDate': '31/04/2020',
          'senderId': 1,
          'receiverId': 1,
          'state': 0
        }
      ];

      messageService.getInvestorDeals($scope.page, true).then(function (response) {
        // $scope.messages = response.data;
        console.log(response.data);
      }, function (errorResponse) {
        console.error(errorResponse);
      })

    }]);

});
