define(['paw2020a', 'restangular'], function(paw2020a) {

    'use strict';
    paw2020a.service('projectService', ['Restangular', function(Restangular) {
      var projectService = {};

      var root = Restangular.one('projects');

      projectService.getAll = function () {
        return Restangular.all('projects').getList();
      };



      projectService.getPage = function(page, order, field, keyWord, maxCost, minCost, cat){
        var params = {p:page, o:order, f:field,s:keyWord,max:maxCost,min:minCost, c:cat};
        return root.get(params)
      }


      projectService.create = function (n, summ, c, category) {
        var body = {name: n, summary: summ, cost: c, cat: category}
        return root.customPOST(body)
      }


      projectService.getById = function (id) {
        return root.one(id).get()
      }

      projectService.getCatById = function (id) {
        return root.one(id).get('categories')
      }


      projectService.getCategories = function () {
        return root.get('categories')
      }


      projectService.modifyProj = function (n, sum, c, category,id) {
        var body = {name: n, summary: sum, cost: c, cat: category}
        return root.one(id).customPUT(body)
      }


      projectService.addHit = function (id) {
        return root.one(id).one('hit').customPUT()
      }


      projectService.funded = function (id) {
        return root.one(id).one('funded').customPUT()
      }




      return projectService;
    }]);

});
