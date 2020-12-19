    'use strict';

define(['paw2020a', 'services/projectService', 'services/sampleService', 'services/messageService', 'services/userService',
  'services/PathService', 'services/AuthenticationService', 'directives/noFloat'], function(paw2020a) {

  paw2020a.controller('chatCtrl', ['projectService', 'sampleService', 'messageService', 'userService', 'PathService', 'AuthenticationService', '$scope', '$routeParams', '$route',
    function(projectService, sampleService, messageService, userService, PathService, AuthenticationService, $scope, $routeParams, $route) {

      var _this = this;
      var projectId = parseInt($routeParams.id1), investorId = parseInt($routeParams.id2);
      var page = 1, maxPage = 1, role, entrepreneur = "Ent", investor = "Inv";
      var oneDayMs = 1000*60*60*24;

      if (AuthenticationService.isInvestor()) role = investor;
      else if (AuthenticationService.isEntrepreneur()) role = entrepreneur;
      else PathService.get().logout().go();

      if (isNaN(projectId) || (isNaN(investorId) && role === entrepreneur)) {
        PathService.get().error().go();
        return;
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
          return;
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

      this.scrollToBottom = function() {
        var element = document.getElementById("chatbox-scroll");
        setTimeout(function(){
          element.scrollTop = element.scrollHeight;
        },50);
      };

      $scope.offerEnabled = false; $scope.responseEnabled = false;

      this.setChatAsSeen = function (lastMessage) {
        if (!lastMessage.incoming || lastMessage.seen) return;
        // TODO: Ver que funque esto
        messageService.setSeen(projectId, investorId).then(function (response) {
          // console.log(response);
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            console.log('Message already seen!');
            return;
          }
          console.error(errorResponse);
        })
      };

      this.handleChatResponse = function (chats) {
        $scope.chats = chats.reverse();
        // Empty chat && Entrepreneur --> 404
        // Empty chat && Investor --> only send new offer
        if (chats.length === 0) {
          if (role === entrepreneur) {
            PathService.get().error().go();
            return;
          } else {
            $scope.responseEnabled = false;
            $scope.offerEnabled = true;
            return;
          }
        }
        var now = new Date();
        $scope.chats.forEach(function (value) {
          if (value.hasOwnProperty('accepted')) {
            value.expInDays = 0;
            value.answered = true;
          } else {
            var aux = Math.ceil((new Date(value.expiryDate) - now) / oneDayMs);
            value.expInDays = (aux <= 0) ? 0 : aux;
            value.answered = false;
          }
          value.incoming = (!value.direction && role === investor) || (value.direction && role === entrepreneur);
        });
        $scope.lastMessage = chats[chats.length - 1];
        this.setChatAsSeen($scope.lastMessage);

        // Last expired --> only send new offer
        // Except you are an investor and last was answered
        if ($scope.lastMessage.expInDays === 0) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = (!$scope.lastMessage.answered) ? true : !$scope.lastMessage.accepted || (role === investor);
          return;
        }
        // Last did not expire, if incoming enable response
        $scope.responseEnabled = !!$scope.lastMessage.incoming;
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
      messageService.getChat(projectId, investorId, page).then(function (response) {
        _this.setMaxPage(response.headers().link);
        _this.handleChatResponse(response.data);
        _this.scrollToBottom();
      }, function (errorResponse) {
        if (errorResponse.status === 403) {
          PathService.get().error().go({code: 403});
          return;
        }
        console.error(errorResponse);
      });

      $scope.rejectOffer = function () {
        messageService.setStatus(projectId, investorId, false).then(function (response) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = true;
          $scope.lastMessage.accepted = false;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            PathService.get().error().go();
            return;
          }
          console.error(errorResponse);
        })
      };

      $scope.acceptOffer = function () {
        messageService.setStatus(projectId, investorId, true).then(function (response) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = (role === investor);
          $scope.lastMessage.accepted = true;
          $scope.project.fundingCurrent += $scope.lastMessage.offer;
          $scope.project.percentage = $scope.project.fundingCurrent * 100 / $scope.project.fundingTarget;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            PathService.get().error().go();
            return;
          }
          console.error(errorResponse);
        })
      };

      this.addOfferToChat = function (offer) {
        var now = new Date();
        offer.publishDate = now.toISOString();
        offer.expiryDate = new Date(now.getTime() + offer.expiryDays * oneDayMs).toISOString();
        offer.expInDays = Math.ceil((new Date(offer.expiryDate) - now) / oneDayMs);
        offer.incoming = false;
        $scope.chats.push(offer);
        $scope.lastMessage = offer;
      };

      $scope.serverFormErrors = false;
      $scope.sendOffer = function (offer) {
        $scope.serverFormErrors = false;
        offer.direction = (role === investor);
        messageService.offer(projectId, investorId, offer).then(function (response) {
          _this.addOfferToChat(offer);
          _this.scrollToBottom();
          $scope.responseEnabled = false;
          $scope.offerEnabled = false;
        }, function (errorResponse) {
          if (errorResponse.status === 400) {
            $route.reload();
            return;
          }
          console.error(errorResponse);
        })
      };

      $scope.viewMoreChat = function () {
        if (page >= maxPage) return;
        page++; $scope.viewMoreEnabled = (page !== maxPage);

        var element = document.getElementById("chatbox-scroll");
        messageService.getChat(projectId, investorId, page).then(function (response) {
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

    }]);
});
