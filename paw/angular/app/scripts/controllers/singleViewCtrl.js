 'use strict';

define(['paw2020a','services/projectService', 'services/userService', 'services/urlService', 'services/PathService', 'services/AuthenticationService'], function(paw2020a) {
  paw2020a.controller('singleViewCtrl',['projectService', 'userService', 'urlService', 'PathService', 'AuthenticationService', '$scope', '$routeParams', function(projectService,userService,urlService, PathService, AuthenticationService, $scope, $routeParams) {

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
    var pressContact = 0;

    $scope.$on("$destroy", function(){
      if ($scope.addStatError || $scope.project == null || $scope.project.id == null) return;
      projectService.addStat($scope.project.id, $scope.timeHere(), $scope.clicksHere(), pressContact, new Date())
        .then(function (response) {
          // console.log($scope.timeHere(), $scope.clicksHere(), pressContact);
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
      PathService.get().error().replace();
      return;
    }
    $scope.id = param;

    var back = !!($routeParams.back);
    $scope.imageError = !!($routeParams.imageError);

    $scope.isEntrepreneur = AuthenticationService.isEntrepreneur();
    $scope.isInvestor = AuthenticationService.isInvestor();

    $scope.backAction = function() {
      if (back) {
        history.back();
      }
      history.back();
    };

    $scope.goToEdit = function () {
      PathService.get().editProject($scope.id).go({back:true});
    };

    $scope.goToChat = function () {
      pressContact = 1;
      PathService.get().chat($scope.project.id).go();
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
      $scope.project = project.data;
      $scope.project.slideshow = [];
      $scope.project.categorieObjects = [];
      $scope.project.stages = [
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
      ];

      // urlService.get($scope.project.projectStages).then(function (response) {       // private URI stages;   -> en ProjectDto
      projectService.getStages($scope.project.id).then(function (response) {       // private URI stages;   -> en ProjectDto
        if (response.data.length === 0) {
          $scope.projectstage = 0;
          return;
        }
        var i = 0;
        response.data.forEach(function (data){
          if(data !== undefined){
            $scope.project.stages[i].number = data.number;
            $scope.project.stages[i].comment = data.comment;
            $scope.project.stages[i].name = data.name;
            $scope.project.stages[i].completed = data.completed;
            $scope.project.stages[i].completedDate = data.completedDate;
            i++;
          }
        });
        $scope.projectstage = $scope.getMaxStage($scope.project.stages);
      }, function (errorResponse) {
        // 404 should not happen
        console.error(errorResponse);
      });

      urlService.get($scope.project.owner).then(function (owner) {
        $scope.project.owner = owner.data;
        $scope.project.ownerUrl = PathService.get().user($scope.project.owner.id).path;
      }, function (errorResponse) {
        // 404 should not happen
        console.error(errorResponse);
      });

      urlService.get($scope.project.categories).then(function (categories) {
        $scope.project.categorieObjects = categories.data;
      }, function (errorResponse) {
        // 404 should not happen
        console.error(errorResponse);
      });

      $scope.project.portraitAvailable = false;
      if($scope.project.portraitExists) {
        urlService.get(project.data.portraitImage).then(function (image) {
          $scope.project.image = image.data.image;
          $scope.project.portraitAvailable = true;
        },function (error) {
          console.log("No image");
        });
      }
      $scope.project.slideshowAvailable = false;
      if($scope.project.slideshowExists) {
        urlService.get(project.data.slideshowImages).then(function (response) {
          $scope.project.slideshow = [];
          for (var i = 0; i < response.data.length; i++){
            $scope.project.slideshow.push(response.data[i].image);
          }
          $scope.project.slideshowAvailable = $scope.project.slideshow.length > 0;
        },function (error) {
          console.log("No slideshow");
        });
      }
    }, function (errorResponse) {
      if (errorResponse.status === 404) {
        $scope.addStatError = true;
        PathService.get().error().replace();
        return;
      }
      console.error(errorResponse);
    });

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
      $scope.project.stages[s].completedDate = new Date();
      /** llamada a set stage **/
      //  setStage($scope.project.id, $scope.project.stages[s])
      projectService.addStage($scope.project.id, s+1, name, comment, true, new Date())
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

    $scope.isFav = false;
    if ($scope.isInvestor) {
      userService.getFavorites().then(function (response) {
        $scope.isFav = response.data.some(function(e) { return e.projectId === $scope.id });
      }, function (errorResponse) {
        console.error(errorResponse);
      });
    }

    $scope.favTap = function () {
      if (!$scope.isInvestor) return;
      $scope.isFav = !$scope.isFav;
      userService.putFavorite($scope.id, $scope.isFav).then(function () {
        // Do nothing
      },function (error) {
        console.error(error);
      });
    };

    $scope.toInt = function (num){
      return parseInt(num);
    };

    $scope.hideornot = function (tar, curr){
      // console.log('perc -> ',$scope.toInt((tar/curr)*100))
      return $scope.toInt((tar/curr)*100) > 10;
    };

  }]);
});
