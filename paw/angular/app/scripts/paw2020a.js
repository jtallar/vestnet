'use strict';
define(['routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate'],
	function(config, dependencyResolverFor, i18n) {
		var paw2020a = angular.module('paw2020a', [
			'ngRoute',
			'pascalprecht.translate'
		]);
		paw2020a
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider) {

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
				}]);

    // define Paths service
    // paw2020a.service('Paths', [
    //   '$location', '$log',
    //   function($location, $log) {
    //     return pathsService($location, $log);
    //   }
    // ]);

		return paw2020a;
	}
);
