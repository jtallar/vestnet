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
        var routeMatches = function (route, routeRE) {
          for (var i = 0; i < routeRE.length; i++) {
            if (routeRE[i].test(route.split('?')[0])) return true;
          }
          return false;
        };

        $rootScope.$on('$routeChangeStart', function (event, next, current) {
          var logged = AuthenticationService.isLoggedIn(), url = $location.url();
          var notAuthUrl = routeMatches(url, PathService.noAuthRoutesRE);

          $rootScope.showHeader = {value: !notAuthUrl};

          if (logged && notAuthUrl) {
            // if logged in and trying to access no auth routes, redirect to projects
            PathService.get().projects().go();
          } else if (!logged && routeMatches(url, PathService.authRoutesRE)) {
            // if not logged in and trying to access auth routes, redirect to login
            PathService.get().login().go();
          } else if ((!AuthenticationService.isInvestor() && routeMatches(url, PathService.investorRoutesRE)) ||
              (!AuthenticationService.isEntrepreneur() && routeMatches(url, PathService.entrepreneurRoutesRE))) {
            // if logged in and trying to access something they shouldnt, redirect to 403
            PathService.get().error().go({code:403});
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
            // PathService.get().error().go({code:404});
            console.error("404 que tenes que manejar");
            return true;
          } else if (response.status === 403) {
            if (AuthenticationService.isLoggedIn()) {
              PathService.get().error().go({code:403});
            } else {
              PathService.get().login().go();
            }
            return false;
          }
          return true;
        });
      }]);


    paw2020a.run(['$route', '$rootScope', '$location', function ($route, $rootScope, $location) {
      var original = $location.path;
      $location.pathReload = function (path, reload) {
        if (reload === false) {
          var lastRoute = $route.current;
          var un = $rootScope.$on('$locationChangeSuccess', function () {
            $route.current = lastRoute;
            un();
          });
        }
        return original.apply($location, [path]);
      };
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
				'RestangularProvider',
				function($routeProvider, $locationProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider, RestangularProvider) {
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
				}]);

		return paw2020a;
	}
);
