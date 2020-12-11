define(['paw2020a', 'restangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('userService', ['Restangular', function(Restangular) {
    var userService = {};

    var root = Restangular.one('users');

    userService.createUser = function (fName, lName,rId,ph,em,rl,pass,ln, bday,count, st, c) { //password solve

      var body = {firstName: fName, lastName: lName, realId: rId, phone: ph, email:em, role:rl, password: pass, linkedin: ln, birthday:bday, country:count, state:st, city: c}
      return root.customPOST(body)

    }


    userService.updateUser = function (fName, lName,rId,ph,em,rl,ln, bday,count, st, c) {
      var body = {firstName: fName, lastName: lName, realId: rId, phone: ph, email:em, role:rl, password: pass, linkedin: ln, birthday:bday, country:count, state:st, city: c}
      return root.customPUT(body)
    }


    userService.deleteUser = function () {
      return root.delete()
    }

    userService.getUser = function (id) {
      return root.one(id).get()
    }


    userService.getUserProjects = function(id, fund){

      var param = {funded: fund}
      return root.one(id).get(param)
    }


    userService.getFavorites  = function () {
      return root.one('favorites').get()
    }

    userService.password = function (pass) {
      var body = {password: pass}
      return root.one('password').customPOST(body)
    }

    userService.verifyPassword = function (pass) {
      var body = {password: pass}
      return root.one('verify').customPOST(body)
    }

    //put password must be done through mail



    return userService;
  }]);

});
