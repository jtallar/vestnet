 'use strict';

define(['paw2020a','services/projectService', 'services/sampleService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('singleViewCtrl',['projectService','sampleService', 'PathService', '$scope', '$routeParams', function(projectService,sampleService, PathService, $scope, $routeParams) {

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
      /** PUT STATS **/
      console.log($scope.timeHere(), $scope.clicksHere(), false);
      if (this.sent) {
        history.back();
      } else {
        history.back();
        history.back();
      }
    };

    $scope.contact = function (){
      console.log($scope.timeHere(), $scope.clicksHere(), true);
    };

    /**    QUEDA ASI ???  **/
    $scope.getDate = function(){
      var today = new Date();
      return (today.getDate() + '/' + today.getMonth() + '/' + today.getFullYear());
    };

    $scope.getMaxStage = function (stages){
      var maxStage = 0;
      stages.forEach(function (stage) {
        if(stage.completed === true) {
          maxStage++;
        }
      });
      console.log(maxStage);
      return maxStage;
    };


    $scope.project = {};

    /* $scope.project = {      // project infromation from db
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
    };*/

    projectService.getById($scope.id.toString()).then(function (project) {
      console.log(project);
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
        'slideshow' : [],
        'fundingCurrent' : project.data.fundingCurrent,
        'fundingTarget' : project.data.fundingTarget,

        /** PARA PROBAR **/
        'stages': [
          {'number': 1, 'name': ' 1', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 2, 'name': 'Stage 2', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 3, 'name': 'Stage 3', 'comment': 'This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.',
            'completed': false, 'completedDate': '02/05/2021'},
          {'number': 4, 'name': 'Stage 4', 'comment': '',
            'completed': false, 'completedDate': ''},
          {'number': 5, 'name': 'Stage 5', 'comment': '',
            'completed': false, 'completedDate': ''}
        ]
      };

      $scope.projectstage = $scope.getMaxStage($scope.project.stages);

      // $scope.maxStage = getMaxStage($scope.project.stages)

      /** STAGES --- COMENTADO PARA QUE NO ROMPA **/
      /* sampleService.get(project.data.stages).then(function (response) {       // private URI stages;   -> en ProjectDto
        $scope.project.stages = [];
        var i = 0;
        response.data.forEach(function (data){
          $scope.project.stages[i].number = data.stage.number;
          $scope.project.stages[i].comment = data.stage.name;
          $scope.project.stages[i].name = data.stage.name;
          $scope.project.stages[i].completed = data.stage.completed;
          $scope.project.stages[i].completedDate = data.stage.completedDate;
          i++;
        })
      })*/

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
      $scope.projectstage++;
      $scope.new.name = '';
      $scope.new.comment = '';
    };

    // $scope.deleteStage = function (stage){
    //   $scope.project.stages[s-1].name = '';
    //   $scope.project.stages[s-1].comment = '';
    //   $scope.project.stages[s-1].number = s;
    //   $scope.project.stages[s-1].completed = false;
    //   $scope.project.stages[s-1].completedDate = '';
    //   /** llamada a delete stage **/
    //   //  deleteStage($scope.project.id, $scope.project.stages[s])    o
    //   //  deleteStage($scope.project.id, $scope.project.stages[s].number)
    //   $scope.project.stage--;
    // }

    $scope.toInt = function (num){
      return parseInt(num);
    };

  }]);
});
