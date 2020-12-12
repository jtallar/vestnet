define(['paw2020a', 'restangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('messageService', ['Restangular', function(Restangular) {
    var messageService = {};
    
    var root = Restangular.one('messages');
    
    
    messageService.offer = function (pId, sId, cont, c, off) {
      var body = {content: cont, cost: c, offers: off, projId: pId, senderId: sId} //cannot send receiver id need a new api call to do this or maybe i do have it inside the object

      return root.customPOST(body)
    }


    messageService.getOffers = function (off, page) {
      var params = {offers: off, p: page}
      return root.get(params)

    }

    messageService.setStatus = function (accepted, projId, senderId) {
      var body = {accepted: accepted, projId : projId, senderId: senderId}
      return root.one('status').customPUT(body)
    }


    messageService.unread = function (projId,last) {
      var param = {last: last}

      return root.one('unread').one(projId).get(param)
    }
    

    return messageService;
  }]);

});
