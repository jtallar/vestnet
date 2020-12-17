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

      base.go = function (paramsObject) {
        // Clear current search params
        $location.url($location.path());
        if (paramsObject) return $location.path(base.absolutePath()).search(paramsObject);
        return $location.path(base.absolutePath());
      };

      base.current = function () {
        return $location.url();
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

      base.singleProject = function (id) {
        return append('/projects/' + id);
      };

      base.notFound = function () {
        return append('/notFound');
      };

      base.forbidden = function () {
        // TODO: Go to forbidden custom
        return append('/notFound');
      };

      return base;
    };

    return pathService;
  };
});
