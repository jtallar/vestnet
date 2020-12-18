'use strict';


// TODO: Pagination, Show page number in "showing page ... from ... pages
define(['paw2020a', 'services/projectService', 'services/imageService','services/sampleService', 'directives/noFloat',
  'services/PathService'], function(paw2020a) {
    paw2020a.controller('feedCtrl', ['projectService','imageService','sampleService', 'PathService', '$scope', '$routeParams', function (projectService,imageService,sampleService,PathService,$scope,$routeParams) {
      var _this = this;
      var pageSize = 12, page = 1, param, aux;
      $scope.page = 1

      var emptyCategory = {id:null, name:'noFilter'};

      $scope.firstPage = 0
      $scope.lastPage = 0

      $scope.noProjectsFound = false;
      $scope.loading = true;

      param = parseInt($routeParams.p);
      if (isNaN(param) || param <= 0) param = 1;
      page = param;

      $scope.searchField = $routeParams.s;
      $scope.fields = [{id: 1, name:'projectNameSearch'}, {id: 2, name:'projectDescSearch'}, {id: 3, name:'ownerNameSearch'}, {id: 4, name:'ownerEmailSearch'}, {id: 5, name:'locationSearch'}];
      param = parseInt($routeParams.f);
      if (isNaN(param)) {
        $scope.selectedField = $scope.fields[0];
      } else {
        aux = $scope.fields.filter(function (el) { return el.id === param; });
        (aux.length !== 0) ? $scope.selectedField = aux[0] : $scope.selectedField = $scope.fields[0];
      }
      $scope.orders = [{id: 1, name:'recommendedOrder'}, {id: 6, name:'oldestOrder'}, {id: 5, name:'newestOrder'}, {id: 2, name:'costAscendingOrder'}, {id: 3, name:'costDescendingOrder'}, {id: 4, name:'alphabeticalOrder'}];
      param = parseInt($routeParams.o);
      if (isNaN(param)) {
        $scope.selectedOrder = $scope.orders[0];
      } else {
        aux = $scope.orders.filter(function (el) { return el.id === param; });
        (aux.length !== 0) ? $scope.selectedOrder = aux[0] : $scope.selectedOrder = $scope.fields[0];
      }
      $scope.categories = [emptyCategory];
      $scope.selectedCategory = emptyCategory;
      param = parseInt($routeParams.min);
      $scope.minCost = (isNaN(param)) ? undefined : param;
      param = parseInt($routeParams.max);
      $scope.maxCost = (isNaN(param)) ? undefined : param;
      $scope.costRangeError = false;

      projectService.getCategories().then(function (cats) {
        $scope.categories = $scope.categories.concat(cats.data);
        param = parseInt($routeParams.c);
        if (!isNaN(param)) {
          aux = $scope.categories.filter(function (el) { return el.id === param; });
          if (aux.length !== 0) $scope.selectedCategory = aux[0];
        }
      });



      $scope.getToPage = function (page) {
        $scope.page = page

        var params = _this.filterObject()
        params.p = page
        projectService.getPage(params).then(function (projects) {
          _this.getArgs(projects.headers().link)
          _this.showProjects(projects.data);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };

      this.setPathParams = function () {
        console.log(page)
        PathService.get().setParamsInUrl({p:page, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost, c:$scope.selectedCategory.id});
      };

      this.filterObject = function () {
        return {p:page, l:pageSize, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost, c:$scope.selectedCategory.id};
      };

      this.getArgs = function (string) {


        var firstLinkStart = string.indexOf("<")
        var firstLinkFinish = string.indexOf(">")
        var secondLinkStart = string.indexOf("<", firstLinkStart + 1)
        var secondLinkFinish = string.indexOf(">", secondLinkStart + 1)



        var firstLink = string.substr(firstLinkStart + 1, firstLinkFinish - firstLinkStart - 1)

        var secondLink = string.substr( secondLinkStart + 1, secondLinkFinish - secondLinkStart - 1)


        //console.log(secondLink)

        var firstPageSplit = firstLink.split("p=")
        var lastPageSplit = secondLink.split("p=")

        $scope.firstPage = firstPageSplit[2]
        $scope.lastPage = lastPageSplit[2]
      }

      this.showProjects = function (projects) {
        if (projects.length === 0) {
          $scope.noProjectsFound = true;
          $scope.loading = false;
          return;
        }
        $scope.projects = projects;
        var map = {};
        for(var i = 0; i < $scope.projects.length; i++) {
          map[$scope.projects[i].id] = i;
          if ($scope.projects[i].portraitExists) {
            sampleService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
              $scope.projects[map[image.data.route]].image = image.data.image
            }, function (err) {
              console.log("No image")
            });
          }
        }
        $scope.loading = false;
      };

      $scope.clearFilter = function () {
        $scope.selectedField = $scope.fields[0];
        $scope.selectedOrder = $scope.orders[0];
        $scope.selectedCategory = emptyCategory;
        $scope.minCost = undefined;
        $scope.maxCost = undefined;
        $scope.searchField = undefined;
      };

      $scope.applyFilter = function () {
        if ($scope.minCost && $scope.maxCost && $scope.minCost > $scope.maxCost) {
          $scope.costRangeError = true;
          return;
        }

        $scope.page = 1

        $scope.projects = [];
        $scope.noProjectsFound = false;
        _this.setPathParams();
        projectService.getPage(_this.filterObject()).then(function (projects) {
          _this.getArgs(projects.headers().link)
          _this.showProjects(projects.data);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };

      $scope.projects = [];
      projectService.getPage(_this.filterObject()).then(function (projects) {
        _this.getArgs(projects.headers().link)
        _this.showProjects(projects.data);
      }, function (errorResponse) {
        console.error(errorResponse);
      });

      $scope.toInt = function (num){
        return parseInt(num);
      }






    }]);





});
