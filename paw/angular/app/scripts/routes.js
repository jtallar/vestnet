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
            '/logout': {
              templateUrl: 'views/logout/logout.html',
              controller: 'logoutCtrl'
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
            '/users/:id': {
                templateUrl: '/views/users/:id/profile.html',
                controller: 'profileCtrl'
            },
            // '/profile/:id': {
            //   templateUrl: '/views/users/:id/profile.html',
            //   controller: 'profileCtrl'
            // },
            '/requests': {
                templateUrl: '/views/requests/requests.html',
                controller: 'requestsCtrl'
            },
            '/dashboard': {
                templateUrl: '/views/dashboard/dashboard.html',
                controller: 'dashboardCtrl'
            },
            '/messages': {
              templateUrl: '/views/requests/messages.html',
              controller: 'messagesCtrl'
            },
            '/newProject': {
                templateUrl: '/views/newProject/newProject.html',
                controller: 'newProjectCtrl'
            },
            '/verify': {
                templateUrl: 'views/verify/verify.html',
                controller: 'verifyCtrl'
            },
            '/editProject': {
              templateUrl: '/views/edit/editProject.html',
              controller: 'editProjectCtrl'
            },
            '/chat/:id1': {
              templateUrl: '/views/dashboard/chat.html',
              controller: 'chatCtrl'
            },
            '/chat/:id1/:id2': {
              templateUrl: '/views/dashboard/chat.html',
              controller: 'chatCtrl'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
