'use strict';
define(['routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
  'services/PathService',
  'services/AuthenticationService',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate'],
	function(config, dependencyResolverFor, i18n, pathService, authService) {
		var paw2020a = angular.module('paw2020a', [
			'ngRoute',
			'restangular',
			'pascalprecht.translate'
		]);

    paw2020a.service('PathService', ['$location', function ($location) {
      return pathService($location);
    }]);

    paw2020a.service('AuthenticationService', ['Restangular', function (Restangular) {
      return authService(Restangular);
    }]);

    // Configure authentication filter, restangular interceptors
    // Based on code taken from:
    // https://arthur.gonigberg.com/2013/06/29/angularjs-role-based-auth/
    // https://stackoverflow.com/questions/24088610/restangular-spinner
    // https://stackoverflow.com/questions/28010548/restangular-how-to-override-error-interceptors
    paw2020a.run(['$rootScope', '$location', 'Restangular', 'PathService', 'AuthenticationService',
      function ($rootScope, $location, Restangular, PathService, AuthenticationService) {
      // TODO: Ver por que empty y / no son lo mismo, como compararlos como igual
        var routesWithNoAuth = [PathService.get().login().path, PathService.get().index().path];

        var routeClean = function (route) {
          return _.find(routesWithNoAuth,
            function (noAuthRoute) {
              console.log(route + ' comp to ' + noAuthRoute);
              return route === noAuthRoute;
            });
        };

        // TODO: Ver bien como queda esto
        $rootScope.$on('$routeChangeStart', function (event, next, current) {
          // if logged in and trying to access login, redirect to home
          if (AuthenticationService.isLoggedIn() && ($location.url() === PathService.get().login().absolutePath())) {
            // PathService.get().go();
            console.log('User already logged in');
          }

          // if route requires auth and user is not logged in
          if (!routeClean($location.url()) && !AuthenticationService.isLoggedIn()) {
              // redirect back to login
              // PathService.get().login().go();
          }
        });

        var requestsInProgress = 0;

        Restangular.addRequestInterceptor(function (element, operation, what, url) {
          if (requestsInProgress === 0) {
            $rootScope.loading = true;
          }
          requestsInProgress++;
          return element;
        });

        // TODO: Ver si esto arruina alguna response
        Restangular.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
          requestsInProgress--;
          if (requestsInProgress === 0) {
            $rootScope.loading = false;
          }
          return data;
        });

        // TODO: Add all error codes wanted
        Restangular.setErrorInterceptor(function (response, deferred, responseHandler) {
          if (response.status === 404) {
            // PathService.get().notFound().go();
            console.error("404 que tenes que manejar");
            return true;
          } else if (response.status === 403) {
            if (AuthenticationService.isLoggedIn()) {
              PathService.get().forbidden().go();
            } else {
              PathService.get().login().go();
            }
            return false;
          }
          return true;
        });
      }]);

		paw2020a
			.config(
				['$routeProvider',
				'$locationProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
        '$httpProvider',
        'RestangularProvider',
				function($routeProvider, $locationProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, $httpProvider, RestangularProvider) {
          $locationProvider.hashPrefix('');
					paw2020a.controller = $controllerProvider.register;
					paw2020a.directive = $compileProvider.directive;
					paw2020a.filter = $filterProvider.register;
					paw2020a.factory = $provide.factory;
					paw2020a.service = $provide.service;

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							$routeProvider.when(path, {
							  templateUrl: route.templateUrl,
                resolve: dependencyResolverFor(
                  ['controllers/' + route.controller]),
                controller: route.controller,
                gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}

					$translateProvider.translations('preferredLanguage', i18n);
					$translateProvider.preferredLanguage('preferredLanguage');
					$translateProvider.useSanitizeValueStrategy('escape');

          // TODO: Uncomment in production, comment the one below
					// RestangularProvider.setBaseUrl('api/v1/');
          RestangularProvider.setBaseUrl('http://localhost:8080/api/v1/');

          // TODO: Siempre que interpretamos la response como response nomas, solo datos, habria que cambiarlo a response.data
          RestangularProvider.setFullResponse(true);

          $httpProvider.defaults.headers.common["Accept-Language"] = navigator.language;
				}]);

		return paw2020a;
	}
);
