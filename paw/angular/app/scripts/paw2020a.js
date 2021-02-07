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

        var credentialChange = function () {
          var logged = AuthenticationService.isLoggedIn(),
            investor = AuthenticationService.isInvestor(),
            entrep = AuthenticationService.isEntrepreneur();

          if (logged && investor) {
            $rootScope.role = {value: 0};
          } else if (logged && entrep) {
            $rootScope.role = {value: 1};
          } else {
            $rootScope.role = {value: 2};
          }
        };
        credentialChange();

        $rootScope.$on('$routeChangeStart', function (event, next, current) {
          var logged = AuthenticationService.isLoggedIn(), url = $location.url();
          var notAuthUrl = routeMatches(url, PathService.noAuthRoutesRE), logoutUrl = routeMatches(url, PathService.logoutRE);

          $rootScope.showHeader = {value: !(notAuthUrl || logoutUrl)};

          if ((logged && $rootScope.role.value === 2) || (!logged && $rootScope.role.value !== 2)) {
            $rootScope.$emit('credentialsChanged');
          }

          if (logged && notAuthUrl) {
            // if logged in and trying to access no auth routes, redirect to projects
            $rootScope.showHeader = {value: true};
            PathService.get().projects().go();
          } else if (!logged && logoutUrl) {
            PathService.get().index().go();
          } else if (!logged && routeMatches(url, PathService.authRoutesRE)) {
            // if not logged in and trying to access auth routes, redirect to login
            PathService.get().login().go({url: url});
          } else if ((!AuthenticationService.isInvestor() && routeMatches(url, PathService.investorRoutesRE)) ||
              (!AuthenticationService.isEntrepreneur() && routeMatches(url, PathService.entrepreneurRoutesRE))) {
            // if logged in and trying to access something they shouldnt, redirect to error
            PathService.get().error().replace({code:403});
          }
        });

        $rootScope.$on('credentialsChanged', function (event) {
          credentialChange();
        });

        var requestsInProgress = 0;

        Restangular.addRequestInterceptor(function (element, operation, what, url) {
          if (requestsInProgress === 0) {
            $rootScope.rootScopeLoading = true;
          }
          requestsInProgress++;
          return element;
        });

        Restangular.addResponseInterceptor(function (data, operation, what, url, response, deferred) {
          requestsInProgress--;
          if (requestsInProgress === 0) {
            $rootScope.rootScopeLoading = false;
          }
          return data;
        });

        Restangular.setErrorInterceptor(function (response, deferred, responseHandler) {
          if (response.status === 404) {
            // PathService.get().error().go({code:404});
            // console.error("404 que tenes que manejar");
            return true;
          } else if (response.status === 403) {
            if (AuthenticationService.isLoggedIn()) {
              PathService.get().logout().replace();
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

    // Define general usage functions + form restrictions in rootScope
    paw2020a.run(['$rootScope', function ($rootScope) {
      $rootScope.formatPrice = function(number) {
        var formatter = new Intl.NumberFormat(navigator.language, { style: 'currency', currency: 'USD', minimumFractionDigits: 0 });
        return formatter.format(number);
      };
      $rootScope.toLocaleDateString = function(date) {
        var aux;
        if(date !== undefined)
          aux = new Date(date);
        else aux = new Date();
        return (aux.toLocaleDateString(navigator.language));
      };
      $rootScope.toLocaleDateTimeString = function(date) {
        var aux;
        if(date !== undefined)
          aux = new Date(date);
        else aux = new Date();
        return (aux.toLocaleDateString(navigator.language) + " " + aux.toLocaleTimeString(navigator.language));
      };

      $rootScope.safari = function() {
        var ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('safari') !== -1) {
          if (ua.indexOf('chrome') > -1) {
            // console.log("Chrome"); // Chrome
            return false;
          } else {
            // console.log("Safari"); // Safari
            return true;
          }
        }
      };

      $rootScope.firstNameMaxLength = 25; $rootScope.lastNameMaxLength = 25;
      $rootScope.realIdMaxLength = 15; $rootScope.phoneMaxLength = 25;
      $rootScope.emailMaxLength = 255; $rootScope.passwordMaxLength = 50;
      $rootScope.linkedinMaxLength = 100;

      $rootScope.projectNameMinLength = 5; $rootScope.projectNameMaxLength = 50;
      $rootScope.projectSummaryMinLength = 30; $rootScope.projectSummaryMaxLength = 250;
      $rootScope.projectFundingMin = 1000; $rootScope.projectFundingMax = 2000000000;
      $rootScope.feedSearchMaxLength = 50;

      $rootScope.offerOfferMin = 100; $rootScope.offerOfferMax = 1000000000;
      $rootScope.offerExchangeMaxLength = 100; $rootScope.offerCommentMaxLength = 250;
      $rootScope.offerExpiresMin = 1; $rootScope.offerExpiresMax = 31;
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
					RestangularProvider.setBaseUrl('api/');
          // RestangularProvider.setBaseUrl('http://localhost:8080/api/');

          RestangularProvider.setFullResponse(true);

          $httpProvider.defaults.headers.common["Accept-Language"] = navigator.language;
				}]);

		return paw2020a;
	}
);
