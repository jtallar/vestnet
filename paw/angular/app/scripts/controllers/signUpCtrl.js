'use strict';
define(['paw2020a', 'services/userService', 'services/locationService', 'services/imageService',
    'directives/customOnChange', 'filters/validMonths', 'filters/daysInMonth', 'filters/validDays', 'services/PathService'],
  function(paw2020a) {
    paw2020a.controller('signUpCtrl', ['userService', 'locationService','imageService', 'PathService', '$scope',
      function(userService, locationService, imageService, PathService, $scope) {
        var emptyElement = {name: "-", id: 0};

        $scope.imageSizeError = false; $scope.serverFormErrors = false;
        $scope.userExistsError = false; $scope.serverRetryError = false;
        $scope.roleSelected = "Investor"; $scope.fileLabel = undefined;
        $scope.countrySelected = emptyElement; $scope.stateSelected = emptyElement;
        $scope.citySelected = emptyElement;

        $scope.loadingCreate = false;

        $scope.countryChange = function () {
          locationService.getStateList($scope.countrySelected.id).then(function (states) {
            if (!states.data || states.data.length === 0) {
              $scope.statesArray = [emptyElement];
              $scope.citiesArray = [emptyElement];
              $scope.citySelected = $scope.citiesArray[0];
              $scope.stateSelected = $scope.statesArray[0];
            } else {
              $scope.statesArray = states.data;
              $scope.stateSelected = $scope.statesArray[0];
              $scope.stateChange();
            }
          });
        };

        $scope.stateChange = function () {
          locationService.getCityList($scope.stateSelected.id).then(function (cities) {
            if (!cities.data || cities.data.length === 0) {
              $scope.citiesArray = [{name: "-", id: 0}];
            } else {
              $scope.citiesArray = cities.data;
            }
            $scope.citySelected = $scope.citiesArray[0];
          });
        };

        locationService.getCountryList().then(function (countries) {
          $scope.countriesArray = countries.data;
          $scope.countrySelected = $scope.countriesArray[0];
          $scope.countryChange();
        });

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

        $scope.updateDate = function (input){
          if (input === "year"){
            $scope.monthSelected = $scope.monthsArray[0];
            $scope.daySelected = $scope.daysArray[0];
          }
          else if (input === "month"){
            $scope.daySelected = $scope.daysArray[0];
          }
        };

        $scope.createUser = function (user) {
          $scope.loadingCreate = true;
          $scope.serverFormErrors = false; $scope.userExistsError = false; $scope.serverRetryError = false;
          user.birthDate = new Date($scope.yearSelected, $scope.monthSelected.id - 1, $scope.daySelected);
          user.countryId = $scope.countrySelected.id;
          user.stateId = $scope.stateSelected.id;
          user.cityId = $scope.citySelected.id;
          user.role = $scope.roleSelected;
          userService.createUser(user).then(function (response) {
            $scope.loadingCreate = false;
            PathService.get().login().go({code: 7});
          }, function (errorResponse) {
            $scope.loadingCreate = false;
            if (errorResponse.status === 400) {
              $scope.serverFormErrors = true;
              return;
            } else if (errorResponse.status === 409) {
              $scope.userExistsError = true;
              return;
            } else if (errorResponse.status === 503) {
              $scope.serverRetryError = true;
              return;
            }
            console.error(errorResponse);
          });
        };
    }]);
});
