    'use strict';

define(['paw2020a', 'services/AuthenticationService', 'services/PathService'], function(paw2020a) {
    paw2020a.controller('logoutCtrl', ['AuthenticationService', 'PathService', '$rootScope', function(AuthenticationService, PathService, $rootScope) {
      AuthenticationService.logout();
      PathService.get().index().go();
      $rootScope.$emit('credentialsChanged');
    }]);
});
