'use strict';

define([], function() {
  // This function should be called on paw2020a module initialization
  // It is defined because it will be used before module acquisition by requireJS
  return function($location) {
    var pathService = {};

    pathService.get = function () {
      var base = {
        path: '' // path: '#!'
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
  };
});
