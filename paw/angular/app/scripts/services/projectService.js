define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

    'use strict';
    paw2020a.service('projectService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
      var projectService = {};

      var root = AuthenticatedRestangular.one('projects');

      projectService.getAll = function () {
        return AuthenticatedRestangular.all('projects').getList();
      };

      projectService.getPage = function(params) {
        return root.get(params);
      };

      projectService.create = function (project) {
        return root.customPOST(project);
      };


      projectService.getById = function (id) {
        return root.one(id).get()
      };

      projectService.getCatById = function (id) {
        return root.one(id).one('categories').get()
      };


      projectService.getCategories = function () {
        return root.one('categories').get()
      };


      projectService.update = function (project) {
        return root.one(project.id.toString()).customPUT(project);
      };


      projectService.addHit = function (id) {
        return root.one(id).one('hit').customPUT()
      };

      projectService.getStages = function (id){
        return root.one(id.toString()).one('stages').get();
      }

      projectService.addStage = function (pid, n, name, comm, comp, date) {
        var body = {number : n, name: name, comment: comm, completed: comp, completedDate: date};
        return root.one(pid.toString()).one('stages').customPUT(body)
      };

      projectService.addStat = function (pid, time, clicks, cont, inv, date){
        var body = {secondsAvg : time, clicksAvg: clicks, contactClicks: cont, investorsSeen: inv, lastSeen: date};
        return root.one(pid.toString()).one('stats').customPUT(body)
      };

      projectService.funded = function (id) {
        return root.one(id).one('funded').customPUT()
      };


      projectService.getStats = function (id) {
        return root.one(id).one('stats').get()
      };

      projectService.toggleClosed = function (id) {
        return root.one(id).one('close').customPUT();
      };


      return projectService;
    }]);




});
