'use strict';

define([], function() {
  // This function should be called on paw2020a module initialization
  // It is defined because it will be used before module acquisition by requireJS
  // Based on code from
  // https://stackoverflow.com/questions/42021076/how-to-implement-remember-me-feature-in-feathers-js-and-jwt
  return function(Restangular) {
    var authService = {};
    var accessTokenKey = 'c091fd4c2ff1277a66e2c0bff4d3efb11e89c1f3',
        refreshTokenKey = '68da4ce1c82076ff4749c2cfde06a4b51956b140',
        rememberKey = 'd4e9a0f8f4609bb34d2aee6486444db48044e4f9';
    // TODO: Check si es correcto almacenar esto en el localStorage/sessionStorage
    var entrepreneurKey = '85f61433041f941dbf9cda260b0a82f9cccfc1d4',
        investorKey = '1d8e6dc7a8959ecab4b983da1f5be041ec44ffc6';
    var shouldPersist = localStorage.getItem(rememberKey) === 't';

    var rest = Restangular.withConfig(function(RestangularConfigurer) {
      RestangularConfigurer.addResponseInterceptor(
        function(data, operation, what, url, response, deferred) {
          var now = new Date();
          authService.setShouldPersist(shouldPersist);
          authService.setToken(JSON.stringify({
            value: data.accessToken,
            expiry: now.getTime() + data.accessMinutes * 60000
          }), false);
          authService.setToken(JSON.stringify({
            value: data.refreshToken,
            expiry: now.getTime() + data.refreshMinutes * 60000
          }), true);
          if (data.roles.includes('ROLE_ENTREPRENEUR')) authService.setRole(false);
          if (data.roles.includes('ROLE_INVESTOR')) authService.setRole(true);
          console.log("Logged");
          return true;
        }
      );
    });

    authService.setStorage = function (key, value) {
      if (shouldPersist) {
        return localStorage.setItem(key, value);
      }
      return sessionStorage.setItem(key, value);
    };

    authService.getStorage = function (key) {
      if (shouldPersist) {
        return localStorage.getItem(key);
      }
      return sessionStorage.getItem(key);
    };

    authService.setShouldPersist = function(persist) {
      shouldPersist = !! persist;
      if (persist) {
        localStorage.setItem(rememberKey, 't');
      } else {
        localStorage.removeItem(rememberKey);
      }
    };

    authService.getToken = function(refresh) {
      var key = (refresh) ? refreshTokenKey : accessTokenKey;
      var token, now = new Date();
      token = JSON.parse(authService.getStorage(key));
      if (!token) return null;
      if (refresh && now.getTime() > token.expiry) { // Only remove old refresh tokens
        authService.removeToken(refresh);
        return null;
      }
      return token.value;
    };

    authService.setToken = function(token, refresh) {
      var key = (refresh) ? refreshTokenKey : accessTokenKey;
      return authService.setStorage(key, token);
    };

    authService.removeToken = function (refresh) {
      var key = (refresh) ? refreshTokenKey : accessTokenKey;
      if (shouldPersist) {
        return localStorage.removeItem(key);
      }
      return sessionStorage.removeItem(key);
    };

    authService.logout = function () {

      if (shouldPersist) {
        localStorage.removeItem(entrepreneurKey);
        localStorage.removeItem(investorKey);
        localStorage.removeItem(refreshTokenKey);
        return localStorage.removeItem(accessTokenKey);
      }
      sessionStorage.removeItem(entrepreneurKey);
      sessionStorage.removeItem(investorKey);
      sessionStorage.removeItem(refreshTokenKey);
      return sessionStorage.removeItem(accessTokenKey);
    };

    authService.getHeaderContent = function (refresh) {
      return 'Bearer ' + authService.getToken(refresh);
    };

    authService.getHeader = function (refresh) {
      return {'Authorization': authService.getHeaderContent(refresh)};
    };

    authService.login = function (user) {
      shouldPersist = !!(user.rememberMe);
      return rest.one('auth').one('login').customPOST(user);
    };

    authService.isLoggedIn = function () {
      // Evaluates as false if undefined or null, in this case null when not logged in
      return !!(authService.getToken(true));
    };

    authService.refresh = function () {
      var token = authService.getToken(true);
      if (!token) return Promise.reject();
      return rest.one('auth').one('refresh').get({}, authService.getHeader(true));
    };

    authService.setRole = function(investor) {
      var key = (investor) ? investorKey : entrepreneurKey;
      return authService.setStorage(key, 't');
    };

    authService.getRole = function(investor) {
      var key = (investor) ? investorKey : entrepreneurKey;
      return authService.getStorage(key);
    };

    authService.isInvestor = function () {
      return authService.getRole(true) === 't';
    };

    authService.isEntrepreneur = function () {
      return authService.getRole(false) === 't';
    };

    return authService;
  };
});
