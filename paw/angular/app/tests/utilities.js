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

      this.rejectPromise = function (service, func, response) {
        spyOn(service, func).and.callFake(function () {
          // var deferred = $q.defer();
          // deferred.reject(response);
          // return deferred.promise;
          return $q.reject(response)
        })
      }

      this.ignoreTestAside = function (httpBackend){
        httpBackend.whenGET(/views.*/).respond(200, '');
        httpBackend.whenGET(/api.*/).respond(200, '');
      }

      this.resolvePromiseMultiple = function (service,func, responses, index) {
        spyOn(service, func).and.callFake(function () {
          var deferred = $q.defer;
          var response = responses[index];
          deferred.resolve(response);
          return deferred.promise;
        })
      }
    }
  ]);
});
