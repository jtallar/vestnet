    'use strict';

define(['paw2020a'], function(paw2020a) {
    paw2020a.controller('feedCtrl', function ($scope) {

      $scope.filterdata = {
        'field': '' ,
        'category': '',
        'order': '',
        'search': '',
        'minCost': 100,
        'maxCost': 100000
      };

      $scope.order = ['price' , 'date'];
      $scope.fields = ['technology', 'audio'];
      $scope.categories = ['research', 'sports'];
      $scope.clearFilter = function () {
          this.filterdata.field = '';
          this.filterdata.category = '';
          this.filterdata.maxCost = 100;
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
      };

      $scope.projects = [{
          'name': 'Vestnet',
          'cost': 100000,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1
        }];

    });

});
