'use strict';
define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

	paw2020a.service('urlService',['AuthenticatedRestangular', function(AuthenticatedRestangular) {

	  var urlService = {};
	  
	  urlService.get = function (absURL, routeName) {
	    if (!routeName) routeName = 'routeName';
      return AuthenticatedRestangular.oneUrl(routeName, absURL).get();
    };

    return urlService

	}]);
});
