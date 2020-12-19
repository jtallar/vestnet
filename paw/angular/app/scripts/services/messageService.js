define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('messageService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var messageService = {};
    
    var root = AuthenticatedRestangular.one('messages');
    
    
    messageService.offer = function (offerBody) {
      return root.customPOST(offerBody);
    };


    messageService.getOffers = function (off,acc, page) {
      var params = {a: acc, p: page};
      return root.one('project').one(off).get(params)

    };

    messageService.setStatus = function (projectId, investorId, accepted) {
      if (!accepted) accepted = false;
      var body = {accepted: accepted};
      return root.one('status').one(projectId).one(investorId).customPUT(body);
    };

    messageService.setSeen = function (projectId, investorId) {
      return root.one('seen').one(projectId).one(investorId).customPUT();
    };

    messageService.unread = function (projId,last) {
      var param = {last: last};

      return root.one('unread').one(projId).get(param)
    };

    messageService.notificationCount = function () {
      return root.one('notifications').get();
    };

    messageService.getChat = function(projectId, investorId, pageNum) {
      if (!pageNum) pageNum = 1;
      return root.one('chat').one(projectId).one(investorId).get({p: pageNum});
    };

    return messageService;
  }]);

});
