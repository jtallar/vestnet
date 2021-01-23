'use strict';

// TODO: Ver de agregar la badge tanto afuera en el icono de mensajes (/notifications/project/{project_id}?) como dentro de un chat.
define(['paw2020a', 'directives/toggle',  'services/projectService', 'services/messageService', 'services/userService', 'services/sampleService', 'services/imageService', 'services/PathService'], function(paw2020a) {
  paw2020a.controller('dashboardCtrl', ['projectService', 'messageService','userService','sampleService','imageService', 'PathService', '$scope', '$rootScope', function(projectService, messageService,userService,sampleService,imageService, PathService, $scope, $rootScope) {

    var _this = this;

    $scope.loadingProjects = true; $scope.loadingFunded = true;

    $scope.messages = []
    $scope.fundedMsgs = []

    var map = {};
    userService.getLoggedProjects(false).then(function (response) {
      $scope.projects = response.data;
      $scope.loadingProjects = false;
      for(var i = 0; i < $scope.projects.length; i++) {
        map[$scope.projects[i].id] = i;
        $scope.projects[i].msgPage = 1;
        $scope.projects[i].offerPage = 1;
        $scope.projects[i].firstFecthed = false;
        $scope.projects[i].firstFecthedOffers = false;
        $scope.projects[i].editUrl= PathService.get().editProject($scope.projects[i].id).path;
        if ($scope.projects[i].portraitExists) {
          sampleService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
            $scope.projects[map[image.data.route]].image = image.data.image;
          }, function (err) {
            console.log("No image");
          });
        }
      }
    })




    userService.getLoggedProjects(true).then(function (response) {
      $scope.fundedProjects = response.data;
      $scope.loadingFunded = false;
      var map = {};
      for(var i = 0; i < $scope.fundedProjects.length; i++) {
        map[$scope.fundedProjects[i].id] = i;
        $scope.fundedProjects[i].editUrl= PathService.get().editProject($scope.fundedProjects[i].id).path;
        if ($scope.fundedProjects[i].portraitExists) {
          sampleService.get($scope.fundedProjects[i].portraitImage, $scope.fundedProjects[i].id.toString()).then(function (image) {
            $scope.fundedProjects[map[image.data.route]].image = image.data.image
          }, function (err) {
            console.log("No image")
          });
        }
      }
    })



    var size = 3;   // it does not get me length of projects we have to fetch it from services later


    var myID = 1; //session

    // $scope.messages = [
    //   [{'investor': 'Gabriel','projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1}, {'investor': 'Mario','projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}],
    //   [{'investor': 'Gloria','projectId': 2,'body': 'como va', 'offer': 80000, 'request': 'el auto', 'senderId': 10}, {'investor': 'Miriam','projectId': 2,'body': 'feqefewq', 'offer': 500, 'request': 'la moto', 'senderId': 3}],
    //   []
    // ];




    // projectService.getById(myID.toString()).then(function (projectsApi) {
    //   console.log(projectsApi)
    //   _this.projects = projectsApi
    //   _this.state = new Array(_this.projects.length).fill(0);
    // })

      // _this.messages = [[{'projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1}, {'projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}],
      //   [{'projectId': 2,'body': 'como va', 'offer': 80000, 'request': 'el auto', 'senderId': 10}, {'projectId': 2,'body': 'feqefewq', 'offer': 500, 'request': 'la moto', 'senderId': 3}], []];

      $scope.answer = function (projectId, senderId, accepted) {
          console.log(projectId);
          console.log(senderId);
      };

    this.updateNextPageOffers = function (linkHeaders, index) {
      var nextLink = linkHeaders.split(',').filter(function (el) {
        return el.includes('next');
      });
      $scope.projects[index].nextPageOffer = parseInt(nextLink[0].split('p=')[1][0]);
    };

    $scope.fetchOffers = function(id, index){
      if($scope.projects[index].firstFecthedOffers == false) {
        $scope.projects[index].firstFecthedOffers = true
        $scope.fundedMsgs[index] = []
        messageService.getOffers(id.toString(), true, 1).then(function (response) {
          _this.updateNextPageOffers(response.headers().link, index);
          var messageMap = []
          console.log(response)
          for (var i = 0; i < response.data.length; i++) {
            messageMap[response.data[i].investorId] = i
            $scope.fundedMsgs[index][i] = {
              'senderId': response.data[i].investorId,
              'body': '',
              'offer': response.data[i].offer,
              'request': response.data[i].request,
              'investor': '',
              'exchange': response.data[i].exchange
            }


            sampleService.get(response.data[i].investor, i.toString()).then(function (inv) {
              $scope.fundedMsgs[index][parseInt(inv.data.route)].investor = inv.data.firstName
              $scope.fundedMsgs[index][parseInt(inv.data.route)].lastName = inv.data.lastName
            }, function (error) {
              console.log(error)
            })
          }
        }, function (error) {
          console.log(error)
        })
      }
    };

    this.updateNextPageMessages = function (linkHeaders, index) {
      var nextLink = linkHeaders.split(',').filter(function (el) {
        return el.includes('next');
      });
      $scope.projects[index].nextPageMessages = parseInt(nextLink[0].split('p=')[1][0]);
    };

      $scope.fetchMessage = function(id, index){
      if($scope.projects[index].firstFecthed == false) {
          $scope.projects[index].firstFecthed = true
          $scope.messages[index] = []
          messageService.getOffers(id.toString(), false, 1).then(function (response) {
            _this.updateNextPageMessages(response.headers().link, index);
            var messageMap = []
            for (var i = 0; i < response.data.length; i++) {
              messageMap[response.data[i].investorId] = i;
              // TODO: Mostrar mas que el body
              $scope.messages[index][i] = {
                'senderId': response.data[i].investorId,
                'body': response.data[i].comment,
                'offer': '',
                'request': '',
                'investor': '',
                'chatUrl': PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path,
                'seen': response.data[i].seen
              };
              // TODO: ver si se puede evitar este request.
              sampleService.get(response.data[i].investor, i.toString()).then(function (inv) {
                $scope.messages[index][inv.data.route].investor = inv.data.firstName
                $scope.messages[index][inv.data.route].lastName = inv.data.lastName
              }, function (error) {
                console.log(error)
              })
            }
          }, function (error) {
            console.log(error)
          })
       }
      }


      $scope.fetchStats = function(id, index){

        projectService.getStats(id.toString()).then(function (response) {
          $scope.projects[index].clicksAvg = response.data.clicksAvg
          $scope.projects[index].secondsAvg = response.data.secondsAvg
          $scope.projects[index].seen = response.data.seen
          $scope.projects[index].contactClicks = response.data.contactClicks
          $scope.projects[index].investorsSeen = response.data.investorsSeen
          $scope.projects[index].lastSeen = response.data.lastSeen
        })
      }




      $scope.enabled = true;
      $scope.onOff = false;
      $scope.funded = false;
      $scope.disabled = true;



      $scope.viewMore = function (id, index) {
        var length = $scope.messages[index].length
        $scope.projects[index].msgPage += 1
        var messageMap = []
        messageService.getOffers(id.toString(), false, $scope.projects[index].msgPage).then(function (response) {
          _this.updateNextPageMessages(response.headers().link, index);
          for (var i = 0; i < response.data.length; i++) {
            messageMap[response.data[i].investorId] = length + i;
            // TODO: Mostrar mas que el body
            $scope.messages[index][i + length] = {
              'senderId':response.data[i].investorId,
              'body': response.data[i].comment,
              'offer': '',
              'request' : '',
              'investor': '',
              'chatUrl': PathService.get().chat($scope.projects[index].id, response.data[i].investorId).path,
              'seen': response.data[i].seen
            };
            // TODO: ver si se puede evitar este request.
            sampleService.get(response.data[i].investor, response.data[i].investorId).then(function (inv) {
              $scope.messages[index][messageMap[inv.data.route]].investor = inv.data.firstName
              $scope.messages[index][messageMap[inv.data.route]].lastName = inv.data.lastName
            }, function (error) {
              console.log(error)
            })
          }
        }, function (error) {
          console.log(error)
        })
      }


    $scope.viewMoreOffers = function (id, index) {
      var length = $scope.fundedMsgs[index].length
      $scope.projects[index].offerPage += 1
      var messageMap = []
      messageService.getOffers(id.toString(), true, $scope.projects[index].offerPage).then(function (response) {
        _this.updateNextPageOffers(response.headers().link, index);
        for (var i = 0; i < response.data.length; i++) {
          messageMap[response.data[i].investorId] = length + i
          $scope.fundedMsgs[index][i + length] = {
            'senderId':response.data[i].investorId,
            'body': '',
            'offer': response.data[i].offer,
            'request' : '',
            'investor': '',
            'exchange': response.data[i].exchange
          }
          sampleService.get(response.data[i].investor, response.data[i].investorId).then(function (inv) {
            $scope.fundedMsgs[index][messageMap[inv.data.route]].investor = inv.data.firstName
            $scope.fundedMsgs[index][messageMap[inv.data.route]].lastName = inv.data.lastName
          }, function (error) {
            console.log(error)
          })
        }
      }, function (error) {
        console.log(error)
      })
    }

    $scope.getList = function () {
      if($scope.funded === true) return $scope.fundedProjects;
      else return $scope.projects;
    };

    $scope.isLoading = function () {
      if($scope.funded === true) return $scope.loadingFunded;
      else return $scope.loadingProjects;
    };

    $scope.goToChat = function (message) {
      if (!message.seen) $rootScope.$emit('messageRead');
      PathService.get().setFullUrl(message.chatUrl).go();
    }

    }]);
});
