'use strict';

define(['paw2020a', 'directives/toggle',  'services/projectService', 'services/messageService', 'services/userService', 'services/AuthenticationService', 'services/sampleService', 'services/imageService'], function(paw2020a) {
  paw2020a.controller('dashboardCtrl', ['projectService', 'messageService','userService','AuthenticationService','sampleService','imageService','$scope', function(projectService, messageService,userService,AuthenticationService,sampleService,imageService, $scope) {


    $scope.id = AuthenticationService.getUserId()
    $scope.messages = []

    var map = {};
    userService.getUserProjects($scope.id, false).then(function (response) {
      $scope.projects = response.data
      for(var i = 0; i < $scope.projects.length; i++) {
        map[$scope.projects[i].id] = i;
        if ($scope.projects[i].portraitExists) {
          sampleService.get($scope.projects[i].portraitImage, $scope.projects[i].id.toString()).then(function (image) {
            $scope.projects[map[image.data.route]].image = image.data.image
          }, function (err) {
            console.log("No image")
          });
        }
      }
    })




    userService.getUserProjects($scope.id, true).then(function (response) {
      $scope.fundedProjects = response.data
      var map = {};
      for(var i = 0; i < $scope.fundedProjects.length; i++) {
        map[$scope.fundedProjects[i].id] = i;
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


      $scope.fetchMessage = function(id, index){
        $scope.messages[index] = []
        console.log(index)
        messageService.getOffers(id.toString(), 'false', 1).then(function (response) {
          console.log(response.data)
          var messageMap = []
          for (var i = 0; i < response.data.length; i++) {
            messageMap[response.data[i].investorId] = i
            $scope.messages[index][i] = {
              'senderId':response.data[i].investorId,
              'body': '',
              'offer': '',
              'request' : '',
              'investor': ''
            }
            sampleService.get(response.data[i].chat, response.data[i].investorId).then(function (chat) {
              console.log(chat)
              $scope.messages[index][messageMap[chat.data.route]].body = chat.data[chat.data.length - 1].comment
            }, function (error) {
              console.log(error)
            })

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




      $scope.enabled = true;
      $scope.onOff = false;
      $scope.funded = false;
      $scope.disabled = true;

      // $scope.fetchMsgs = function(projectId, index) {
      //   messageService.unread(projectId.toString(), false).then(function (msgsApi) {
      //     _this.messages[index] = msgsApi
      //   })
      // }

      $scope.extraMessages = [
        {'investor': 'Kiko','projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1},
        {'investor': 'Lolo','projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}
        ];

      $scope.viewMore = function (id) {
        console.log($scope.messages[id]);
        $scope.extraMessages.forEach(function(msg){
          $scope.messages[id].push(msg)
        });
      }

      $scope.getList = function () {
        if($scope.funded === true) return $scope.fundedProjects;
        else return $scope.projects;
      }

    }]);
});
