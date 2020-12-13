define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

    'use strict';
    paw2020a.service('projectService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
      var projectService = {};

      var root = AuthenticatedRestangular.one('projects');

      projectService.getAll = function () {
        return AuthenticatedRestangular.all('projects').getList();
      };


      projectService.getPageNoFilter = function(page){
        var params = {p:page};
        return root.get(params);
      };

      projectService.getPage = function(page, order, field, keyWord, maxCost, minCost, cat){
        var params = {p:page, o:order, f:field,s:keyWord,max:maxCost,min:minCost, c:cat};

        return root.get(params);
      };


      projectService.create = function (n, summ, c, category) {
        var body = {name: n, summary: summ, cost: c, cat: category};
        return root.customPOST(body)
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


      projectService.modifyProj = function (n, sum, c, category,id) {
        var body = {name: n, summary: sum, cost: c, cat: category};
        return root.one(id).customPUT(body)
      };


      projectService.addHit = function (id) {
        return root.one(id).one('hit').customPUT()
      };


      projectService.funded = function (id) {
        return root.one(id).one('funded').customPUT()
      };


      return projectService;
    }]);




});
