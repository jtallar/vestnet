    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('singleViewCtrl', function($scope) {

      $scope.owner = true;
      $scope.sent = true;    // if the mail was sent retreive from url
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
        'name': 'Superchero',
        'target': 1000,
        'current': 900,
        'image': 'images/filter.png',
        'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país y en todo el mundo',
        'id': 1,
        'owner': {'firstName': 'Grupo', 'lastName': '5', 'mail': 'fchoi@itba.edu.ar', 'id': 1},
        'categories': ['Technology', 'Research'],
        'updateDate': '15/02/2019',
        'percentage': 90,
        'stage' : 3,
        'stages': [
          {'name': 'Stage 1', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.'},
          {'name': 'Stage 2', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.'},
          {'name': 'Stage 3', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.'},
          {'name': '', 'comment': ''},
          {'name': '', 'comment': ''}
          ]
      };

      $scope.new = {'name':'', 'comment':''};

      $scope.addStage = function (name, comment) {
        if(name==='') name='Stage '+ ($scope.project.stage+1);
        $scope.project.stages[$scope.project.stage].name = name;
        $scope.project.stages[$scope.project.stage].comment = comment;
        $scope.project.stage++;
        $scope.new.name='';
        $scope.new.comment='';
      }

      $scope.deleteStage = function (stage){
        $scope.project.stages[stage-1].name = '';
        $scope.project.stages[stage-1].comment = '';
        $scope.project.stage--;
      }

    });

});
