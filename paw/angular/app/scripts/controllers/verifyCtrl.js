    'use strict';

define(['paw2020a', 'services/userService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('verifyCtrl', ['userService', 'PathService', '$scope', '$routeParams', function(userService, PathService, $scope, $routeParams) {
      var token = $routeParams.token;
      if (token === undefined) {
        PathService.get().login().go({code: 3});
        return;
      }

      userService.verifyPassword(token).then(function () {
        PathService.get().login().go({code: 8});
      }, function (errorResponse) {
        if (errorResponse.status === 400) {
          PathService.get().login().go({code: 4});
          return;
        }
        console.error(errorResponse);
      });

    }]);

});
