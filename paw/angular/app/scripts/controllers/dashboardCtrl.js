    'use strict';

define(['paw2020a',  'services/projectService', 'services/messageService'], function(paw2020a) {

  paw2020a.controller('dashboardCtrl',['projectService', 'messageService', function(projectService, messageService) {


    var _this = this
    var filter = {
      page : '',
      order: '',
      field: '',
      keyWord: '',
      maxCost: '',
      minCost: '',
      cat: ''
    }

    _this.messages = []

    var myID = 1 //session

    projectService.getById(myID.toString()).then(function (projectsApi) {
      console.log(projectsApi)
      _this.projects = projectsApi

      _this.state = new Array(_this.projects.length).fill(0);
    })

      // $scope.projects = [
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1,
      //     'funded': false,
      //     'hits': 6,
      //     'msgCount': 2
      //   },
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 2,
      //     'funded': false,
      //     'hits': 6,
      //     'msgCount': 2
      //   },
      //   {
      //     'name': 'Vestnet',
      //     'cost': 100000,
      //     'image': 'images/projectNoImage.png',
      //     'summary': 'Es una página que tiene como objetivo aumentar la cantidad de inversiones en el país',
      //     'id': 1,
      //     'funded': false,
      //     'hits': 3,
      //     'msgCount': 0
      //   }
      // ];



      // _this.messages = [[{'projectId': 1,'body': 'hello', 'offer': 500, 'request': 'tu casa', 'senderId': 1}, {'projectId': 1,'body': 'chau', 'offer': 10000, 'request': 'tu esposa', 'senderId': 2}],
      //   [{'projectId': 2,'body': 'como va', 'offer': 80000, 'request': 'el auto', 'senderId': 10}, {'projectId': 2,'body': 'feqefewq', 'offer': 500, 'request': 'la moto', 'senderId': 3}], []];

      _this.answer = function (projectId, senderId, accepted) {
          console.log(projectId);
          console.log(senderId);
      };


      _this.fetchMsgs = function(projectId, index) {
        messageService.unread(projectId.toString(), false).then(function (msgsApi) {
          _this.messages[index] = msgsApi

        })
      }

      _this.enabled = true;
      _this.onOff = false;
      _this.yesNo = true;
      _this.disabled = true;

    }]);
});
