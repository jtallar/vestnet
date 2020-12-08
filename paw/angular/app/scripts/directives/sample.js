'use strict';
define(['VestNet'], function(VestNet) {

	VestNet.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
