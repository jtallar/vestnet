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
    foot.style.display = 'none';

    param = parseInt($routeParams.p);
    if (isNaN(param) || param <= 0) param = 1;
    $scope.page = param;

    param = $routeParams.s;
    $scope.searchField = (!($routeParams.s)) ? undefined : param;
    $scope.fields = [{id: 1, name:'projectNameSearch'}, {id: 2, name:'projectDescSearch'}, {id: 3, name:'ownerNameSearch'}, {id: 4, name:'ownerEmailSearch'}, {id: 5, name:'locationSearch'}];
    param = parseInt($routeParams.f);
    if (isNaN(param)) {
      $scope.selectedField = $scope.fields[0];
    } else {
      aux = $scope.fields.filter(function (el) { return el.id === param; });
      (aux.length !== 0) ? $scope.selectedField = aux[0] : $scope.selectedField = $scope.fields[0];
    }
    $scope.orders = [{id: 1, name:'recommendedOrder'}, {id: 6, name:'oldestOrder'}, {id: 5, name:'newestOrder'}, {id: 2, name:'costAscendingOrder'},
      {id: 3, name:'costDescendingOrder'}, {id: 4, name:'alphabeticalOrder'}, {id: 7, name:'percentageAscendingOrder'}, {id: 8, name:'percentageDescendingOrder'}];
    param = parseInt($routeParams.o);
    if (isNaN(param)) {
      $scope.selectedOrder = $scope.orders[0];
    } else {
      aux = $scope.orders.filter(function (el) { return el.id === param; });
      (aux.length !== 0) ? $scope.selectedOrder = aux[0] : $scope.selectedOrder = $scope.fields[0];
    }
    $scope.categories = [emptyCategory];
    param = parseInt($routeParams.c);
    $scope.selectedCategory = (isNaN(param)) ? emptyCategory : {id:param, name:'noFilter'};

    param = parseInt($routeParams.min);
    $scope.minCost = (isNaN(param)) ? undefined : param;
    param = parseInt($routeParams.max);
    $scope.maxCost = (isNaN(param)) ? undefined : param;
    if ($scope.minCost != null && $scope.maxCost != null && $scope.minCost > $scope.maxCost) {
      $scope.minCost = undefined; $scope.maxCost = undefined;
    }
    $scope.costRangeError = false;

    /*param = parseInt($routeParams.pmin);
    $scope.minPercentage = (isNaN(param)) ? 0 : param;
    param = parseInt($routeParams.pmax);
    $scope.maxPercentage = (isNaN(param)) ? 100 : param;
    if ($scope.minPercentage > $scope.maxPercentage) {
      $scope.minPercentage = 0; $scope.maxPercentage = 100;
    }
    $scope.percentageRangeError = false;*/
    param = parseInt($routeParams.pmin);
    $scope.minPercentage = (isNaN(param)) ? undefined : param;
    param = parseInt($routeParams.pmax);
    $scope.maxPercentage = (isNaN(param)) ? undefined : param;
    if ($scope.minPercentage != null && $scope.maxPercentage != null && $scope.minPercentage > $scope.maxPercentage) {
      $scope.minPercentage = undefined; $scope.maxPercentage = undefined;
    }
    $scope.percentageRangeError = false;


    projectService.getCategories().then(function (cats) {
      $scope.categories = [emptyCategory].concat(cats.data);
      param = parseInt($routeParams.c);
      if (!isNaN(param)) {
        aux = $scope.categories.filter(function (el) { return el.id === param; });
        $scope.selectedCategory = (aux.length !== 0) ? aux[0] : emptyCategory;
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
      PathService.get().setParamsInUrl({p:$scope.page, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost,
        pmax:$scope.maxPercentage, pmin:$scope.minPercentage, c:$scope.selectedCategory.id});
    };

    this.filterObject = function () {
      var pmin = (!$scope.minPercentage) ? undefined : $scope.minPercentage / 100.0;
      var pmax = (!$scope.maxPercentage || $scope.maxPercentage === 100) ? undefined : $scope.maxPercentage / 100.0;
      return {p:$scope.page, l:pageSize, f:$scope.selectedField.id, o:$scope.selectedOrder.id, s:$scope.searchField, max:$scope.maxCost, min:$scope.minCost,
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
        foot.style.display = 'block';
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
      foot.style.display = 'block';
    };

    $scope.clearFilter = function () {
      $scope.selectedField = $scope.fields[0];
      $scope.selectedOrder = $scope.orders[0];
      $scope.selectedCategory = emptyCategory;
      $scope.minCost = undefined;
      $scope.maxCost = undefined;
      $scope.searchField = undefined;
      $scope.minPercentage = undefined;
      $scope.maxPercentage = undefined;

      $scope.costRangeError = false;
      $scope.percentageRangeError = false;
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
    }

    /*$scope.onSliderChange = function (event) {
      if (event.target.name === 'maxPercentage') {
        if (event.target.value >= $scope.minPercentage) {
          $scope.$apply(function () {
            $scope.maxPercentage = event.target.value;
          });
        } else {
          event.target.value = $scope.maxPercentage;
        }
      } else if (event.target.name === 'minPercentage') {
        if (event.target.value <= $scope.maxPercentage) {
          $scope.$apply(function () {
            $scope.minPercentage = event.target.value;
          });
        } else {
          event.target.value = $scope.minPercentage;
        }
      }
    }*/
  }]);
});