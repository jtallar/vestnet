'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.filter('daysInMonth', function() {
    return function (days, year, month) {
      var filtered = [];
      angular.forEach(days, function (day) {
        if (month !== ""){
          if (month.id === 1 || month.id === 3 || month.id === 5 || month.id === 7 || month.id === 8 || month.id === 10 || month.id === 12) {
            filtered.push(day);
          }
          else if ((month.id === 4 || month.id === 6 || month.id === 9 || month.id === 11) && day <= 30){
            filtered.push(day);
          }
          else if (month.id === 2){
            if (year % 4 === 0 && day <= 29){
              filtered.push(day);
            }
            else if (day <= 28){
              filtered.push(day);
            }
          }
        }
      });
      return filtered;
    };
  });

});
