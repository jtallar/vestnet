'use strict';
define(['paw2020a', 'services/userService', 'services/locationService', 'services/imageService',
    'directives/customOnChange', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('signUpCtrl', ['userService', 'locationService','imageService', 'PathService', '$scope',
      function(userService, locationService, imageService, PathService, $scope) {
        var maxImageSize = 2097152;

        $scope.imageSizeError = false; $scope.serverFormErrors = false;

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

        // TODO: Put image
        $scope.createUser = function (user) {
          // Custom validations
          $scope.serverFormErrors = false;
          /*projectService.create(project).then(function (response) {
            var location = response.headers().location;
            PathService.get().singleProject(location.substring(location.lastIndexOf('/') + 1)).go();
          }, function (errorResponse) {
            if (errorResponse.status === 400) {
              $scope.serverFormErrors = true;
              return;
            }
            console.error(errorResponse);
          });*/
        };
    }]);
});
