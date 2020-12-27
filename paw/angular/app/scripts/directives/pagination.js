'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('pagination', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      template: function(element, attrs) {
          return '<ul class="pagination justify-content-center">\n' +
            '      <div ng-if="(page - 1) >= 1">\n' +
            '        <li id="li-previous" class="page-item">\n' +
            '          <a id="li-a-previous" class="page-link" ng-click="getToPage(page - 1)" aria-label="{{\'previousPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&laquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page - 1) >= 1">\n' +
            '        <li id="li-previous-num" class="page-item">\n' +
            '          <a id="li-a-previous-num" class="page-link" ng-click="getToPage(page - 1)" aria-label="{{\'pageNumber\' | translate : {p: page - 1} }}">{{page -1}}</a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <li id="li-now" class="page-item">\n' +
            '        <a id="li-a-now" class="page-link" onclick="" aria-label="{{\'pageNumber\' | translate : {p: page} }}">{{page}}</a>\n' +
            '      </li>\n' +
            '      <div ng-if="(page + 1) <= lastPage">\n' +
            '        <li id="li-next-num" class="page-item">\n' +
            '          <a id="li-a-next-num" class="page-link" ng-click="getToPage(page + 1)" aria-label="{{\'pageNumber\' | translate : {p: page + 1} }}">{{page + 1}}</a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page + 1) <= lastPage">\n' +
            '        <li id="li-next" class="page-item">\n' +
            '          <a id="li-a-next" class="page-link" ng-click="getToPage(page + 1)"  aria-label="{{\'nextPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&raquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '    </ul>'
      }
    }
  });
});
