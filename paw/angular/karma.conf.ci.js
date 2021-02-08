// Code taken from: https://github.com/eirslett/frontend-maven-plugin/blob/master/frontend-maven-plugin/src/it/example%20project/src/test/javascript/karma.conf.ci.js

'use strict';

var baseConfig = require('./karma.conf.js');

module.exports = function(config) {
  // Load base config
  baseConfig(config);

  // Override base config
  config.set({
    autoWatch: false,
    singleRun: true
  });
};
