define(['paw2020a', 'restangular', 'services/AuthenticationService', 'services/PathService'], function(paw2020a) {
    'use strict';
    paw2020a.service('AuthenticatedRestangular', ['Restangular', 'AuthenticationService', 'PathService', '$timeout', '$route', '$http', '$rootScope', function(Restangular, AuthenticationService, PathService, $timeout, $route, $http, $rootScope) {
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
              response.config.headers.Authorization = AuthenticationService.getHeaderContent();
              $http(response.config).then(deferred.resolve, deferred.reject);
              return false;
            }, function (errorResponse) {
              AuthenticationService.logout();
              $rootScope.$emit('credentialsChanged');
              $route.reload();
              return true;
            });
            return false;
          }
          return true;
        });
      })
        
    }]);

});
