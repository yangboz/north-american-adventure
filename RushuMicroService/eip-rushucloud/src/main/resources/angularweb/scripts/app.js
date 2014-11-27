'use strict';

angular.module('activitiApp', [ 'ngResource', 'ui.bootstrap', "ngRoute",'angular-moment'])

    // Temporary until we have a login page: always log in with kermit:kermit
//    .config(['$httpProvider', function ($httpProvider) {
//        $httpProvider.defaults.headers.common['Authorization'] = 'Basic a2VybWl0Omtlcm1pdA==';
//    }])
    ////$log configure
    .config(['$logProvider', function($logProvider){
        $logProvider.debugEnabled(true);
        //TODO:https://github.com/ThomasBurleson/angularjs-logDecorator
    }])
    .constant('Config_API', {
        'endpoint': 'http://localhost:8082/eip-rushucloud/',
        'version': '5.16.3'
    })

    .config(['$routeProvider', function ($routeProvider){
        $routeProvider
            .when('/', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            }) .when('/dashboard', {
                templateUrl: 'views/dashboard.html',
                controller: 'DashboardCtrl'
            }).when('/users', {
                templateUrl: 'views/users.html',
                controller: 'UsersCtrl'
            }).when('/groups', {
                templateUrl: 'views/groups.html',
                controller: 'GroupsCtrl'
            }).when('/tasks', {
                templateUrl: 'views/tasks.html',
                controller: 'TasksCtrl'
            }).when('/processes', {
                templateUrl: 'views/processes.html',
                controller: 'ProcessesCtrl'
            }).when('/instances', {
                templateUrl: 'views/instances.html',
                controller: 'InstancesCtrl'
            })

//            .when('/tasks', {
//                templateUrl: 'views/tasks.html',
//                controller: 'TaskCtrl'
//            })
            .otherwise({
                redirectTo: '/'
            });
    }]);



