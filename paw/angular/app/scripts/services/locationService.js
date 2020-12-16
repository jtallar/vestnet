define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('locationService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var locationService = {};

    var root = AuthenticatedRestangular.one('location');

    locationService.getCountryList = function () {

      return root.one('country').get();

    };


    locationService.getStateList = function (countryId) {
      return root.one('state').one(countryId).get();

    };


    locationService.getCityList = function (stateId) {
      return root.one('city').one(stateId).get();
    };


    return locationService;
  }]);

});
