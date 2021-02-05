define(['paw2020a'], function(paw2020a) {
  paw2020a.service('apiResponses', [
    function() {
      this.isInvestor = true;

      this.projects = {headers: function () {
          return {'cache-control': "no-cache, no-store, max-age=0, must-revalidate",
          'content-length': "7559",
          'content-type': "application/json",
          expires: "0",
          link: '<http://localhost:8080/api/projects?p=1&f=1&l=12&o=1>; rel="first", <http://localhost:8080/api/projects?p=1&f=1&l=12&o=1>; rel="start", <http://localhost:8080/api/projects?p=5&f=1&l=12&o=1>; rel="end", <http://localhost:8080/api/projects?p=9&f=1&l=12&o=1>; rel="last"',
          pragma: "no-cache"}
        },
        data : [{
        id: 5,
        owner_id: 2,
        project_name: 'Tesls',
        summary: 'Electric cars designed with AI. Project to produce cars that work with water.',
        funding_target: 90000,
        published_date: '2020-05-11 03:00:00.000000',
        update_date: '2020-05-11 03:00:00.000000',
        relevance: 6128,
        message_count: 1,
        closed: false,
        funding_current: 5000,
        stats_id: 1
      },
        {
          id: 7,
          owner_id: 6,
          project_name: 'Alphabet',
          summary: 'Just gooooooooooooooooooooooooooooooooooooogle.',
          funding_target: 100000,
          published_date: '2020-10-15 08:00:00.000000',
          update_date: '2020-12-16 03:00:00.000000',
          relevance: 50000,
          message_count: 5,
          closed: false,
          funding_current: 4000,
          stats_id: 2
        },
        {
          id: 10,
          owner_id: 10,
          project_name: 'Amazon',
          summary: 'Just amazoooooooooooooooooooooooooooooooon.',
          funding_target: 50000,
          published_date: '2020-10-15 08:00:00.000000',
          update_date: '2020-12-16 03:00:00.000000',
          relevance: 50000,
          message_count: 5,
          closed: true,
          funding_current: 4000,
          stats_id: 3
        },
        {
          id: 15,
          owner_id: 10,
          project_name: 'Mercado Libre',
          summary: 'Just Meeeeercadddddoooooooo Liiiiiibrrreeeeee.',
          funding_target: 50000,
          published_date: '2020-10-15 08:00:00.000000',
          update_date: '2020-12-16 03:00:00.000000',
          relevance: 70000,
          message_count: 5,
          closed: true,
          funding_current: 70000,
          stats_id: 4
        },
      ] };

      this.projectService = {
        getPage : function () {},
      }

      this.userService = {
        getFavorites : function () { return apiResponses.favs},
      }


      this.favs = {data:[{projectId: '7'}, {projectId: '15'},{projectId: '10'}]};


      this.emptyProjects = [];

      //this.categories = {data : [{id: 5, category:'hola'}, {id: 6, category:'chau'}, {id: 7, category:'adios'}, {id: 8, category:'de nuevo'}]}

      this.categories = {data: [{"id":14,"name":"3D_Printers","parent":1},{"id":2,"name":"Art"},{"id":12,"name":"Audio","parent":1},{"id":7,"name":"Computing","parent":1},{"id":20,"name":"Earphones","parent":12},{"id":10,"name":"Energy","parent":1},{"id":13,"name":"Entertainement","parent":1},{"id":5,"name":"Fashion"},{"id":18,"name":"Fashion_Accessories","parent":5},{"id":21,"name":"Headphones","parent":12},{"id":9,"name":"Health_&_Fitness","parent":1},{"id":11,"name":"Home","parent":1},{"id":3,"name":"Industry"},{"id":19,"name":"Matketplace","parent":6},{"id":4,"name":"Research"},{"id":6,"name":"Software_Solutions","parent":1},{"id":16,"name":"Sports","parent":5},{"id":23,"name":"Sunglasses","parent":18},{"id":22,"name":"Systems","parent":12},{"id":8,"name":"Tech_Accessories","parent":1},{"id":1,"name":"Technology"},{"id":17,"name":"Travel","parent":5},{"id":15,"name":"Wereables","parent":1}] }



    }


  ]);
});
