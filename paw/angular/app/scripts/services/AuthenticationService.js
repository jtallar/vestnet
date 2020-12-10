'use strict';

define([], function() {
  // This function should be called on paw2020a module initialization
  // It is defined because it will be used before module acquisition by requireJS
  // Based on code from
  // https://stackoverflow.com/questions/42021076/how-to-implement-remember-me-feature-in-feathers-js-and-jwt
  return function(Restangular) {
    var authService = {};
    var tokenKey = 'token_id', rememberKey = 'remember_id';
    var shouldPersist = localStorage.getItem(rememberKey) === 't';

    var rest = Restangular.withConfig(function(RestangularConfigurer) {
      RestangularConfigurer.addResponseInterceptor(
        function(data, operation, what, url, response, deferred) {
          return response.headers()['Authorization'];
        }
      );
    });

    authService.setShouldPersist = function(persist) {
      shouldPersist = !! persist;
      if (persist) {
        localStorage.setItem(rememberKey, 't');
      } else {
        localStorage.removeItem(rememberKey);
      }
    };

    authService.getToken = function() {
      if (shouldPersist) {
        return localStorage.getItem(tokenKey);
      }
      return sessionStorage.getItem(tokenKey);
    };

    authService.setToken = function(token) {
      if (shouldPersist) {
        return localStorage.setItem(tokenKey, token);
      }
      return sessionStorage.setItem(tokenKey, token);
    };

    authService.logout = function () {

      if (shouldPersist) {
        return localStorage.removeItem(tokenKey);
      }
      return sessionStorage.removeItem(tokenKey);
    };

    authService.getHeader = function () {
      return {'Authorization': 'Bearer ' + authService.getToken()};
    };

    authService.login = function (user) {
      console.log(user);
      return rest.one('auth').one('login').post(user);
    };

    authService.isLoggedIn = function () {
      // Evaluates as false if undefined or null, in this case null when not logged in
      return authService.getToken();
    };

    return authService;
  };
});
