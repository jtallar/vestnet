    'use strict';

define(['paw2020a','services/projectService', 'services/sampleService'], function(paw2020a) {
    paw2020a.controller('singleViewCtrl',['projectService','sampleService','$scope', function(projectService,sampleService, $scope) {
      $scope.sent = false;    // if the mail was sent retreive from url
      $scope.id = 108;

      $scope.backAction = function() {
        if (this.sent) {
          history.back();
        } else {
          history.back();
          history.back();
        }
      };

      $scope.project= {};

      projectService.getById($scope.id.toString()).then(function (project) {
        console.log(project)
        $scope.project = {      // project infromation from db
          'name': project.data.name,
          'cost': project.data.cost,
          'summary': project.data.summary,
          'id': project.data.id,
          'ownerURL': project.data.owner,
          'owner': {},
          'updateDate': project.data.updateDate,
          'catsURL': project.data.categories,
          'imageExists': project.data.portraitExists,
          'slideshowExists': project.data.slideshowExists
        };




          sampleService.get($scope.project.ownerURL).then(function (owner) {

            $scope.project.owner.firstName = owner.data.firstName;
            $scope.project.owner.lastName = owner.data.lastName;
            $scope.project.owner.mail = owner.data.email;
            $scope.project.owner.id = owner.data.id
          });



        sampleService.get($scope.project.catsURL).then(function (categories) {
          var cats = [];
          for (var i = 0; i < categories.data.length ;i++){
            cats.push(categories.data[i].name)
          }
          $scope.project.categories = cats

        });

        if($scope.project.imageExists === true) {
          sampleService.get(project.data.portraitImage).then(function (image) {
            $scope.project.image = image.data.image
          })
        }

        if($scope.project.slideshowExists === true) {
          sampleService.get(project.data.slideshowImages).then(function (response) {

            $scope.project.slideshow = []
            for (var i = 0; i < response.data.length ;i++){
              $scope.project.slideshow.push(response.data[i].image)
            }


          })
        }






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
