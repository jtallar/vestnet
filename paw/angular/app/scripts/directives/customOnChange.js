'use strict';

// Taken from
// https://stackoverflow.com/questions/17922557/angularjs-how-to-check-for-changes-in-file-input-fields#comment43502160_17923521
define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('customOnChange', function() {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var onChangeHandler = scope.$eval(attrs.customOnChange);
        element.on('change', onChangeHandler);
        element.on('$destroy', function () {
          element.off();
        });
      }
    };
  });

});
