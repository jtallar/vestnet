'use strict';
define(['paw2020a', 'restangular'], function(paw2020a) {

	paw2020a.service('sampleService',['Restangular', function(Restangular) {

	  var sampleService = {}
	  
	  sampleService.get = function (absURL) {
      return Restangular.oneUrl('routeName', absURL).get()
    }

    return sampleService

	}]);
});
