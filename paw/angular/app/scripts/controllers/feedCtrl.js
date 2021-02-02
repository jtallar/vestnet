'use strict';


define(['paw2020a','services/AuthenticationService','services/userService', 'services/projectService', 'services/imageService','services/sampleService', 'directives/noFloat', 'directives/pagination',
  'services/PathService'], function(paw2020a) {
    paw2020a.controller('feedCtrl', ['AuthenticationService','userService','projectService','imageService','sampleService', 'PathService', '$scope', '$routeParams', '$window', function (AuthenticationService,userService,projectService,imageService,sampleService,PathService,$scope,$routeParams,$window) {
      var _this = this;
      var pageSize = 12, param, aux;
      $scope.page = 1;

      var emptyCategory = {id:null, name:'noFilter'};

      $scope.isInvestor = AuthenticationService.isInvestor();

      $scope.favs = [];
      if($scope.isInvestor){
        userService.getFavorites().then(function (response) {
          for(var i = 0; i < response.data.length;i++){
            $scope.favs.push(response.data[i].projectId)
          }
        })
      }


      $scope.containsFav = function(id){
        return $scope.favs.includes(id)
      };

      // $scope.formatPrice = function(number) {
      //   var formatter = new Intl.NumberFormat(navigator.language, { style: 'currency', currency: 'USD', minimumFractionDigits: 0, });
      //   return formatter.format(number);
      // }

      $scope.favTap = function(id){
        if($scope.containsFav(id)){
          var index = $scope.favs.indexOf(id);
          $scope.favs.splice(index,1);
          userService.putFavorite(id, false).then(function () {

          },function (error) {
            console.log(error)
          })
        }
        else {
          $scope.favs.push(id);
          userService.putFavorite(id, true).then(function () {

          },function (error) {
            console.log(error)
          })

        }
        console.log(id)
      };



      $scope.lastPage = 0;

      $scope.noProjectsFound = false;
      $scope.loading = true;

      param = parseInt($routeParams.p);
      if (isNaN(param) || param <= 0) param = 1;
      $scope.page = param;

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
        $scope.page = page;
        _this.setPathParams();
        projectService.getPage(_this.filterObject()).then(function (projects) {
          _this.getArgs(projects.headers().link);
          _this.showProjects(projects.data);
          $window.scrollTo(0,0);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };

      this.setPathParams = function () {
        PathService.get().setParamsInUrl({p:$scope.page, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost, c:$scope.selectedCategory.id});
      };

      this.filterObject = function () {
        return {p:$scope.page, l:pageSize, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost, c:$scope.selectedCategory.id};
      };

      this.getArgs = function (string) {

        var lastLink = string.split(',').filter(function (el) {
          return el.includes('last');
        });
        $scope.lastPage = parseInt(lastLink[0].split('p=')[1][0]);
      };

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

        $scope.page = 1;
        $scope.projects = [];
        $scope.noProjectsFound = false;
        _this.setPathParams();
        projectService.getPage(_this.filterObject()).then(function (projects) {
          _this.getArgs(projects.headers().link);
          _this.showProjects(projects.data);
          $window.scrollTo(0,0);
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };

      $scope.projects = [];
      projectService.getPage(_this.filterObject()).then(function (projects) {
        _this.getArgs(projects.headers().link);
        _this.showProjects(projects.data);
      }, function (errorResponse) {
        console.error(errorResponse);
      });

      $scope.toInt = function (num){
        return parseInt(num);
      }

    }]);
});
