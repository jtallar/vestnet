'use strict';

define([], function() {
    return {
        defaultRoutePath: '/error',
        routes: {
            '/': {                                // welcome
                templateUrl: 'views/home.html',
                controller: 'HomeCtrl'
            },
            '/welcome': {                                // welcome
              templateUrl: 'views/home.html',
              controller: 'HomeCtrl'
            },
            '/login': {
                templateUrl: 'views/login/login.html',
                controller: 'loginCtrl'
            },
            '/resetPassword': {
                templateUrl: '/views/resetPassword/resetPassword.html',
                controller: 'resetPasswordCtrl'
            },
            '/requestPassword': {
                templateUrl: '/views/requestPassword/requestPassword.html',
                controller: 'requestPasswordCtrl'
            },
            '/signUp': {
                templateUrl: '/views/signUp/signUp.html',
                controller: 'signUpCtrl',
                css: 'styles/signup.css'
            },
            '/projects': {
              templateUrl: '/views/projects/feed.html',
              controller: 'feedCtrl'
            },
            '/error': {
              templateUrl: '/views/errors/errors.html',
              controller: 'errorCtrl'
            },
            '/projects/:id': {
                templateUrl: '/views/viewProject/:id/singleView.html',
                controller: 'singleViewCtrl'
            },
            '/users/1': {
                templateUrl: '/views/users/profile.html',
                controller: 'profileCtrl'
            },
            '/profile': {
              templateUrl: '/views/users/profile.html',
              controller: 'profileCtrl'
            },
            '/deals': {
                templateUrl: '/views/deals/deals.html',
                controller: 'dealsCtrl'
            },
            '/requests': {
                templateUrl: '/views/requests/requests.html',
                controller: 'requestsCtrl'
            },
            '/dashboard': {
                templateUrl: '/views/dashboard/dashboard.html',
                controller: 'dashboardCtrl'
            },
            '/newProject': {
                templateUrl: '/views/newProject/newProject.html',
                controller: 'newProjectCtrl'
            },
            '/verify': {
                templateUrl: 'views/verify/verify.html',
                controller: 'verifyCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
