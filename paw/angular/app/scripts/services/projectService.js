define(['paw2020a', 'restangular'], function(paw2020a) {

    'use strict';
    paw2020a.service('projectService', ['Restangular', function(Restangular) {
      var projectService = {};

      projectService.getAll = function () {
        return Restangular.all('projects').getList();
      };

      return projectService;
    }]);

});
