'use strict';

define(['paw2020a', 'services/projectService', 'services/imageService', 'services/urlService', 'directives/customOnChange', 'directives/noFloat', 'services/PathService'],
  function(paw2020a) {

    paw2020a.controller('editProjectCtrl',['projectService','imageService', 'urlService', 'PathService', '$scope', '$routeParams', function(projectService, imageService, urlService, PathService, $scope, $routeParams) {

      var id = parseInt($routeParams.id);
      if (isNaN(id) || id <= 0) {
        PathService.get().error().replace();
        return;
      }
      var back = !!($routeParams.back);

      $scope.maxImageSizeMB = 2; $scope.maxSlideshowCount = 5;
      var maxImageSize = 2097152; // 2 * 1024 * 1024
      this.selectedCategories = [];
      var portraitImage = undefined, slideshowImages = undefined, existingPortrait = false;
      var _this = this;

      $scope.loading = false; $scope.loadingInfo = true;
      $scope.imageSizeError = false; $scope.slideshowSizeError = false;
      $scope.slideshowCountError = false; $scope.categoryCountError = false;
      $scope.serverFormErrors = false; $scope.disableSlideshow = true;
      $scope.readingSingleFilesCount = 0; $scope.readingMultiFilesCount = 0;
      $scope.editingPortrait = false; $scope.editingSlideshow = false;

      projectService.getCategories().then(function (cats) {
        $scope.categories = cats.data;
        $scope.initCategory = $scope.categories[0];
      });



      projectService.getById(id.toString()).then(function (project) {
        $scope.project = project.data;
        if (!$scope.project.getByOwner) {
          PathService.get().error().replace();
          return;
        }
        // Fill up the rest of the form.
        existingPortrait = false;
        $scope.disableSlideshow = !existingPortrait;
        // Check if portraitImage exists to enable/disable slideshow
        urlService.get($scope.project.portraitImage).then(function (image) {
          existingPortrait = true;
          $scope.disableSlideshow = !existingPortrait;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            return;
          }
          console.error(errorResponse);
        });

        urlService.get($scope.project.categories).then(function (categories) {
          var ids = categories.data.reduce(function (map, obj) {
            map[obj.id] = true;
            return map;
          }, {});
          var cat = document.getElementById('all-categories');
          var sel = document.getElementById('final-categories');
          for (var i = 0; i < cat.options.length; i++){
            if (ids[cat.options[i].value]) {
              _this.selectedCategories.push(_this.objectFromOption(cat.options[i]));
              sel.appendChild(cat.options[i]);
            }
          }
          $scope.loadingInfo = false;
        }, function (errorResponse) {
          console.error(errorResponse);
        });
      }, function (errorResponse) {
        if (errorResponse.status === 404) {
          PathService.get().error().replace();
          return;
        }
        console.error(errorResponse);
      });

      $scope.fileboxChange = function (event) {
        if(event.target.files.length === 0) {
          portraitImage = undefined;
          slideshowImages = undefined;
          $scope.$apply(function () {
            $scope.disableSlideshow = !existingPortrait;
            $scope.readingSingleFilesCount = 0;
          });
          return;
        }
        if (event.target.files[0].size >= maxImageSize) {
          event.target.value = null;
          portraitImage = undefined;
          slideshowImages = undefined;
          $scope.$apply(function () {
            $scope.imageSizeError = true;
            $scope.disableSlideshow = !existingPortrait;
            $scope.readingSingleFilesCount = 0;
          });
        } else {
          var r = new FileReader();
          r.onloadend = function (ev) {
            portraitImage = ev.target.result;
            $scope.$apply(function () {
              $scope.readingSingleFilesCount--;
            });
          };
          r.readAsArrayBuffer(event.target.files[0]);
          $scope.$apply(function () {
            $scope.imageSizeError = false;
            $scope.disableSlideshow = false;
            $scope.readingSingleFilesCount++;
          });
        }
      };

      $scope.editingPortraitClick = function () {
        var sel = document.getElementById('customFileProjectPic');
        $scope.editingPortrait = !$scope.editingPortrait;
        if (!$scope.editingPortrait) {
          portraitImage = undefined;
          sel.value = "";
        }
      };

      this.readMultipleFiles = function(files) {
        var reader = new FileReader();
        function readFile(index) {
          if( index >= files.length ) return;
          var file = files[index];
          reader.onload = function(ev) {
            slideshowImages.push(ev.target.result);
            $scope.$apply(function () {
              $scope.readingMultiFilesCount--;
            });
            readFile(index+1)
          };
          reader.readAsArrayBuffer(file);
        }
        readFile(0);
      };

      $scope.multipleFileBoxChange = function (event) {
        var index = 0, error = 0;
        if (event.target.files.length > $scope.maxSlideshowCount) {
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
          slideshowImages = [];
          _this.readMultipleFiles(event.target.files);
          $scope.$apply(function () {
            $scope.slideshowSizeError = false;
            $scope.slideshowCountError = false;
            $scope.readingMultiFilesCount += event.target.files.length;
          });
        } else {
          event.target.value = null;
          slideshowImages = undefined;
          $scope.$apply(function () {
            $scope.readingMultiFilesCount = 0;
          });
        }
      };

      $scope.editingSlideshowClick = function () {
        var sel = document.getElementById('customMultipleFileProjectPic');
        $scope.editingSlideshow = !$scope.editingSlideshow;
        if (!$scope.editingSlideshow) {
          slideshowImages = undefined;
          sel.value = "";
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
          _this.selectedCategories.push(_this.objectFromOption(op));
          sel.appendChild(op);
        }
      };

      $scope.delCategory = function () {
        var cat = document.getElementById('all-categories');
        var sel = document.getElementById('final-categories');
        if (sel.selectedIndex !== -1) {
          var op = sel.options[sel.selectedIndex], opId = _this.objectFromOption(op).id;
          _.remove(_this.selectedCategories, function(n) { return n.id === opId;});
          cat.appendChild(op);
        }
      };

      this.finishCreation = function(projectId, params) {
        if (back) {
          if (!params) params = {back:true};
          else params.back = true;
        }
        PathService.get().singleProject(projectId).replace(params);
      };

      this.sendSlideshow = function (projectId) {
        if (!!(slideshowImages)) {
          imageService.setProjectSlideshow(projectId, slideshowImages).then(function (imageResponse) {
            _this.finishCreation(projectId);
          }, function (imageErrorResponse) {
            console.error(imageErrorResponse);
            _this.finishCreation(projectId, {imageError: true});
          });
        } else {
          this.finishCreation(projectId);
        }
      };

      this.sendPortrait = function (projectId) {
        if (!!(portraitImage)) {
          imageService.setProjectImage(projectId, portraitImage).then(function (imageResponse) {
            _this.sendSlideshow(projectId);
          }, function (imageErrorResponse) {
            console.error(imageErrorResponse);
            _this.finishCreation(projectId, {imageError: true});
          });
        } else {
          this.sendSlideshow(projectId);
        }
      };

      $scope.updateProject = function (project) {
        $scope.loading = true;
        if (_this.selectedCategories.length === 0) {
          $scope.categoryCountError = true;
          $scope.loading = false;
          return;
        }
        project.categories = _this.selectedCategories;
        $scope.serverFormErrors = false;
        projectService.update(project).then(function (response) {
          _this.sendPortrait(project.id.toString());
        }, function (errorResponse) {
          if (errorResponse.status === 400) {
            $scope.serverFormErrors = true;
            $scope.loading = false;
            return;
          } else if (errorResponse.status === 404) {
            PathService.get().error().go();
            return;
          }
          $scope.loading = false;
          console.error(errorResponse);
        });
      };

      $scope.backAction = function() {
        history.back();
      };

    }]);

  });
