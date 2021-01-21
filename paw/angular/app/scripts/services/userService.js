define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('userService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var userService = {};

    var root = AuthenticatedRestangular.one('users');

    userService.createUser = function (user) {
      return root.customPOST(user);
    };


    userService.updateUser = function (fName, lName,rId,ph,em,rl,ln, bday,count, st, c) {
      var body = {firstName: fName, lastName: lName, realId: rId, phone: ph, email:em, role:rl, password: pass, linkedin: ln, birthday:bday, country:count, state:st, city: c};
      return root.customPUT(body)
    };


    userService.deleteUser = function () {
      return root.delete()
    };

    userService.getUser = function (id) {
      return root.one(id).get()
    };


    userService.getUserProjects = function(id, fund){

      var param = {funded: fund};
      return root.one(id).one('projects').get(param)
    };


    userService.getFavorites  = function () {
      return root.one('favorites').get()
    };

    userService.getProfileFavorites  = function () {
      return root.one('favorites').one('profile').get()
    };


    userService.putFavorite = function (id, add) {
      var body = {projectId : id, add: add}
      return root.one('favorites').customPUT(body)
    }

    userService.requestPassword = function (mail) {
      var body = {mail: mail};
      return root.one('password').customPOST(body);
    };

    userService.verifyPassword = function (token) {
      var body = {token: token};
      return root.one('verify').customPUT(body)
    };

    userService.resetPassword = function (passwordBlock) {
      return root.one('password').customPUT(passwordBlock);
    };

    return userService;
  }]);



});
