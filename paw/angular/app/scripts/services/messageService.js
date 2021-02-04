define(['paw2020a', 'services/AuthenticatedRestangular'], function(paw2020a) {

  'use strict';
  paw2020a.service('messageService', ['AuthenticatedRestangular', function(AuthenticatedRestangular) {
    var messageService = {};
    
    var root = AuthenticatedRestangular.one('messages');
    
    
    messageService.offer = function (projectId, investorId, offerBody) {
      if (isNaN(investorId)) return root.one(projectId.toString()).customPOST(offerBody);     // role === Investor
      return root.one(projectId.toString()).one(investorId.toString()).customPOST(offerBody); // role === Entrepreneur
    };


    messageService.getOffers = function (off,acc, page) {
      var params = {a: acc, p: page};
      return root.one('project').one(off).get(params)

    };

    messageService.setStatus = function (projectId, investorId, accepted) {
      if (!accepted) accepted = false;
      var body = {accepted: accepted};
      if (isNaN(investorId)) return root.one('status').one(projectId.toString()).customPUT(body);     // role === Investor
      return root.one('status').one(projectId.toString()).one(investorId.toString()).customPUT(body); // role === Entrepreneur
    };

    messageService.setSeen = function (projectId, investorId) {
      if (isNaN(investorId)) return root.one('seen').one(projectId.toString()).customPUT();     // role === Investor
      return root.one('seen').one(projectId.toString()).one(investorId.toString()).customPUT(); // role === Entrepreneur
    };

    messageService.unread = function (projId,last) {
      var param = {last: last};

      return root.one('unread').one(projId).get(param)
    };

    messageService.projectNotificationCount = function (projectId) {
      return root.one('notifications').one('project').one(projectId.toString()).get();
    };

    messageService.notificationCount = function () {
      return root.one('notifications').get();
    };

    messageService.getChat = function(projectId, investorId, pageNum) {
      if (!pageNum) pageNum = 1;
      if (isNaN(investorId)) return root.one('chat').one(projectId.toString()).get({p: pageNum});     // role === Investor
      return root.one('chat').one(projectId.toString()).one(investorId.toString()).get({p: pageNum}); // role === Entrepreneur
    };

    messageService.getInvestorChatList = function (page) {
      if (!page) page = 1;
      return root.one('investor').get({a: false, p: page});
    };

    messageService.getInvestorDeals = function (page) {
      if (!page) page = 1;
      return root.one('investor').get({a: true, p: page}); // role === Investor
    };

    messageService.getInvestedAmount = function () {
      return root.one('invested').get();
    };

    return messageService;
  }]);

});
