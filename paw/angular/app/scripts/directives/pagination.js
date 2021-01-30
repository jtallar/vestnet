'use strict';

define(['paw2020a'], function(paw2020a) {
  paw2020a.directive('pagination', function(){
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      template: function(element, attrs) {
          return '<ul class="pagination justify-content-center" ng-if="lastPage !== 1">\n' +
            '      <div ng-if="page > 1">\n' +
            '        <li id="li-previous" class="page-item">\n' +
            '          <a href="" id="li-a-previous" class="page-link" ng-click="getToPage(1)" aria-label="{{\'previousPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&laquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page - 1) >= 1">\n' +
            '        <li id="li-previous" class="page-item">\n' +
            '          <a href="" id="li-a-previous" class="page-link" ng-click="getToPage(page - 1)" aria-label="{{\'previousPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&lsaquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page - 2) >= 1">\n' +
            '        <li id="li-previous-num" class="page-item">\n' +
            '          <a href="" id="li-a-previous-num" class="page-link" ng-click="getToPage(page - 2)" aria-label="{{\'pageNumber\' | translate : {p: page - 1} }}">\n' +
            '            {{page -2}}\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page - 1) >= 1">\n' +
            '        <li id="li-previous-num" class="page-item">\n' +
            '          <a href="" id="li-a-previous-num" class="page-link" ng-click="getToPage(page - 1)" aria-label="{{\'pageNumber\' | translate : {p: page - 1} }}">{{page -1}}</a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <li id="li-now" class="page-item">\n' +
            '        <a id="li-a-now" class="page-link" onclick="" aria-label="{{\'pageNumber\' | translate : {p: page} }}">{{page}}</a>\n' +
            '      </li>\n' +
            '      <div ng-if="(page + 1) <= lastPage">\n' +
            '        <li id="li-next-num" class="page-item">\n' +
            '          <a href="" id="li-a-next-num" class="page-link" ng-click="getToPage(page + 1)" aria-label="{{\'pageNumber\' | translate : {p: page + 1} }}">{{page + 1}}</a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page + 2) <= lastPage">\n' +
            '        <li id="li-next-num" class="page-item">\n' +
            '          <a href="" id="li-a-next-num" class="page-link" ng-click="getToPage(page + 2)" aria-label="{{\'pageNumber\' | translate : {p: page + 2} }}">{{page + 2}}</a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="(page + 1) <= lastPage">\n' +
            '        <li id="li-next" class="page-item">\n' +
            '          <a href="" id="li-a-next" class="page-link" ng-click="getToPage(page + 1)"  aria-label="{{\'nextPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&rsaquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '      <div ng-if="page < lastPage">\n' +
            '        <li id="li-next" class="page-item">\n' +
            '          <a href="" id="li-a-next" class="page-link" ng-click="getToPage(lastPage)"  aria-label="{{\'nextPage\' | translate}}">\n' +
            '            <span aria-hidden="true">&raquo;</span>\n' +
            '          </a>\n' +
            '        </li>\n' +
            '      </div>\n' +
            '    </ul>'
      }
    }
  });
});
