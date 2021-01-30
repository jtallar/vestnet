'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('vnfooter', function() {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      templateUrl: 'views/directives/footer.html',
      controller: function ($scope) {

      }
    };
  });

});
