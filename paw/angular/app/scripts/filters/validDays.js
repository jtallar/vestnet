'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.filter('validDays', function() {
    return function (days, year, month) {
      var filtered = [];
      var now = new Date();
      var over18Day = now.getUTCDate();
      var over18Month = now.getUTCMonth() + 1;
      var over18Year = now.getUTCFullYear() - 18;
      if(year === over18Year && month.id === over18Month){
        angular.forEach(days, function (day) {
          if (day <= over18Day) {
            filtered.push(day);
          }
        });
      }
      else{
        angular.forEach(days, function (day) {
          filtered.push(day);
        });
      }
      return filtered;
    };
  });

});
