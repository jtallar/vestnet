define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('locationService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var locationService = {};

    var root = AuthenticatedRestangular.all('location');

    locationService.getCountryList = function () {
      return root.all('country').getList();
    };


    locationService.getStateList = function (countryId) {
      return root.all('state').all(countryId).getList();
    };


    locationService.getCityList = function (stateId) {
      return root.all('city').all(stateId).getList();
    };


    return locationService;
  }]);

});
