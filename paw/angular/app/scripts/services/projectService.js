define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

    'use strict';
    paw2020a.service('projectService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
      var projectService = {};

      projectService.getAll = function () {
        return AuthenticatedRestangular.all('projects').getList();
      };

      return projectService;
    }]);

});
