'use strict';


define(['paw2020a','services/AuthenticationService','services/userService', 'services/projectService', 'services/imageService','services/urlService', 'directives/noFloat', 'directives/pagination', 'directives/customOnChange',
  'services/PathService'], function(paw2020a) {
    paw2020a.controller('feedCtrl', ['AuthenticationService','userService','projectService','imageService','urlService', 'PathService', '$scope', '$routeParams', '$window', function (AuthenticationService,userService,projectService,imageService,urlService,PathService,$scope,$routeParams,$window) {
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

      $scope.favTap = function(id){
        if($scope.containsFav(id)){
          var index = $scope.favs.indexOf(id);
          $scope.favs.splice(index,1);
          userService.putFavorite(id, false).then(function () {

          },function (error) {
            console.error(error)
          })
        }
        else {
          $scope.favs.push(id);
          userService.putFavorite(id, true).then(function () {

          },function (error) {
            console.error(error)
          })

        }
      };



      $scope.lastPage = 0;

      $scope.noProjectsFound = false;
      $scope.loading = true;

      var foot = document.getElementById("foot");
      if(foot) {
        foot.style.display = 'none';
      }

      param = parseInt($routeParams.p);
      if (isNaN(param) || param <= 0) param = 1;
      $scope.page = param;

      param = $routeParams.s;
      $scope.selectedSearchField = $scope.searchField = (!($routeParams.s)) ? undefined : param;
      $scope.fields = [{id: 1, name:'projectNameSearch'}, {id: 2, name:'projectDescSearch'}, {id: 3, name:'ownerNameSearch'}, {id: 4, name:'ownerEmailSearch'}, {id: 5, name:'locationSearch'}];
      param = parseInt($routeParams.f);
      if (isNaN(param)) {
        $scope.selectedField = $scope.field = $scope.fields[0];
      } else {
        aux = $scope.fields.filter(function (el) { return el.id === param; });
        $scope.selectedField = $scope.field = (aux.length !== 0) ? aux[0] : $scope.fields[0];
      }
      $scope.orders = [{id: 1, name:'recommendedOrder'}, {id: 6, name:'oldestOrder'}, {id: 5, name:'newestOrder'}, {id: 2, name:'costAscendingOrder'},
        {id: 3, name:'costDescendingOrder'}, {id: 4, name:'alphabeticalOrder'}, {id: 7, name:'percentageAscendingOrder'}, {id: 8, name:'percentageDescendingOrder'}];
      param = parseInt($routeParams.o);
      if (isNaN(param)) {
        $scope.selectedOrder = $scope.order = $scope.orders[0];
      } else {
        aux = $scope.orders.filter(function (el) { return el.id === param; });
        $scope.selectedOrder = $scope.order = (aux.length !== 0) ? aux[0] : $scope.orders[0];
      }
      $scope.categories = [emptyCategory];
      param = parseInt($routeParams.c);
      $scope.selectedCategory = $scope.category = (isNaN(param)) ? emptyCategory : {id:param, name:'noFilter'};

      param = parseInt($routeParams.min);
      $scope.selectedMinCost = $scope.minCost = (isNaN(param)) ? undefined : param;
      param = parseInt($routeParams.max);
      $scope.selectedMaxCost = $scope.maxCost = (isNaN(param)) ? undefined : param;
      param = parseInt($routeParams.pmin);
      $scope.selectedMinPercentage = $scope.minPercentage = (isNaN(param)) ? undefined : param;
      param = parseInt($routeParams.pmax);
      $scope.selectedMaxPercentage = $scope.maxPercentage = (isNaN(param)) ? undefined : param;

      this.normalizeNumberInput = function () {
        if ($scope.minCost != null && $scope.maxCost != null && $scope.minCost > $scope.maxCost) {
          $scope.selectedMinCost = $scope.minCost = undefined;
          $scope.selectedMaxCost = $scope.maxCost = undefined;
        }
        $scope.costRangeError = false;
        if ($scope.minPercentage != null && $scope.maxPercentage != null && $scope.minPercentage > $scope.maxPercentage) {
          $scope.selectedMinPercentage = $scope.minPercentage = undefined;
          $scope.selectedMaxPercentage = $scope.maxPercentage = undefined;
        }
        $scope.percentageRangeError = false;
      };
      this.normalizeNumberInput();

      projectService.getCategories().then(function (cats) {
        $scope.categories = [emptyCategory].concat(cats.data);
        param = parseInt($routeParams.c);
        if (!isNaN(param)) {
          aux = $scope.categories.filter(function (el) { return el.id === param; });
          $scope.selectedCategory = $scope.category = (aux.length !== 0) ? aux[0] : emptyCategory;
        }
      });

      $scope.loadingPage = false;
      $scope.getToPage = function (page) {
        $scope.loadingPage = true;
        $scope.page = page;
        _this.setPathParams();
        projectService.getPage(_this.filterObject()).then(function (projects) {
          $scope.loadingPage = false;
          _this.getArgs(projects.headers().link);
          _this.showProjects(projects.data);
          $window.scrollTo(0,0);
        }, function (errorResponse) {
          $scope.loadingPage = false;
          console.error(errorResponse);
        });
      };

      this.setPathParams = function () {
        PathService.get().setParamsInUrl({p:$scope.page, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.selectedSearchField, max:$scope.selectedMaxCost, min:$scope.selectedMinCost,
          pmax:$scope.selectedMaxPercentage, pmin:$scope.selectedMinPercentage, c:$scope.selectedCategory.id});
      };

      this.filterObject = function () {
        var pmin = ($scope.selectedMinPercentage == null) ? undefined : $scope.selectedMinPercentage / 100.0;
        var pmax = ($scope.selectedMaxPercentage == null || $scope.selectedMaxPercentage === 100) ? undefined : $scope.selectedMaxPercentage / 100.0;
        return {p:$scope.page, l:pageSize, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.selectedSearchField, max:$scope.selectedMaxCost, min:$scope.selectedMinCost,
          pmax:pmax, pmin:pmin, c:$scope.selectedCategory.id};
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
          $scope.lastPage = 1;
          if(foot) {
            foot.style.display = 'block';
          }
          return;
        }
        $scope.projects = projects;
        var map = {};
        for(var i = 0; i < $scope.projects.length; i++) {
          map[$scope.projects[i].id] = i;
          $scope.projects[i].portraitExists = false;
          urlService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
            $scope.projects[map[image.data.route]].image = image.data.image;
            $scope.projects[map[image.data.route]].portraitExists = true;
          }, function (errorResponse) {
            if (errorResponse.status === 404) {
              return;
            }
            console.error(errorResponse);
          });
        }
        $scope.loading = false;
        if(foot) {
          foot.style.display = 'block';
        }
      };

      $scope.hideornot = function (tar, curr){
        // console.log('perc -> ',$scope.toInt((tar/curr)*100))
        return $scope.toInt((tar/curr)*100) > 10;
      };

      $scope.clearFilter = function () {
        $scope.field = $scope.fields[0];
        $scope.order = $scope.orders[0];
        $scope.category = emptyCategory;
        $scope.minCost = undefined;
        $scope.maxCost = undefined;
        $scope.searchField = undefined;
        $scope.minPercentage = undefined;
        $scope.maxPercentage = undefined;

        $scope.costRangeError = false;
        $scope.percentageRangeError = false;
      };

      this.updateSelectedValues = function () {
        $scope.selectedField = $scope.field;
        $scope.selectedOrder = $scope.order;
        $scope.selectedCategory = $scope.category;
        $scope.selectedMinCost = $scope.minCost;
        $scope.selectedMaxCost = $scope.maxCost;
        $scope.selectedSearchField = $scope.searchField;
        $scope.selectedMinPercentage = $scope.minPercentage;
        $scope.selectedMaxPercentage = $scope.maxPercentage;
      };

      $scope.applyFilter = function () {
        if ($scope.minCost != null && $scope.maxCost != null && $scope.minCost > $scope.maxCost) {
          $scope.costRangeError = true;
          return;
        }
        $scope.costRangeError = false;
        if ($scope.minPercentage != null && $scope.maxPercentage != null && $scope.minPercentage > $scope.maxPercentage) {
          $scope.percentageRangeError = true;
          return;
        }
        $scope.percentageRangeError = false;
        _this.updateSelectedValues();

        $scope.page = 1;
        $scope.projects = [];
        $scope.noProjectsFound = false;
        $scope.loading = true;
        _this.setPathParams();
        projectService.getPage(_this.filterObject()).then(function (projects) {
          _this.getArgs(projects.headers().link);
          _this.showProjects(projects.data);
          $window.scrollTo(0,0);
        }, function (errorResponse) {
          $scope.loading = false;
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
      };

      $scope.goToProject = function (id) {
        PathService.get().singleProject(id).go();
      };
    }]);
});
