'use strict';
define(['routes',
	'services/dependencyResolverFor',
  'services/PathService',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate'],
	function(config, dependencyResolverFor, i18n) {
		var paw2020a = angular.module('paw2020a', [
			'ngRoute',
			'restangular',
			'pascalprecht.translate'
		]);

		// Configure authentication filter, restangular interceptors
    // Based on code taken from:
    // https://arthur.gonigberg.com/2013/06/29/angularjs-role-based-auth/
    // https://stackoverflow.com/questions/24088610/restangular-spinner
    // https://stackoverflow.com/questions/28010548/restangular-how-to-override-error-interceptors
    /*paw2020a.run(['$rootScope', '$location', 'Restangular', 'PathService',
      function ($rootScope, $location, Restangular, PathService) {
        var routesWithNoAuth = [PathService.get().login().path];

        var routeClean = function (route) {
          return _.find(routesWithNoAuth,
            function (noAuthRoute) {
              return _.str.startsWith(route, noAuthRoute);
            });
        };

        // TODO: Uncomment when AuthenticationService is done
        /!*$rootScope.$on('$routeChangeStart', function (event, next, current) {
          // if logged in and trying to access login, redirect to home
          if (Authentication.isLoggedIn() && ($location.url() === Paths.get().login().absolutePath())) {
            Paths.get().go();
          }

          // if route requires auth and user is not logged in
          if (!routeClean($location.url()) && !AuthenticationService.isLoggedIn()) {
              // redirect back to login
              PathService.get().login().go();
          }
        });*!/

        var requestsInProgress = 0;

        Restangular.addRequestInterceptor(function (element, operation, what, url) {
          if (requestsInProgress === 0) {
            $rootScope.loading = true;
          }
          requestsInProgress++;
          return element;
        });

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
            PathService.get().notFound().go();
            return false;
          }
          return true;
        });
      }]);*/

		paw2020a
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				'RestangularProvider',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider,
                 RestangularProvider) {

					paw2020a.controller = $controllerProvider.register;
					paw2020a.directive = $compileProvider.directive;
					paw2020a.filter = $filterProvider.register;
					paw2020a.factory = $provide.factory;
					paw2020a.service = $provide.service;

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							$routeProvider.when(path, {templateUrl: route.templateUrl, resolve: dependencyResolverFor(['controllers/' + route.controller]), controller: route.controller, gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}

					$translateProvider.translations('preferredLanguage', i18n);
					$translateProvider.preferredLanguage('preferredLanguage');

					RestangularProvider.setBaseUrl('api/v1/');
				}]);

		return paw2020a;
	}
);
