define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('imageService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var imageService = {};

    var root = AuthenticatedRestangular.one('images');


    imageService.getProfileImage = function(id){
      return root.one('users').one(id).get()
    };


    imageService.setProfileImage = function (im) {
      var body = {image: im};
      return root.one('users').customPUT(body)
    };


    imageService.getProjectImage = function (projId) {

      return root.one('projects').one(projId).get();
    };


    imageService.setProjectImage = function (projId, im) {

      var body = {image : im};

      return root.one('projects').one(projId).customPUT(body)

    };


    imageService.setProjectSlideshow = function (projId, imgs) {

      var body = {images : imgs};

      return root.one('projects').one(projId).one('slideshow').customPUT(body)

    };


    imageService.getProjectSlideshow = function (projId) {


      return root.one('projects').one(projId).one('slideshow').get()

    };

    return imageService;
  }]);

});
