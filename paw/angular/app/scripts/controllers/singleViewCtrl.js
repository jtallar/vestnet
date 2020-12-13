    'use strict';

define(['paw2020a','services/projectService', 'services/sampleService'], function(paw2020a) {
    paw2020a.controller('singleViewCtrl',['projectService','sampleService','$scope', function(projectService,imageService,userService,sampleService, $scope) {
      $scope.sent = false;    // if the mail was sent retreive from url
      $scope.userId = 1;      // user id from db
      $scope.id = 82

      $scope.backAction = function() {
        if (this.sent) {
          history.back();
        } else {
          history.back();
          history.back();
        }
      };

      $scope.project= {}

      projectService.getById($scope.id.toString()).then(function (project) {

        $scope.project = {      // project infromation from db
          'name': project.name,
          'cost': project.cost,
          'summary': project.summary,
          'id': project.id,
          'ownerURL': project.owner,
          'owner': {},
          'updateDate': project.updateDate,
          'catsURL': project.categories,
          'imageURL': project.portraitImage,
          'slideshowURL': project.slideshow

        };


        console.log($scope.project.imageURL)


        sampleService.get($scope.project.ownerURL).then(function (owner) {

          $scope.project.owner.firstName = owner.firstName
          $scope.project.owner.lastName = owner.lastName
          $scope.project.owner.mail = owner.email
          $scope.project.owner.id = owner.id
        })


        sampleService.get($scope.project.catsURL).then(function (categories) {
          var cats = []
          for (var i = 0; i < categories.length ;i++){
            cats.push(categories[i].name)
          }
          $scope.project.categories = cats

        })


        sampleService.get($scope.project.imageURL).then(function (image) {
          $scope.project.image = image.image
        })









      })











      // $scope.project = {      // project infromation from db
      //   'name': 'Vestnet',
      //   'cost': 18000,
      //   'image': 'images/filter.png',
      //   'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país y en todo el mundo',
      //   'id': 1,
      //   'owner': {'firstName': 'Grupo', 'lastName': '5', 'mail': 'fchoi@itba.edu.ar', 'id': 1},
      //   'categories': ['Technology', 'Research'],
      //   'updateDate': '15/02/2019'
      // };








    }]);

});
