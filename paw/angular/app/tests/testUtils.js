define(['paw2020a'], function(paw2020a) {
  paw2020a.service('testUtils', [$q,
    function() {

      this.resolvePromise = function (service, func, response) {
        spyOn(service, func).and.callFake(function () {
          var deferred = $q.defer();
          deferred.resolve(response);
          return deferred.promise;
        })
      }

    }


  ]);
});
