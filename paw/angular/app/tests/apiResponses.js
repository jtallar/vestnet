define(['paw2020a'], function(paw2020a) {
  paw2020a.service('apiResponses', [
    function() {
      this.isInvestor = true;

      this.projects = [{
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
          id: 10,
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
      ];




    }


  ]);
});
