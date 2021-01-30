'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.filter('validMonths', function() {
    return function (months, year) {
      var filtered = [];
      var now = new Date();
      var over18Month = now.getUTCMonth() + 1;
      var over18Year = now.getUTCFullYear() - 18;
      if(year !== ""){
        if(year === over18Year){
          angular.forEach(months, function (month) {
            if (month.id <= over18Month) {
              filtered.push(month);
            }
          });
        }
        else{
          angular.forEach(months, function (month) {
            filtered.push(month);
          });
        }
      }
      return filtered;
    };
  });

});
