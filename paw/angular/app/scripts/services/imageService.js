define(['paw2020a', 'restangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('imageService', ['Restangular', function(Restangular) {
    var imageService = {};

    var root = Restangular.one('images');


    imageService.getProfileImage = function(id){
      return root.one(id).get()
    }


    imageService.setProfileImage = function (im) {
      var body = {image: im}
      return root.customPUT(body)
    }


    imageService.getProjectImage = function (projId, index) {

      var object = root.one('projects').one(projId).get()
      object.index = index

      return object
    }


    imageService.setProjectImage = function (projId, im) {

      var body = {image : im}

      return root.one('projects').one(projId).customPUT(body)

    }


    imageService.setProjectSlideshow = function (projId, imgs) {

      var body = {images : imgs}

      return root.one('projects').one(projId).one('slideshow').customPUT(body)

    }


    imageService.getProjectSlideshow = function (projId) {


      return root.one('projects').one(projId).one('slideshow').get()

    }

    return imageService;
  }]);

});
