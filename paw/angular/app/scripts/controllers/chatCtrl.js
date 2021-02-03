    'use strict';

define(['paw2020a', 'services/projectService', 'services/sampleService', 'services/messageService', 'services/userService',
  'services/PathService', 'services/AuthenticationService', 'directives/noFloat'], function(paw2020a) {

  paw2020a.controller('chatCtrl', ['projectService', 'sampleService', 'messageService', 'userService', 'PathService', 'AuthenticationService', '$scope', '$routeParams', '$route', '$rootScope',
    function(projectService, sampleService, messageService, userService, PathService, AuthenticationService, $scope, $routeParams, $route, $rootScope) {

      $scope.serverRetryError = false;

      var _this = this;
      var projectId = parseInt($routeParams.id1), investorId = parseInt($routeParams.id2);
      var role, entrepreneur = "Ent", investor = "Inv";
      var oneDayMs = 1000*60*60*24;

      if (AuthenticationService.isInvestor()) role = investor;
      else if (AuthenticationService.isEntrepreneur()) role = entrepreneur;
      else PathService.get().logout().go();

      if (isNaN(projectId) || (isNaN(investorId) && role === entrepreneur)) {
        PathService.get().error().replace();
        return;
      }

      $scope.backAction = function() {
        history.back();
      };

      // $scope.toLocaleDateTimeString = function(date) {
      //   var aux;
      //   if(date !== undefined)
      //     aux = new Date(date);
      //   else aux = new Date();
      //   return (aux.toLocaleDateString(navigator.language) + " " + aux.toLocaleTimeString(navigator.language));
      // };

      this.setUser = function (user) {
        $scope.user = user;
        $scope.user.userUrl = PathService.get().user(user.id).path;
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
        $scope.project.projectUrl = PathService.get().singleProject(projectId).path;
        $scope.project.percentage = parseInt($scope.project.fundingCurrent * 100 / $scope.project.fundingTarget);
        $scope.project.portraitExists = false;
        sampleService.get($scope.project.portraitImage).then(function (image) {
          $scope.project.image = image.data.image;
          $scope.project.portraitExists = true;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            return;
          }
          console.error(errorResponse);
        });
        if (role === investor) {
          sampleService.get($scope.project.owner).then(function (user) {
            _this.setUser(user.data);
          }, function (err) {
            console.error(err);
            PathService.get().error().replace();
          })
        }
      }, function (errorResponse) {
        if (errorResponse.status === 404) {
          PathService.get().error().replace();
          return;
        }
        console.error(errorResponse);
      });

      if (role === entrepreneur) {
        userService.getUser(investorId.toString()).then(function (user) {
          _this.setUser(user.data);
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            PathService.get().error().replace();
            return;
          }
          console.error(errorResponse);
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
        messageService.setSeen(projectId, investorId).then(function (response) {
          $rootScope.$emit('notificationChange');
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            // console.log('Message already seen!');
            return;
          }
          console.error(errorResponse);
        })
      };

      this.addInfoToChats = function (chats) {
        var now = new Date();
        chats.forEach(function (value) {
          if (value.hasOwnProperty('accepted')) {
            value.expInDays = 0;
            value.answered = true;
          } else {
            var aux = Math.ceil((new Date(value.expiryDate) - now) / oneDayMs);
            value.expInDays = (aux <= 0) ? 0 : aux;
            value.answered = false;
          }
          value.incoming = (!value.direction && role === investor) || (value.direction && role === entrepreneur);
        })
      };

      this.handleChatResponse = function (chats) {
        $scope.chats = chats.reverse();
        // Empty chat && Entrepreneur --> 404
        // Empty chat && Investor --> only send new offer
        if (chats.length === 0) {
          if (role === entrepreneur) {
            PathService.get().error().replace();
            return;
          } else {
            $scope.responseEnabled = false;
            $scope.offerEnabled = true;
            return;
          }
        }
        this.addInfoToChats($scope.chats);
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

      $scope.nextPageUrl = undefined;
      this.setNextPage = function (linkHeaders) {
        if (!linkHeaders) {
          $scope.nextPageUrl = undefined;
          return;
        }
        var nextLink = linkHeaders.split(',').filter(function (el) { return el.includes('next'); });
        if (isNaN(parseInt(nextLink[0].split('p=')[1][0]))) {
          $scope.nextPageUrl = undefined;
          return;
        }
        $scope.nextPageUrl = nextLink[0].substring(1, nextLink[0].indexOf('>'));
      };

      $scope.chats = [];
      messageService.getChat(projectId, investorId, 1).then(function (response) {
        _this.setNextPage(response.headers().link);
        _this.handleChatResponse(response.data);
        _this.scrollToBottom();
      }, function (errorResponse) {
        if (errorResponse.status === 403) {
          PathService.get().error().replace({code: 403});
          return;
        }
        console.error(errorResponse);
      });

      $scope.rejectOffer = function () {
        $scope.serverRetryError = false;
        messageService.setStatus(projectId, investorId, false).then(function (response) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = true;
          $scope.lastMessage.answered = true;
          $scope.lastMessage.accepted = false;
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            PathService.get().error().go();
            return;
          } else if (errorResponse.status === 503) {
            $scope.serverRetryError = true;
            return;
          }
          console.error(errorResponse);
        })
      };

      $scope.acceptOffer = function () {
        $scope.serverRetryError = false;
        messageService.setStatus(projectId, investorId, true).then(function (response) {
          $scope.responseEnabled = false;
          $scope.offerEnabled = (role === investor);
          $scope.lastMessage.answered = true;
          $scope.lastMessage.accepted = true;
          $scope.project.fundingCurrent += $scope.lastMessage.offer;
          $scope.project.percentage = parseInt($scope.project.fundingCurrent * 100 / $scope.project.fundingTarget);
        }, function (errorResponse) {
          if (errorResponse.status === 404) {
            PathService.get().error().go();
            return;
          } else if (errorResponse.status === 503) {
            $scope.serverRetryError = true;
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

      $scope.sendOffer = function (offer) {
        $scope.serverRetryError = false;
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
          } else if (errorResponse.status === 503) {
            $scope.serverRetryError = true;
            return;
          }
          console.error(errorResponse);
        })
      };

      $scope.viewMoreChat = function () {
        if (!$scope.nextPageUrl) return;

        var element = document.getElementById("chatbox-scroll");
        sampleService.get($scope.nextPageUrl).then(function (response) {
          _this.setNextPage(response.headers().link);
          if (response.data.length === 0) return;
          element.scrollTop = 0;
          response.data.reverse();
          _this.addInfoToChats(response.data);
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
