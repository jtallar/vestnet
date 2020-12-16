'use strict';

define(['paw2020a'], function(paw2020a) {

  paw2020a.controller('editProjectCtrl', function($scope) {

    $scope.projectInfo = {  //  fetch from db
      'cost': 0,
      'title': '',
      'summary': '',
      'categories': [],
      'image': '',
      'slideshow': ''
    };
    $scope.maxSlideshowCount = 5; //  fetch from webapp
    $scope.maxSize = 2; //  fectch from webapp
    $scope.doubleLeft = '>>';
    $scope.doubleRight = '<<';
    $scope.categories = [{'name': 'technology', 'id': 1} , {'name': 'research', 'id': 2}, {'name': 'sports', 'id': 3}, {'name': 'audio', 'id': 4}];

    var fileBox = document.getElementById('customFileProjectPic');
    var maxSizeMsg = document.getElementById('maxSizeErrorMsg');
    var errorTag = document.getElementById('fileErrorFormTag');

    $scope.fileboxChange = function () {
      if (!(fileBox.files.length === 0) && fileBox.files[0].size >= this.maxSize) {
        fileBox.value = null;
        if (errorTag !== null) {
          errorTag.hidden = true;
        }
        maxSizeMsg.hidden = false;
      } else {
        maxSizeMsg.hidden = true;
      }
    };

    var multipleFileBox = document.getElementById('customMultipleFileProjectPic');
    var maxSizeMsgMultiple = document.getElementById('maxSizeErrorMsgMultiple');
    var maxCountMsgMultiple = document.getElementById('maxCountErrorMsgMultiple');
    var errorTagMultiple = document.getElementById('multipleFileErrorFormTag');

    $scope.multipleFileBoxChange = function () {
      var index = 0, error = 0;
      if (multipleFileBox.files.length > this.maxSlideshowCount) {
        error = 1;
        maxSizeMsgMultiple.hidden = true;
        maxCountMsgMultiple.hidden = false;
      } else if (!(multipleFileBox.files.length === 0)) {
        for (index = 0; index < multipleFileBox.files.length; index++) {
          if (multipleFileBox.files[index].size >= this.maxSize) {
            error = 1;
            maxSizeMsgMultiple.hidden = false;
            maxCountMsgMultiple.hidden = true;
            break;
          }
        }
      }
      if (error === 0) {
        maxSizeMsgMultiple.hidden = true;
        maxCountMsgMultiple.hidden = true;
      } else {
        multipleFileBox.value = null;
        if (errorTagMultiple !== null) {
          errorTagMultiple.hidden = true;
        }
      }
    };

    var costTag = document.getElementById('new-project-cost');

    $scope.costKeypress = function () {
      if (costTag.value.length > 6) {
        costTag.value = costTag.value.slice(0, 6);
      }
    };

    $scope.addCategory = function () {
      var cat = document.getElementById('all-categories');
      if (cat.selectedIndex !== -1) {
        document.getElementById('final-categories').appendChild(cat.options[cat.selectedIndex]);
      }
    };

    $scope.delCategory = function () {
      var cat = document.getElementById('final-categories');
      if (cat.selectedIndex !== -1) {
        document.getElementById('all-categories').appendChild(cat.options[cat.selectedIndex]);
      }
    };

    $scope.addCategories = function () {
      var cat = document.getElementById('final-categories');
      for (var i = 0; i < cat.options.length; i++) {
        cat[i].selected = true;
      }
    };

    $scope.adjustInputs = function () {
      addCategories();
      var titleTag = document.getElementById('new-project-title');
      titleTag.value = titleTag.value.trim();
      var summaryTag = document.getElementById('new-project-summary');
      summaryTag.value = summaryTag.value.trim();
      if (costTag.value.length === 0 || costTag.value < 0) {
        costTag.value = 0;
      }
      costTag.value = Math.round(costTag.value);
    };

  });

});
