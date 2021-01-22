'use strict';

define([], function() {
  // This function should be called on paw2020a module initialization
  // It is defined because it will be used before module acquisition by requireJS
  return function($location) {
    var pathService = {};

    // /\/users\/.*/
    pathService.noAuthRoutesRE = [/^\/$/, /^\/login$/, /^\/welcome$/, /^\/signUp$/, /^\/resetPassword$/, /^\/requestPassword$/, /^\/verify$/];
    pathService.logoutRE = [/^\/logout$/];
    pathService.freeRoutesRE = [/^\/projects\/.*$/, /^\/error$/];
    pathService.investorRoutesRE = [/^\/requests$/, /^\/messages$/, /^\/chat\/[^\/]*$/];
    pathService.entrepreneurRoutesRE = [/^\/dashboard$/, /^\/editProject\/.*$/, /^\/newProject$/, /^\/chat\/[^\/]*\/.+$/];
    pathService.authRoutesRE = [/^\/users\/.*$/, /^\/profile$/].concat(pathService.investorRoutesRE).concat(pathService.entrepreneurRoutesRE);

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

      base.setParamsInUrl = function (paramsObject) {
        if (!paramsObject) return;
        $location.pathReload($location.path().split('?')[0], false).search(paramsObject);
      };

      base.current = function () {
        base.path = $location.url();
        return base;
      };

      base.setFullUrl = function (url) {
        base.path = url;
        return base;
      };

      base.index = function () {
        return append('/');
      };

      base.welcome = function () {
        return append('/welcome');
      };

      base.login = function () {
        return append('/login');
      };

      base.logout = function () {
        return append('/logout');
      };

      base.resetPassword = function () {
        return append('/resetPassword');
      };

      base.requestPassword = function () {
        return append('/requestPassword');
      };

      base.signUp = function () {
        return append('/signUp');
      };

      base.projects = function () {
        return append('/projects');
      };

      base.singleProject = function (id) {
        return append('/projects/' + id);
      };

      base.user = function (id) {
        return append('/users/' + id);
      };

      base.profile = function () {
        return append('/profile');
      };

      base.requests = function () {
        return append('/requests');
      };

      base.dashboard = function () {
        return append('/dashboard');
      };

      base.messages = function () {
        return append('/messages');
      };

      base.newProject = function () {
        return append('/newProject');
      };

      base.verify = function () {
        return append('/verify');
      };

      base.editProject = function (id) {
        return append('/projects/' + id + '/edit');
      };

      base.chat = function (id1, id2) {
        if (!id2) return append('/chat/' + id1);
        return append('/chat/' + id1 + '/' + id2);
      };

      base.error = function () {
        return append('/error');
      };

      return base;
    };

    return pathService;
  };
});
