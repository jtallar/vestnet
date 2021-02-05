define(['paw2020a'], function(paw2020a) {
  paw2020a.service('utilities', ['$q',
    function($q) {
      this.resolvePromise = function (service, func, response) {
        spyOn(service, func).and.callFake(function () {
          var deferred = $q.defer();
          deferred.resolve(response);
          return deferred.promise;
        })
      }

      this.ignoreTestAside = function (httpBackend){
        httpBackend.whenGET(/views.*/).respond(200, '');
        httpBackend.whenGET(/api.*/).respond(200, '');
      }
    }
  ]);
});
