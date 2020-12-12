    'use strict';

define(['paw2020a', 'services/projectService', 'services/imageService'], function(paw2020a) {
    paw2020a.controller('feedCtrl', ['projectService','imageService', '$scope', function (projectService,imageService, $scope) {

      var page = 1
      $scope.filterdata = {
        'field': '' ,
        'category': '',
        'order': '',
        'search': '',
        'minCost': 100,
        'maxCost': 100000
      };

      var _this = this


      $scope.order = ['price' , 'date'];
      $scope.fields = ['technology', 'audio'];
      $scope.categories = [];

      projectService.getCategories().then(function (cats) {
        $scope.categories = cats
      })
      $scope.clearFilter = function () {
          this.filterdata.field = '';
          this.filterdata.category = '';
          this.filterdata.minCost = 100;
          this.filterdata.maxCost = 100000;
          this.filterdata.search = '';

      };

      $scope.adjustInputs = function () {
        var minTag = document.getElementById('filter-form-min');
        if (minTag.value < 0 || minTag.value > 9999999) {
          minTag.value = '';
        }
        if (minTag.value.length) {
          minTag.value = Math.round(minTag.value);
        }
        var maxTag = document.getElementById('filter-form-max');
        if (maxTag.value < 0 || maxTag.value > 9999999) {
          maxTag.value = '';
        }
        if (maxTag.value.length) {
          maxTag.value = Math.round(maxTag.value);
        }

        projectService.getPage(page, $scope.filterdata.order,$scope.filterdata.field, $scope.filterdata.search, $scope.filterdata.maxCost, $scope.filterdata.minCost, $scope.filterdata.category)
      };

      // $scope.projects = [{
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1
      //   }];

      $scope.projects = []

      projectService.getPageNoFilter(page.toString()).then(function (projects) {
        $scope.projects = projects


        var map = {}

        for(var i = 0; i < $scope.projects.length; i++){
            map[$scope.projects[i].id] = i
            if($scope.projects[i].id != 80) {
              imageService.getProjectImage(String($scope.projects[i].id), i).then(function (image) {
                $scope.projects[map[image.route]].image = image.image

              })
            }
        }

        console.log($scope.projects)
      })






    }]);

});
