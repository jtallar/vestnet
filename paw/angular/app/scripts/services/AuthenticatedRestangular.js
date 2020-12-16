define(['paw2020a', 'restangular', 'services/AuthenticationService', 'services/PathService'], function(paw2020a) {
    'use strict';
    paw2020a.service('AuthenticatedRestangular', ['Restangular', 'AuthenticationService', 'PathService', '$log', '$timeout', '$route', function(Restangular, AuthenticationService, PathService, $log, $timeout, $route) {
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
              // TODO: Probar si esto funca bien
              $route.reload();
            }, function (errorResponse) {
              $timeout(function () {
                PathService.get().login().go();
              }, 0);
            });
            return false;
          }
          return true;
        });
      })
        
    }]);

});
