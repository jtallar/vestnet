'use strict';

// Taken from
// https://stackoverflow.com/questions/34013311/text-input-allow-only-integer-input-in-angularjs
define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('noFloat', function() {
    return {
      restrict: 'A',
      link: function (scope, element, attrs, ctrl) {
        element.on('keydown', function (event) {
          if ([110, 190].indexOf(event.which) > -1) {
            // dot and numpad dot
            event.preventDefault();
            return false;
          }
          else{
            return true;
          }
        });
      }
    };
  });

});
