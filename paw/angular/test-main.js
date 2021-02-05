var allTestFiles = []
var TEST_REGEXP = /(spec|test)\.js$/i

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
  if (TEST_REGEXP.test(file)) {
    // Normalize paths to RequireJS module names.
    // If you require sub-dependencies of test files to be loaded as-is (requiring file extension)
    // then do not normalize the paths
    var normalizedTestModule = file.replace(/^\/base\/|\.js$/g, '')
    allTestFiles.push(normalizedTestModule)
  }
})

var tests = [];
for (var file in window.__karma__.files) {
  if (window.__karma__.files.hasOwnProperty(file)) {
    if (/Spec\.js$/.test(file)) {
      tests.push(file);
    }
  }
}


require.config({
  // Karma serves files under /base, which is the basePath from your config file
  baseUrl: '/base/app/scripts',

  paths: {
    paw2020a: 'paw2020a',
    utilities: '../tests/utilities',
    'services/dependencyResolverFor': 'services/dependencyResolverFor',
    jquery: '../../bower_components/jquery/dist/jquery',
    angular: '../../bower_components/angular/angular',
    angularMocks: '../../bower_components/angular-mocks/angular-mocks',
    ngResource: '../../bower_components/angular-resource/angular-resource.min',
    'angular-route': '../../bower_components/angular-route/angular-route',
    bootstrap: '../../bower_components/bootstrap/dist/js/bootstrap',
    restangular: '../../bower_components/restangular/dist/restangular',
    lodash: '../../bower_components/lodash/lodash',
    modal: '../../bower_components/bootstrap-sass-official/assets/javascripts/bootstrap/modal',
    'angular-translate': '../../bower_components/angular-translate/angular-translate',
    feedCtrl: 'controllers/feedCtrl',
    newProjectCtrl: 'controllers/newProjectCtrl',
    signUpCtrl: 'controllers/signUpCtrl',
    chatCtrl: 'controllers/chatCtrl',
    userService: 'services/userService',
    apiResponses: '../tests/apiResponses',

  },

  shim: {
    jquery: {
      exports: '$'
    },
    angular: {
      deps: [ 'jquery', 'bootstrap'],
      exports: 'angular'
    },
    'angular-route': {
      deps: [
        'angular'
      ]
    },
    'angular-translate': {
      deps: [
        'angular'
      ]
    },
    modal: {
      deps: [
        'jquery'
      ]
    },
    bootstrap: {
      deps: [
        'jquery',
        'modal'
      ]
    },
    ngResource: {
      deps: [ 'angular' ],
      exports: 'ngResource'
    },
    angularMocks: {
      deps: [ 'ngResource' ],
      exports: 'angularMocks'
    },
    lodash: {
      exports: '_'
    },
    restangular: {
      deps: [
        'angular',
        'lodash'
      ]
    },
  },

  priority: [
    'angular'
  ],


  // dynamically load all test files
  deps: tests,

  // // we have to kickoff jasmine, as it is asynchronous
  // callback: window.__karma__.start
});

require(tests, function(){
  window.__karma__.start();
});
