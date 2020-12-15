'use strict';

define(['paw2020a', 'services/projectService', 'services/imageService', 'directives/customOnChange', 'directives/noFloat', 'services/PathService'],
  function(paw2020a) {

    paw2020a.controller('newProjectCtrl',['projectService','imageService', 'PathService', '$scope', function(projectService, imageService, PathService, $scope) {

      var maxImageSize = 2097152, maxSlideshowCount = 5;
      var selectedCategories = [];
      var _this = this;

      $scope.imageSizeError = false; $scope.slideshowSizeError = false;
      $scope.slideshowCountError = false; $scope.categoryCountError = false;
      $scope.serverFormErrors = false;

      projectService.getCategories().then(function (cats) {
        $scope.categories = cats.data;
        $scope.initCategory = $scope.categories[0];
      });

      $scope.fileboxChange = function (event) {
        if (!(event.target.files.length === 0) && event.target.files[0].size >= maxImageSize) {
          event.target.value = null;
          $scope.$apply(function () {
            $scope.imageSizeError = true;
          });
        } else {
          $scope.$apply(function () {
            $scope.imageSizeError = false;
          });
        }
      };

      $scope.multipleFileBoxChange = function (event) {
        var index = 0, error = 0;
        if (event.target.files.length > maxSlideshowCount) {
          error = 1;
          $scope.$apply(function () {
            $scope.slideshowSizeError = false;
            $scope.slideshowCountError = true;
          });
        } else if (!(event.target.files.length === 0)) {
          for (index = 0; index < event.target.files.length; index++) {
            if (event.target.files[index].size >= maxImageSize) {
              error = 1;
              $scope.$apply(function () {
                $scope.slideshowSizeError = true;
                $scope.slideshowCountError = false;
              });
              break;
            }
          }
        }
        if (error === 0) {
          $scope.$apply(function () {
            $scope.slideshowSizeError = false;
            $scope.slideshowCountError = false;
          });
        } else {
          event.target.value = null;
        }
      };

      this.objectFromOption = function (option) {
        return {
          id: parseInt(option.value)
        };
      };

      $scope.addCategory = function () {
        var cat = document.getElementById('all-categories');
        var sel = document.getElementById('final-categories');
        if (cat.selectedIndex !== -1) {
          $scope.categoryCountError = false;
          var op = cat.options[cat.selectedIndex];
          selectedCategories.push(_this.objectFromOption(op));
          sel.appendChild(op);
        }
      };

      $scope.delCategory = function () {
        var cat = document.getElementById('all-categories');
        var sel = document.getElementById('final-categories');
        if (sel.selectedIndex !== -1) {
          var op = sel.options[sel.selectedIndex], opId = _this.objectFromOption(op).id;
          _.remove(selectedCategories, function(n) { return n.id === opId;});
          cat.appendChild(op);
        }
      };

      // TODO: Put image and slideshow
      $scope.createProject = function (project) {
        if (selectedCategories.length === 0) {
          $scope.categoryCountError = true;
          return;
        }
        project.categories = selectedCategories;
        $scope.serverFormErrors = false;
        projectService.create(project).then(function (response) {
          var location = response.headers().location;
          PathService.get().singleProject(location.substring(location.lastIndexOf('/') + 1)).go();
        }, function (errorResponse) {
          if (errorResponse.status === 400) {
            $scope.serverFormErrors = true;
            return;
          }
          console.error(errorResponse);
        });
      };

    }]);

});
