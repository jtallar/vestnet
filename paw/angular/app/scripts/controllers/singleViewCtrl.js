 'use strict';

define(['paw2020a','services/projectService', 'services/sampleService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('singleViewCtrl',['projectService','sampleService', 'PathService', '$scope', '$routeParams', function(projectService,sampleService, PathService, $scope, $routeParams) {

    /*** STATS ***/
    $scope.$on('$viewContentLoaded', function() {
        $scope.start = new Date();
        $scope.clicks = 0;
    });
    $scope.timeHere = function(){
      return new Date().getTime() - $scope.start.getTime();
    };
    $scope.clicksHere = function (){
      return $scope.clicks;
    };
    document.getElementById('all').addEventListener('click', function(event) {
      $scope.clicks++;
    }, false);
    $scope.pressContact = 0;

    $scope.$on("$destroy", function(){
      projectService.addStat($scope.project.id, $scope.timeHere(), $scope.clicksHere(), $scope.pressContact, new Date())
        .then(function (response) {
          console.log($scope.timeHere(), $scope.clicksHere(), $scope.pressContact);
        }, function (errorResponse) {
        if (errorResponse.status === 404) {
          $scope.addStatError = true;
          return;
        }
        console.error(errorResponse);
      });
    });
    /** ********* **/

    var param = parseInt($routeParams.id);
    if (isNaN(param) || param <= 0) {
      PathService.get().error().go();
      return;
    }
    $scope.id = param;
    $scope.sent = false;    // if the mail was sent retreive from url
    $scope.owner = true;
    $scope.userId = 2;

    $scope.backAction = function() {
        history.back();
    };

    $scope.getDate = function(date){
      if(date !== undefined)
        return date.toString().match(/.+?(?=T)/);

      var today = new Date();
      return (today.getFullYear() + '-' + (today.getMonth()+1) + '-' + today.getDate());
    };

    $scope.getMaxStage = function (stages){
      var maxStage = 0;
      stages.forEach(function (stage) {
        if(stage.completed === true) {
          maxStage++;
        }
      });
      return maxStage;
    };

    $scope.project = {};

    projectService.getById($scope.id.toString()).then(function (project) {
      // console.log(project.data.projectStages);
      $scope.project = {      // project infromation from db
        'name': project.data.name,
        'cost': project.data.cost,
        'summary': project.data.summary,
        'id': project.data.id,
        'ownerURL': project.data.owner,
        'owner': {
          'firstName':'Claudio',
          'lastName':'Caniggia',
          'mail':'claudiopaul@gmail.com',
          'id': 2
        },
        'updateDate': project.data.updateDate,
        'catsURL': project.data.categories,
        'imageExists': project.data.portraitExists,
        'slideshowExists': project.data.slideshowExists,
        'slideshow': [],
        'fundingCurrent': project.data.fundingCurrent,
        'fundingTarget': project.data.fundingTarget,
        'stagesURL': project.data.projectStages,
        'getByOwner': project.data.getByOwner,

        /** PARA PROBAR **/
        'stages': [
          {'number': 1, 'name': 'Stage 1', 'comment': '',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 2, 'name': 'Stage 2', 'comment': '',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 3, 'name': 'Stage 3', 'comment': '',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 4, 'name': 'Stage 4', 'comment': '',
            'completed': false, 'completedDate': ''},
          {'number': 5, 'name': 'Stage 5', 'comment': '',
            'completed': false, 'completedDate': ''}
        ]
      };


      // sampleService.get($scope.project.stagesURL).then(function (response) {       // private URI stages;   -> en ProjectDto
      projectService.getStages($scope.project.id).then(function (response) {       // private URI stages;   -> en ProjectDto
        var i = 0;
        console.log(response.data)
        response.data.forEach(function (data){
          if(data !== undefined){
            $scope.project.stages[i].number = data.number;
            $scope.project.stages[i].comment = data.comment;
            $scope.project.stages[i].name = data.name;
            $scope.project.stages[i].completed = data.completed;
            $scope.project.stages[i].completedDate = $scope.getDate(data.completedDate)[0];
            console.log(i, $scope.project.stages[i])
            console.log($scope.getDate(data.completedDate))
            i++;
          }
        })
        $scope.projectstage = $scope.getMaxStage($scope.project.stages);
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            $scope.addStatError = true;
            return;
          }
          console.error(errorResponse);
        }
      );

      sampleService.get($scope.project.ownerURL).then(function (owner) {
        $scope.project.owner.firstName = owner.data.firstName;
        $scope.project.owner.lastName = owner.data.lastName;
        $scope.project.owner.mail = owner.data.email;
        $scope.project.owner.id = owner.data.id;
      });
      sampleService.get($scope.project.catsURL).then(function (categories) {
        var cats = [];
        for (var i = 0; i < categories.data.length; i++){
          cats.push(categories.data[i].name);
        }
        $scope.project.categories = cats;
      });
      if($scope.project.imageExists === true) {
        sampleService.get(project.data.portraitImage).then(function (image) {
          $scope.project.image = image.data.image;
        },function (error) {
          console.log(error);
        });
      }
      if($scope.project.slideshowExists === true) {
        sampleService.get(project.data.slideshowImages).then(function (response) {
          $scope.project.slideshow = [];
          for (var i = 0; i < response.data.length; i++){
            $scope.project.slideshow.push(response.data[i].image);
          }
        });
      }
    });

    /* $scope.project = {      // project infromation from db
      'name': 'Vestnet',
      'cost': 18000,
      'image': 'images/filter.png',
      'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país y en to el mundo',
      'id': 1,
      'owner': {'firstName': 'Grupo', 'lastName': '5', 'mail': 'fchoi@itba.edu.ar', 'id': 1},
      'categories': ['Technology', 'Research'],
      'updateDate': '15/02/2019'
    };*/


    $scope.new = {'name':'', 'comment':''};
    $scope.addStage = function (name, comment, defaultname) {
      var s = $scope.projectstage;
      if(name === '') {
          name = defaultname + (s + 1);
      }
      $scope.project.stages[s].name = name;
      $scope.project.stages[s].comment = comment;
      $scope.project.stages[s].number = s + 1;
      $scope.project.stages[s].completed = true;
      $scope.project.stages[s].completedDate = $scope.getDate();
      /** llamada a set stage **/
      //  setStage($scope.project.id, $scope.project.stages[s])
      projectService.addStage($scope.project.id, s+1, name, comment, true, $scope.getDate())
        .then(function () {}, function (errorResponse) {
          if (errorResponse.status === 404) {
            $scope.addStageError = true;
            return;
          }
          console.error(errorResponse);
        });
      $scope.projectstage++;
      $scope.new.name = '';
      $scope.new.comment = '';
    };

    $scope.toInt = function (num){
      return parseInt(num);
    };

  }]);
});
