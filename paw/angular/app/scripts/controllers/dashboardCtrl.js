    'use strict';

define(['paw2020a', 'directives/toggle'], function(paw2020a) {

  paw2020a.controller('dashboardCtrl', function($scope) {

      $scope.projects = [
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'funded': false,
          'hits': 6,
          'msgCount': 2
        },
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 2,
          'funded': false,
          'hits': 6,
          'msgCount': 2
        },
        {
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 3,
          'funded': false,
          'hits': 3,
          'msgCount': 0
        }
      ];

    $scope.fundedProjects = [
      {
        'name': 'Mateico',
        'cost': 100000,
        'image': 'images/mate.jpg',
        'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
        'id': 1,
        'funded': true,
        'hits': 6,
        'msgCount': 2
      }
    ];

      var size = 3;   // it does not get me length of projects we have to fetch it from services later

      $scope.state = new Array($scope.projects.length).fill(0);

      $scope.messages = [[{'investor': 'Gabriel','projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1}, {'investor': 'Mario','projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}],
        [{'investor': 'Gloria','projectId': 2,'body': 'como va', 'offer': 80000, 'request': 'el auto', 'senderId': 10}, {'investor': 'Miriam','projectId': 2,'body': 'feqefewq', 'offer': 500, 'request': 'la moto', 'senderId': 3}], []];

      $scope.answer = function (projectId, senderId, accepted) {
          console.log(projectId);
          console.log(senderId);
      };

      $scope.enabled = true;
      $scope.onOff = false;
      $scope.funded = true;
      $scope.disabled = true;

      $scope.extraMessages = [{'investor': 'Kiko','projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1}, {'investor': 'Lolo','projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}];

      $scope.viewMore = function (id) {
        console.log($scope.messages[id]);
        $scope.extraMessages.forEach(function(msg){
          $scope.messages[id].push(msg)
        });
      }

      $scope.getList = function () {
        if($scope.funded === true) return $scope.fundedProjects;
        else return $scope.projects;
      }

    });
});
