'use strict';
define(['paw2020a', 'services/userService', 'services/locationService', 'services/urlService',
    'directives/customOnChange', 'filters/validMonths', 'filters/daysInMonth', 'filters/validDays', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('editProfileCtrl', ['userService', 'locationService','urlService', 'PathService', '$scope',
      function(userService, locationService, urlService, PathService, $scope) {
        $scope.serverFormErrors = false;
        $scope.loadingUpdate = false; $scope.loadingInfo = true;

        // Based on https://codepen.io/1bb/pen/QZajBg?editors=1010
        this.initializeDate = function() {
          $scope.daysArray = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31];
          $scope.monthsArray = [{id: 1, name:"January"},
            {id: 2, name:"February"},
            {id: 3, name:"March"},
            {id: 4, name:"April"},
            {id: 5, name:"May"},
            {id: 6, name:"June"},
            {id: 7, name:"July"},
            {id: 8, name:"August"},
            {id: 9, name:"September"},
            {id: 10, name:"October"},
            {id: 11, name:"November"},
            {id: 12, name:"December"}
          ];
          $scope.yearsArray = [];
          var d = new Date();
          for (var i = (d.getFullYear() - 18); i > (d.getFullYear() - 100); i--) {
            $scope.yearsArray.push(i);
          }
          $scope.daySelected = $scope.daysArray[0]; $scope.monthSelected = $scope.monthsArray[0];
          $scope.yearSelected = $scope.yearsArray[0];
        };
        this.initializeDate();

        userService.getLoggedUser().then(function (userApi) {
          $scope.user = userApi.data;
          urlService.get($scope.user.location).then(function (response) {
            $scope.user.country = response.data.country;
            $scope.user.city = response.data.city;
            $scope.user.state = response.data.state;
            $scope.loadingInfo = false;
          }, function (errorResponse) {
            $scope.loadingInfo = false;
            // 404 should never happen
            console.error(errorResponse);
          });
          var date = new Date($scope.user.birthDate);
          $scope.yearSelected = date.getFullYear();
          $scope.monthSelected = $scope.monthsArray.filter(function (value) { return value.id - 1 === date.getMonth(); })[0];
          $scope.daySelected = date.getDate();
        }, function (errorReponse) {
          $scope.loadingInfo = false;
          // Should never throw 404
          console.error(errorReponse);
        });

        $scope.updateDate = function (input){
          if (input === "year"){
            $scope.monthSelected = $scope.monthsArray[0];
            $scope.daySelected = $scope.daysArray[0];
          }
          else if (input === "month"){
            $scope.daySelected = $scope.daysArray[0];
          }
        };

        $scope.updateUser = function (user) {
          $scope.serverFormErrors = false; $scope.loadingUpdate = true;
          user.birthDate = new Date($scope.yearSelected, $scope.monthSelected.id - 1, $scope.daySelected);
          userService.updateUser(user).then(function (response) {
            $scope.loadingUpdate = false;
            PathService.get().profile().replace();
          }, function (errorResponse) {
            $scope.loadingUpdate = false;
            if (errorResponse.status === 400) {
              $scope.serverFormErrors = true;
              return;
            }
            // 404 should never happen
            console.error(errorResponse);
          });
        };
    }]);
});
