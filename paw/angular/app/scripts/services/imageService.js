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

    imageService.blobToBase64 = function (blob) {
      return btoa(new Uint8Array(blob).reduce(function (data, byte) {
        return data + String.fromCharCode(byte);
      }, ''));
    };

    imageService.setProjectImage = function (projId, imageArrayBuffer) {
      var body = {image : imageService.blobToBase64(imageArrayBuffer)};
      return root.one('projects').one(projId).customPUT(body)
    };


    imageService.setProjectSlideshow = function (projId, imgs) {
      imgs = imgs.map(function (elem) {
        return {image: imageService.blobToBase64(elem)}
      });
      var body = {slideshow : imgs};
      return root.one('projects').one(projId).one('slideshow').customPUT(body)
    };


    imageService.getProjectSlideshow = function (projId) {


      return root.one('projects').one(projId).one('slideshow').get()

    };

    return imageService;
  }]);

});
