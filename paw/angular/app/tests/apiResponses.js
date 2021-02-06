define(['paw2020a'], function(paw2020a) {
  paw2020a.service('apiResponses', [
    function() {
      this.isInvestor = true;

      this.isEntrepreneur = false;

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


      this.favs = {data:[{projectId: '7'}, {projectId: '15'},{projectId: '10'}]};

      this.profileFavs = {data:[{id: '7', "fundingCurrent":2109090, "fundingTarget":9999999}, {id: '15', "fundingCurrent":0,"fundingTarget":5000000},{id: '10',"fundingCurrent":2109090, "fundingTarget":9999999}]};


      this.emptyProjects = [];

      this.userInvestor = {data: {"type":"fullUserDto","birthDate":"1999-04-02T00:00:00-03:00","cityId":0,"countryId":0,"favorites":"http://localhost:8080/paw-2020a-5/api/users/4/favorites","firstName":"francisco","id":4,"imageExists":false,"joinDate":"2020-04-14T00:00:00-03:00","lastName":"choi","linkedin":"linkedin.com/in/francisco-choi-2a3577194/","locale":"es-AR","location":"http://localhost:8080/paw-2020a-5/api/users/4/location","phone":"1158885374","projectList":"http://localhost:8080/paw-2020a-5/api/users/4/projects","realId":"41824814","receivedMessages":"http://localhost:8080/paw-2020a-5/api/users/4/received_messages","sentMessages":"http://localhost:8080/paw-2020a-5/api/users/4/sent_messages","stateId":0,"verified":true,"email":"fchoi@itba.edu.ar","role":"Investor"}};

      this.invLocation = {data: {"city":"Ghazni","country":"Afghanistan","id":4,"state":"Ghazni"}};

      this.userEntrepreuner = {data : {"type":"fullUserDto","birthDate":"1987-04-06T00:00:00-03:00","cityId":0,"countryId":0,"favorites":"http://localhost:8080/paw-2020a-5/api/users/16/favorites","firstName":"elon","id":16,"imageExists":false,"joinDate":"2020-05-11T00:00:00-03:00","lastName":"musk","linkedin":"www.linkedin.com/in/elonmusk","locale":"es-AR","location":"http://localhost:8080/paw-2020a-5/api/users/16/location","phone":"43215315321","projectList":"http://localhost:8080/paw-2020a-5/api/users/16/projects","realId":"43124321423","receivedMessages":"http://localhost:8080/paw-2020a-5/api/users/16/received_messages","sentMessages":"http://localhost:8080/paw-2020a-5/api/users/16/sent_messages","stateId":0,"verified":true,"email":"franchoi42@gmail.com","role":"Entrepreneur"}};

      this.entLocation = {data: {"city":"Buenos Aires","country":"Argentina","id":10,"state":"Buenos Aires"}};

      this.project = {data: {"categories":"http://localhost:8080/paw-2020a-5/api/projects/82/categories","closed":false,"fundingCurrent":2109090,"fundingTarget":9999999,"getByOwner":true,"hits":59,"id":82,"name":"tesla","owner":"http://localhost:8080/paw-2020a-5/api/users/16","portraitImage":"http://localhost:8080/paw-2020a-5/api/images/projects/82","projectStages":"http://localhost:8080/paw-2020a-5/api/projects/82/stages","publishDate":"2020-05-11T00:00:00-03:00","slideshowImages":"http://localhost:8080/paw-2020a-5/api/images/projects/82/slideshow","summary":"Electric cars designed with AI. Project to produce cars that work with water.","updateDate":"2020-05-11T00:00:00-03:00"}};

      this.formErrorResponse = {status: 400};

      this.projectCategories = {data : [{"id":1,"name":"Technology"},{"id":2,"name":"Art"},{"id":7,"name":"Computing","parent":1},{"id":20,"name":"Earphones","parent":12},{"id":23,"name":"Sunglasses","parent":18},{"id":6,"name":"Software_Solutions","parent":1}]};

      this.fundedProjects = {data :[{"categories":"http://localhost:8080/paw-2020a-5/api/users/16/projects/categories","closed":true,"fundingCurrent":0,"fundingTarget":5000000,"getByOwner":false,"hits":0,"id":85,"name":"SolarCity","owner":"http://localhost:8080/paw-2020a-5/api/users/16","portraitImage":"http://localhost:8080/paw-2020a-5/api/images/projects/85","projectStages":"http://localhost:8080/paw-2020a-5/api/projects/85/stages","publishDate":"2020-05-12T00:00:00-03:00","slideshowImages":"http://localhost:8080/paw-2020a-5/api/images/projects/85/slideshow","summary":"SolarCity Corporation is a subsidiary of Tesla, Inc. that specializes in solar energy services and is headquartered in San Mateo, California.","updateDate":"2020-05-12T00:00:00-03:00"}]};

      this.notFundedProjects = { headers: function () {
          return {'cache-control': "no-cache, no-store, max-age=0, must-revalidate",
            'content-length': "7559",
            'content-type': "application/json",
            expires: "0",
            link: '<http://localhost:8080/api/projects?p=1&f=1&l=12&o=1>; rel="first", <http://localhost:8080/api/projects?p=1&f=1&l=12&o=1>; rel="start", <http://localhost:8080/api/projects?p=5&f=1&l=12&o=1>; rel="end", <http://localhost:8080/api/projects?p=9&f=1&l=12&o=1>; rel="last"',
            pragma: "no-cache"}
        },data: [{"categories":"http://localhost:8080/paw-2020a-5/api/users/16/projects/categories","closed":false,"fundingCurrent":2109090,"fundingTarget":9999999,"getByOwner":false,"hits":59,"id":82,"name":"tesla","owner":"http://localhost:8080/paw-2020a-5/api/users/16","portraitImage":"http://localhost:8080/paw-2020a-5/api/images/projects/82","projectStages":"http://localhost:8080/paw-2020a-5/api/projects/82/stages","publishDate":"2020-05-11T00:00:00-03:00","slideshowImages":"http://localhost:8080/paw-2020a-5/api/images/projects/82/slideshow","summary":"Electric cars designed with AI. Project to produce cars that work with water.","updateDate":"2020-05-11T00:00:00-03:00"},{"categories":"http://localhost:8080/paw-2020a-5/api/users/16/projects/categories","closed":false,"fundingCurrent":0,"fundingTarget":5000000,"getByOwner":false,"hits":1,"id":102,"name":"SpaceY","owner":"http://localhost:8080/paw-2020a-5/api/users/16","portraitImage":"http://localhost:8080/paw-2020a-5/api/images/projects/102","projectStages":"http://localhost:8080/paw-2020a-5/api/projects/102/stages","publishDate":"2020-06-24T00:00:00-03:00","slideshowImages":"http://localhost:8080/paw-2020a-5/api/images/projects/102/slideshow","summary":"new project to reach other galaxies reaching almost speed of light","updateDate":"2020-06-24T00:00:00-03:00"},{"categories":"http://localhost:8080/paw-2020a-5/api/users/16/projects/categories","closed":false,"fundingCurrent":0,"fundingTarget":500000,"getByOwner":false,"hits":1,"id":88,"name":"Ziip2","owner":"http://localhost:8080/paw-2020a-5/api/users/16","portraitImage":"http://localhost:8080/paw-2020a-5/api/images/projects/88","projectStages":"http://localhost:8080/paw-2020a-5/api/projects/88/stages","publishDate":"2020-05-12T00:00:00-03:00","slideshowImages":"http://localhost:8080/paw-2020a-5/api/images/projects/88/slideshow","summary":"The company developed and marketed an internet city guide for the newspaper publishing industry, with maps, directions and yellow pages,[61] with the vector graphics mapping and direction code being implemented by Musk in Java.","updateDate":"2020-05-12T00:00:00-03:00"}]};

      this.userExistingError = {status: 409};

      this.serverRetryError = {status: 503};

      //this.categories = {data : [{id: 5, category:'hola'}, {id: 6, category:'chau'}, {id: 7, category:'adios'}, {id: 8, category:'de nuevo'}]}

      this.categories = {data: [{"id":14,"name":"3D_Printers","parent":1},{"id":2,"name":"Art"},{"id":12,"name":"Audio","parent":1},{"id":7,"name":"Computing","parent":1},{"id":20,"name":"Earphones","parent":12},{"id":10,"name":"Energy","parent":1},{"id":13,"name":"Entertainement","parent":1},{"id":5,"name":"Fashion"},{"id":18,"name":"Fashion_Accessories","parent":5},{"id":21,"name":"Headphones","parent":12},{"id":9,"name":"Health_&_Fitness","parent":1},{"id":11,"name":"Home","parent":1},{"id":3,"name":"Industry"},{"id":19,"name":"Matketplace","parent":6},{"id":4,"name":"Research"},{"id":6,"name":"Software_Solutions","parent":1},{"id":16,"name":"Sports","parent":5},{"id":23,"name":"Sunglasses","parent":18},{"id":22,"name":"Systems","parent":12},{"id":8,"name":"Tech_Accessories","parent":1},{"id":1,"name":"Technology"},{"id":17,"name":"Travel","parent":5},{"id":15,"name":"Wereables","parent":1}] }

      this.countries = {data: [{"currency":"AFN","id":1,"isoCode":"AF","name":"Afghanistan","phoneCode":"93"},{"currency":"EUR","id":2,"isoCode":"AX","name":"Aland Islands","phoneCode":"+358-18"},{"currency":"ALL","id":3,"isoCode":"AL","name":"Albania","phoneCode":"355"},{"currency":"DZD","id":4,"isoCode":"DZ","name":"Algeria","phoneCode":"213"},{"currency":"USD","id":5,"isoCode":"AS","name":"American Samoa","phoneCode":"+1-684"},{"currency":"EUR","id":6,"isoCode":"AD","name":"Andorra","phoneCode":"376"},{"currency":"AOA","id":7,"isoCode":"AO","name":"Angola","phoneCode":"244"},{"currency":"XCD","id":8,"isoCode":"AI","name":"Anguilla","phoneCode":"+1-264"},{"currency":"","id":9,"isoCode":"AQ","name":"Antarctica","phoneCode":""},{"currency":"XCD","id":10,"isoCode":"AG","name":"Antigua And Barbuda","phoneCode":"+1-268"},{"currency":"ARS","id":11,"isoCode":"AR","name":"Argentina","phoneCode":"54"},{"currency":"AMD","id":12,"isoCode":"AM","name":"Armenia","phoneCode":"374"}]}

      this.states = {data: [{"id":3901,"isoCode":"BDS","name":"Badakhshan"},{"id":3871,"isoCode":"BDG","name":"Badghis"},{"id":3875,"isoCode":"BGL","name":"Baghlan"},{"id":3884,"isoCode":"BAL","name":"Balkh"},{"id":3872,"isoCode":"BAM","name":"Bamyan"},{"id":3892,"isoCode":"DAY","name":"Daykundi"},{"id":3899,"isoCode":"FRA","name":"Farah"},{"id":3889,"isoCode":"FYB","name":"Faryab"},{"id":3870,"isoCode":"GHA","name":"Ghazni"},{"id":3888,"isoCode":"GHO","name":"Ghōr"},{"id":3873,"isoCode":"HEL","name":"Helmand"},{"id":3887,"isoCode":"HER","name":"Herat"},{"id":3886,"isoCode":"JOW","name":"Jowzjan"},{"id":3902,"isoCode":"KAB","name":"Kabul"},{"id":3890,"isoCode":"KAN","name":"Kandahar"},{"id":3879,"isoCode":"KAP","name":"Kapisa"},{"id":3878,"isoCode":"KHO","name":"Khost"},{"id":3876,"isoCode":"KNR","name":"Kunar"},{"id":3900,"isoCode":"KDZ","name":"Kunduz Province"},{"id":3891,"isoCode":"LAG","name":"Laghman"},{"id":3897,"isoCode":"LOG","name":"Logar"},{"id":3882,"isoCode":"NAN","name":"Nangarhar"},{"id":3896,"isoCode":"NIM","name":"Nimruz"},{"id":3880,"isoCode":"NUR","name":"Nuristan"},{"id":3894,"isoCode":"PIA","name":"Paktia"},{"id":3877,"isoCode":"PKA","name":"Paktika"},{"id":3881,"isoCode":"PAN","name":"Panjshir"},{"id":3895,"isoCode":"PAR","name":"Parwan"},{"id":3883,"isoCode":"SAM","name":"Samangan"},{"id":3885,"isoCode":"SAR","name":"Sar-e Pol"},{"id":3893,"isoCode":"TAK","name":"Takhar"},{"id":3898,"isoCode":"URU","name":"Urozgan"},{"id":3874,"isoCode":"ZAB","name":"Zabul"}]}

      this.cities = {data: [{"id":38592,"name":"Alaba Special Wereda"},{"id":38593,"name":"Arba Minch"},{"id":38599,"name":"Bako"},{"id":38603,"name":"Bench Maji Zone"},{"id":38606,"name":"Bodītī"},{"id":38607,"name":"Bonga"},{"id":38609,"name":"Butajīra"},{"id":38625,"name":"Dīla"},{"id":38629,"name":"Felege Neway"},{"id":38634,"name":"Gedeo Zone"},{"id":38645,"name":"Guraghe Zone"},{"id":38647,"name":"Gīdolē"},{"id":38648,"name":"Hadiya Zone"},{"id":38651,"name":"Hawassa"},{"id":38652,"name":"Hosa’ina"},{"id":38662,"name":"Jinka"},{"id":38663,"name":"Kembata Alaba Tembaro Zone"},{"id":38668,"name":"Konso"},{"id":38670,"name":"K’olīto"},{"id":38672,"name":"Leku"},{"id":38674,"name":"Lobuni"},{"id":38683,"name":"Mīzan Teferī"},{"id":38696,"name":"Sheka Zone"},{"id":38698,"name":"Sidama Zone"},{"id":38700,"name":"Sodo"},{"id":38705,"name":"Tippi"},{"id":38707,"name":"Turmi"},{"id":38710,"name":"Wendo"},{"id":38716,"name":"Wolayita Zone"},{"id":38718,"name":"Yem"},{"id":38719,"name":"Yirga ‘Alem"},{"id":38725,"name":"Āreka"}]}

      this.messages = {headers: function () {
          return {'cache-control': "no-cache, no-store, max-age=0, must-revalidate",
          'content-length': "2953",
          'content-type': "application/json",
          expires: "0",
          link: '<http://localhost:8080/api/messages/chat/111?p=2>; rel="next"',
          pragma: "no-cache"}
        },
        data: [{
      $$hashKey: "object:50",
      accepted: false,
      answered: true,
      chat: "http://localhost:8080/api/messages/chat/110/7",
      comment: "Me interesaron mucho los cascos",
      direction: true,
      exchange: "10% de futuras ventas",
      expInDays: 0,
      expiryDate: "2021-02-06T22:10:01.77-03:00",
      expiryDays: 0,
      id: 123,
      incoming: false,
      investor: "http://localhost:8080/api/users/7",
      investorId: 7,
      offer: 1000,
      owner: "http://localhost:8080/api/users/16",
      ownerId: 16,
      project: "http://localhost:8080/api/projects/110",
      projectId: 110,
      publishDate: "2021-02-04T00:00:00-03:00",
      seen: true,
      seenAnswer: true,
      _proto_: Object},
      {
      $$hashKey: "object:51",
      accepted: true,
      answered: true,
      chat: "http://localhost:8080/api/messages/chat/110/7",
      comment: "Es mucho 10 bro",
      direction: false,
      exchange: "5% Ventas",
      expInDays: 0,
      expiryDate: "2021-02-05T22:34:16.482-03:00",
      expiryDays: 0,
      id: 124,
      incoming: true,
      investor: "http://localhost:8080/api/users/7",
      investorId: 7,
      offer: 1000,
      owner: "http://localhost:8080/api/users/16",
      ownerId: 16,
      project: "http://localhost:8080/api/projects/110",
      projectId: 110,
      publishDate: "2021-02-04T00:00:00-03:00",
      seen: true,
      seenAnswer: true,
      _proto_: Object},
      {
      $$hashKey: "object:52",
      accepted: false,
      answered: true,
      chat: "http://localhost:8080/api/messages/chat/110/7",
      comment: "Nada",
      direction: true,
      exchange: "Nada",
      expInDays: 0,
      expiryDate: "2021-02-06T20:07:39.27-03:00",
      expiryDays: 0,
      id: 125,
      incoming: false,
      investor: "http://localhost:8080/api/users/7",
      investorId: 7,
      offer: 100,
      owner: "http://localhost:8080/api/users/2",
      ownerId: 16,
      project: "http://localhost:8080/api/projects/110",
      projectId: 110,
      publishDate: "2021-02-05T00:00:00-03:00",
      seen: true,
      seenAnswer: false}]};

      this.msgCount = {data: {route: 82, unread:5}};

      this.accept = {status: 200, data: []};

    }


  ]);
});
