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

      this.formErrorResponse = {status: 400};

      this.userExistingError = {status: 409};

      this.serverRetryError = {status: 503};

      //this.categories = {data : [{id: 5, category:'hola'}, {id: 6, category:'chau'}, {id: 7, category:'adios'}, {id: 8, category:'de nuevo'}]}

      this.categories = {data: [{"id":14,"name":"3D_Printers","parent":1},{"id":2,"name":"Art"},{"id":12,"name":"Audio","parent":1},{"id":7,"name":"Computing","parent":1},{"id":20,"name":"Earphones","parent":12},{"id":10,"name":"Energy","parent":1},{"id":13,"name":"Entertainement","parent":1},{"id":5,"name":"Fashion"},{"id":18,"name":"Fashion_Accessories","parent":5},{"id":21,"name":"Headphones","parent":12},{"id":9,"name":"Health_&_Fitness","parent":1},{"id":11,"name":"Home","parent":1},{"id":3,"name":"Industry"},{"id":19,"name":"Matketplace","parent":6},{"id":4,"name":"Research"},{"id":6,"name":"Software_Solutions","parent":1},{"id":16,"name":"Sports","parent":5},{"id":23,"name":"Sunglasses","parent":18},{"id":22,"name":"Systems","parent":12},{"id":8,"name":"Tech_Accessories","parent":1},{"id":1,"name":"Technology"},{"id":17,"name":"Travel","parent":5},{"id":15,"name":"Wereables","parent":1}] }

      this.countries = {data: [{"currency":"AFN","id":1,"isoCode":"AF","name":"Afghanistan","phoneCode":"93"},{"currency":"EUR","id":2,"isoCode":"AX","name":"Aland Islands","phoneCode":"+358-18"},{"currency":"ALL","id":3,"isoCode":"AL","name":"Albania","phoneCode":"355"},{"currency":"DZD","id":4,"isoCode":"DZ","name":"Algeria","phoneCode":"213"},{"currency":"USD","id":5,"isoCode":"AS","name":"American Samoa","phoneCode":"+1-684"},{"currency":"EUR","id":6,"isoCode":"AD","name":"Andorra","phoneCode":"376"},{"currency":"AOA","id":7,"isoCode":"AO","name":"Angola","phoneCode":"244"},{"currency":"XCD","id":8,"isoCode":"AI","name":"Anguilla","phoneCode":"+1-264"},{"currency":"","id":9,"isoCode":"AQ","name":"Antarctica","phoneCode":""},{"currency":"XCD","id":10,"isoCode":"AG","name":"Antigua And Barbuda","phoneCode":"+1-268"},{"currency":"ARS","id":11,"isoCode":"AR","name":"Argentina","phoneCode":"54"},{"currency":"AMD","id":12,"isoCode":"AM","name":"Armenia","phoneCode":"374"}]}

      this.states = {data: [{"id":3901,"isoCode":"BDS","name":"Badakhshan"},{"id":3871,"isoCode":"BDG","name":"Badghis"},{"id":3875,"isoCode":"BGL","name":"Baghlan"},{"id":3884,"isoCode":"BAL","name":"Balkh"},{"id":3872,"isoCode":"BAM","name":"Bamyan"},{"id":3892,"isoCode":"DAY","name":"Daykundi"},{"id":3899,"isoCode":"FRA","name":"Farah"},{"id":3889,"isoCode":"FYB","name":"Faryab"},{"id":3870,"isoCode":"GHA","name":"Ghazni"},{"id":3888,"isoCode":"GHO","name":"Ghōr"},{"id":3873,"isoCode":"HEL","name":"Helmand"},{"id":3887,"isoCode":"HER","name":"Herat"},{"id":3886,"isoCode":"JOW","name":"Jowzjan"},{"id":3902,"isoCode":"KAB","name":"Kabul"},{"id":3890,"isoCode":"KAN","name":"Kandahar"},{"id":3879,"isoCode":"KAP","name":"Kapisa"},{"id":3878,"isoCode":"KHO","name":"Khost"},{"id":3876,"isoCode":"KNR","name":"Kunar"},{"id":3900,"isoCode":"KDZ","name":"Kunduz Province"},{"id":3891,"isoCode":"LAG","name":"Laghman"},{"id":3897,"isoCode":"LOG","name":"Logar"},{"id":3882,"isoCode":"NAN","name":"Nangarhar"},{"id":3896,"isoCode":"NIM","name":"Nimruz"},{"id":3880,"isoCode":"NUR","name":"Nuristan"},{"id":3894,"isoCode":"PIA","name":"Paktia"},{"id":3877,"isoCode":"PKA","name":"Paktika"},{"id":3881,"isoCode":"PAN","name":"Panjshir"},{"id":3895,"isoCode":"PAR","name":"Parwan"},{"id":3883,"isoCode":"SAM","name":"Samangan"},{"id":3885,"isoCode":"SAR","name":"Sar-e Pol"},{"id":3893,"isoCode":"TAK","name":"Takhar"},{"id":3898,"isoCode":"URU","name":"Urozgan"},{"id":3874,"isoCode":"ZAB","name":"Zabul"}]}

      this.cities = {data: [{"id":38592,"name":"Alaba Special Wereda"},{"id":38593,"name":"Arba Minch"},{"id":38599,"name":"Bako"},{"id":38603,"name":"Bench Maji Zone"},{"id":38606,"name":"Bodītī"},{"id":38607,"name":"Bonga"},{"id":38609,"name":"Butajīra"},{"id":38625,"name":"Dīla"},{"id":38629,"name":"Felege Neway"},{"id":38634,"name":"Gedeo Zone"},{"id":38645,"name":"Guraghe Zone"},{"id":38647,"name":"Gīdolē"},{"id":38648,"name":"Hadiya Zone"},{"id":38651,"name":"Hawassa"},{"id":38652,"name":"Hosa’ina"},{"id":38662,"name":"Jinka"},{"id":38663,"name":"Kembata Alaba Tembaro Zone"},{"id":38668,"name":"Konso"},{"id":38670,"name":"K’olīto"},{"id":38672,"name":"Leku"},{"id":38674,"name":"Lobuni"},{"id":38683,"name":"Mīzan Teferī"},{"id":38696,"name":"Sheka Zone"},{"id":38698,"name":"Sidama Zone"},{"id":38700,"name":"Sodo"},{"id":38705,"name":"Tippi"},{"id":38707,"name":"Turmi"},{"id":38710,"name":"Wendo"},{"id":38716,"name":"Wolayita Zone"},{"id":38718,"name":"Yem"},{"id":38719,"name":"Yirga ‘Alem"},{"id":38725,"name":"Āreka"}]}



    }


  ]);
});
