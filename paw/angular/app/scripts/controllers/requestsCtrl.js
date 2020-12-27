    'use strict';

define(['paw2020a', 'services/messageService', 'services/projectService', 'services/sampleService', 'services/PathService', 'directives/pagination'], function(paw2020a) {
    paw2020a.controller('requestsCtrl', ['messageService','projectService', 'sampleService', 'PathService', '$scope', '$routeParams', function(messageService, projectService, sampleService, PathService, $scope, $routeParams) {

      var _this = this;
      $scope.noDealsFound = false;

      var param = parseInt($routeParams.p);
      if (isNaN(param) || param <= 0) param = 1;
      $scope.page = param; $scope.lastPage = param;

      // Cannot use scope, too many changes to digest
      this.animate = function (id, start, end, duration) {
        if (start === end) return;
        var range = end - start;
        var current = start;
        var increment = end/500;
        var stepTime = Math.abs(Math.floor(duration / range));
        var obj = document.getElementById(id);
        var timer = setInterval(function() {
          current += increment;
          obj.innerHTML = parseInt(current);
          if (current >= end) {
            clearInterval(timer);
            obj.innerHTML = end;
          }
        }, stepTime);
      };

      $scope.messages = [];

      // TODO: Que pasa si son varias paginas? Voy a mostrar solo el tope de esta pagina.
      //  Deberia traerme un dato aparte?
      this.updateCounter = function () {
        var total = 0;
        $scope.messages.forEach(function (msg){
          total += msg.offer;
        });
        this.animate("invested", 0, total, 5000);
      };

      this.setMaxPage = function (linkHeaders) {
        var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
        var maxPage = parseInt(lastLink[0].split('p=')[1][0]);
        if (isNaN(maxPage)) maxPage = page;
        $scope.lastPage = maxPage;
      };

      this.processMessages = function (messages) {
        $scope.messages = messages;
        var map = {};
        for(var i = 0; i < $scope.messages.length; i++) {
          map[$scope.messages[i].id] = i;
          $scope.messages[i].ownerUrl = PathService.get().user($scope.messages[i].ownerId).path;
          // TODO: Ver si con un cambio en el back puedo ahorrarme esta mamushka de llamadas
          sampleService.get($scope.messages[i].project, $scope.messages[i].id.toString()).then(function (project) {
            $scope.messages[map[project.data.route]].projectName = project.data.name;
            $scope.messages[map[project.data.route]].projectUrl = PathService.get().singleProject(project.data.id).path;
            $scope.messages[map[project.data.route]].projectPortraitExists = project.data.portraitExists;
            if (project.data.portraitExists) {
              sampleService.get(project.data.portraitImage, project.data.route).then(function (image) {
                $scope.messages[map[image.data.route]].projectImage = image.data.image;
              }, function (err) {
                console.log("No image")
              });
            }
          }, function (err) {
            console.log("No project found");
          });
        }
      };

      this.fetchDeals = function () {
        messageService.getInvestorDeals($scope.page).then(function (response) {
          if (response.data.length === 0) {
            $scope.noDealsFound = true;
            $scope.messages = [];
            return;
          }
          _this.setMaxPage(response.headers().link);
          _this.processMessages(response.data);
          _this.updateCounter();
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      };
      this.fetchDeals();

      $scope.getToPage = function (page) {
        $scope.page = page;
        PathService.get().setParamsInUrl({p:$scope.page});
        _this.fetchDeals();
      }

    }]);

});
