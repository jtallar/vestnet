'use strict';
define(['paw2020a'], function(paw2020a) {

	paw2020a.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
