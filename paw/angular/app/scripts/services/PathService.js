define(['paw2020a'], function(paw2020a) {

    'use strict';
    paw2020a.service('PathService', ['$location', function($location) {
        var pathService = {};

        pathService.get = function () {
          var base = {
            path: '#!'
          };

          var append = function(str) {
            base.path += str;
            return base;
          };

          base.absolutePath = function () {
            return base.path.replace('#!', '');
          };

          base.go = function () {
            return $location.path(base.absolutePath());
          };

          base.index = function () {
            return append('/');
          };

          base.login = function () {
            return append('/login');
          };

          base.logout = function () {
            return append('/logout');
          };

          base.projects = function () {
            return append('/projects');
          };

          base.notFound = function () {
            return append('/notFound');
          };

          return base;
        };

        return pathService;
    }]);

});
