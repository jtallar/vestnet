define(['paw2020a', 'restangular', 'services/AuthenticationService', 'services/PathService'], function(paw2020a) {
    'use strict';
    paw2020a.service('AuthenticatedRestangular', ['Restangular', 'AuthenticationService', 'PathService', '$log', '$timeout', function(Restangular, AuthenticationService, PathService, $log, $timeout) {
      return Restangular.withConfig(function (RestangularConfigurer) {
        // cannot use RestangularConfigurer.setDefaultHeaders(), it is called once and before token set
        // This requests the token on each request, when this instance is made token should already be set
        RestangularConfigurer.addFullRequestInterceptor(function (element, operation, route, url, headers, params, httpConfig) {
          return {
            headers: _.extend(headers, AuthenticationService.getHeader()),
            params: params,
            element: element,
            httpConfig: httpConfig
          }
        });

        RestangularConfigurer.setErrorInterceptor(function (response, deferred, responseHandler) {
          if (response.status === 401) {
            AuthenticationService.refresh().then(function () {
              console.log('post refresh');
              // TODO: Should retry operation or reload this part, but not working
              // Restangular.all().customOperation(response.config.method, response.config.url);
              // $scope.$apply(); // Should not be using scope in service
              // if (!$rootScope.$$phase) $rootScope.$apply();
              // $window.location.reload();
              // $timeout(function () {
              //   PathService.get().reload();
              // }, 0);
            }, function (errorResponse) {
              console.log('post refresh - error');
              $timeout(function () {
                PathService.get().login().go();
              }, 0);
            });
            return false;
          } else if (response.status === 403) {
            if (AuthenticationService.isLoggedIn()) {
              console.log('logged in, but unauthorized');
              // Forbidden entrance, should show 404 or forbidden page
              return true;
            } else {
              console.log('redirecting to login');
              PathService.get().login().go();
              return false;
            }
          }
          // 404 should show 404 error page, should be default behaviour
          return true;
        })
      })
        
    }]);

});
