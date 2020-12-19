    'use strict';

define(['paw2020a', 'services/projectService', 'services/sampleService', 'services/messageService', 'services/userService',
  'services/PathService', 'services/AuthenticationService', 'directives/noFloat'], function(paw2020a) {

  paw2020a.controller('chatCtrl', ['projectService', 'sampleService', 'messageService', 'userService', 'PathService', 'AuthenticationService', '$scope', '$routeParams',
    function(projectService, sampleService, messageService, userService, PathService, AuthenticationService, $scope, $routeParams) {

      var _this = this;
      var projectId = parseInt($routeParams.id1), investorId = parseInt($routeParams.id2);
      var page = 1, maxPage = 1, role, entrepreneur = "Ent", investor = "Inv";
      var oneDayMs = 1000*60*60*24;

      if (AuthenticationService.isInvestor()) role = investor;
      else if (AuthenticationService.isEntrepreneur()) role = entrepreneur;
      else PathService.get().logout().go();

      if (isNaN(projectId) || isNaN(investorId)) {
        PathService.get().error().go();
      }

      $scope.sent = true;    // if the mail was sent retreive from url
      $scope.backAction = function() {
        if (this.sent) {
          history.back();
        } else {
          history.back();
          history.back();
        }
      };

      this.setUser = function (user) {
        $scope.user = user;
        if ($scope.user.imageExists) {
          sampleService.get($scope.user.image).then(function (image) {
            $scope.user.image = image.data.image;
          }, function (err) {
            console.log("No image")
          });
        }
      };

      projectService.getById(projectId.toString()).then(function (response) {
        $scope.project = response.data;
        $scope.project.percentage = $scope.project.fundingCurrent * 100 / $scope.project.fundingTarget;
        if ($scope.project.portraitExists) {
          sampleService.get($scope.project.portraitImage).then(function (image) {
            $scope.project.image = image.data.image;
          }, function (err) {
            console.log("No image")
          });
        }
        if (role === investor) {
          sampleService.get($scope.project.owner).then(function (user) {
            _this.setUser(user.data);
          }, function (err) {
            console.error(err);
            PathService.get().error().go();
          })
        }
      }, function (errorResponse) {
        if (errorResponse.status === 404) {
          PathService.get().error().go();
        }
        console.error(errorResponse);
      });

      if (role === entrepreneur) {
        userService.getUser(investorId.toString()).then(function (user) {
          _this.setUser(user.data);
        }, function (err) {
          console.error(err);
          PathService.get().error().go();
        })
      }

      $scope.offerEnabled = false; $scope.responseEnabled = false;

      this.handleChatResponse = function (chats) {
        console.log(chats);
        $scope.chats = chats.reverse();
        // Empty chat && Entrepreneur --> 404
        if (chats.length === 0 && role === entrepreneur) {
          PathService.get().error().go();
          return;
        }
        var now = new Date();
        $scope.chats.forEach(function (value) {
          var aux = Math.ceil((new Date(value.expiryDate) - now) / oneDayMs);
          value.expInDays = (aux <= 0) ? 0 : aux;
          value.incoming = (!value.direction && role === investor) || (value.direction && role === entrepreneur);
        });
        var lastMessage = chats[chats.length - 1];
        $scope.lastMessageDate = lastMessage.publishDate;
        // (Empty chat && Investor) || Last expired --> only send new offer
        if (chats.length === 0 || lastMessage.expInDays === 0) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = true;
          return;
        }
        // Last did not expire, if incoming enable response
        $scope.responseEnabled = !!lastMessage.incoming;
        $scope.offerEnabled = false;
      };

      $scope.viewMoreEnabled = false;
      this.setMaxPage = function (linkHeaders) {
        var lastLink = linkHeaders.split(',').filter(function (el) { return el.includes('last'); });
        maxPage = parseInt(lastLink[0].split('p=')[1][0]);
        if (isNaN(maxPage)) maxPage = page;
        $scope.viewMoreEnabled = (page !== maxPage);
      };

      $scope.chats = [];
      messageService.getChat(projectId.toString(), investorId.toString(), page).then(function (response) {
        _this.setMaxPage(response.headers().link);
        _this.handleChatResponse(response.data);
      }, function (errorResponse) {
        if (errorResponse.status === 403) {
          PathService.get().error().go({code: 403});
          return;
        }
        console.error(errorResponse);
      });

      // TODO: DO accept, reject + correct send offer + testing
      $scope.rejectOffer = function () {
        console.log("rejecting offer");
        $scope.offerEnabled = true;
      };

      $scope.acceptOffer = function () {
        console.log("Accepting offer");
        // Vamos al dashboard??
      };

      this.addOfferToChat = function (offer) {
        var now = new Date();
        offer.publishDate = now.toISOString();
        offer.expiryDate = (now + offer.expiryDays * oneDayMs).toISOString();
        $scope.chats.push(offer);
      };

      $scope.serverFormErrors = false;
      $scope.sendOffer = function (offer) {
        $scope.serverFormErrors = false;
        offer.projectId = projectId;
        offer.investorId = investorId;
        offer.ownerId = (role === entrepreneur) ? AuthenticationService.getUserId() : $scope.user.id;
        offer.direction = (role === investor);
        console.log(offer);
        messageService.offer(offer).then(function (response) {
          _this.addOfferToChat(offer);
          $scope.responseEnabled = false;
          $scope.offerEnabled = false;
        }, function (errorResponse) {
          if (errorResponse.status === 400) {
            $scope.serverFormErrors = true;
            return;
          }
          console.error(errorResponse);
        })
        // vamos al dashboard?
      };

      $scope.viewMoreChat = function () {
        if (page >= maxPage) return;
        page++; $scope.viewMoreEnabled = (page !== maxPage);

        var element = document.getElementById("chatbox-scroll");
        messageService.getChat(projectId.toString(), investorId.toString(), page).then(function (response) {
          if (response.data.length === 0) return;
          element.scrollTop = 0;
          response.data.reverse();
          response.data.forEach(function (msg) { $scope.chats.unshift(msg) });
        }, function (errorResponse) {
          if (errorResponse.status === 403) {
            PathService.get().error().go({code: 403});
            return;
          }
          console.error(errorResponse);
        });
      };

      $scope.$on('$viewContentLoaded', function() {
        var element = document.getElementById("chatbox-scroll");
        setTimeout(function(){
          element.scrollTop = element.scrollHeight;
          },50);
      });

      // var myDiv = document.getElementById("chatbox-scroll");
      // myDiv.scrollTop = myDiv.scrollHeight;

      }]);
});
