'use strict';
define(['paw front'], function(paw front) {

	paw front.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
