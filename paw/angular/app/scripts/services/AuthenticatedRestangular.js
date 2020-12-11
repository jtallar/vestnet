define(['paw2020a', 'restangular', 'services/AuthenticationService'], function(paw2020a) {
    'use strict';
    paw2020a.service('AuthenticatedRestangular', ['Restangular', 'AuthenticationService', function(Restangular, AuthenticationService) {
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
        })
      })
        
    }]);

});
