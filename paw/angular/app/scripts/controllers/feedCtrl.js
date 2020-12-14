'use strict';

// import {Component} from '@angular/core';
//
// @Component({
//   selector: 'ngbd-progressbar-basic',
//   templateUrl: './progressbar-basic.html'
// })
// export class NgbdProgressbarBasic {
// }

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
          'target': 1000,
          'current': 750,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'percentage': 75
        },
        {
          'name': 'Superchero',
          'target': 1000,
          'current': 900,
          'image': 'images/projectNoImage.png',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'percentage': 90
        },
        {
          'name': 'Mate Electrico',
          'target': 1000,
          'current': 200,
          'image': 'images/mate.jpg',
          'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
          'id': 1,
          'percentage': 20
        },
        {
          'name': 'Cerberus',
          'target': 1000,
          'current': 660,
          'image': 'images/yeoman.png',
          'summary': 'Es un proyecto super pedorro',
          'id': 1,
          'percentage': 66
        },
        {
          'name': 'BED',
          'target': 1000,
          'current': 20,
          'image': 'images/projectNoImage.png',
          'summary': 'jaja una tablet para tarados jaja',
          'id': 1,
          'percentage': 2
        },
        {
          'name': 'Otros',
          'target': 1000,
          'current': 880,
          'image': 'images/projectNoImage.png',
          'summary': 'bla',
          'id': 1,
          'percentage': 88
        }
      ];

    });

});
