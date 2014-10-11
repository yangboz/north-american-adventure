angular.module('starter.controllers', [])
//
.controller('MainController', function($scope, $http, $rootScope, $location,$ionicModal,$ionicLoading){
//app.controller('MainController', function($scope, $http, $rootScope, $location,$ionicModal,$WebSocket){
//    console.log("MainController init!!!");
///Loading
$rootScope.showLoading = function() {
    $ionicLoading.show({
        template: 'Loading...'
    });
};
$rootScope.hideLoading = function(){
    $ionicLoading.hide();
};
///WebSocket
//    $WebSocket.onopen(function() {
//        console.log('connection');
//        $WebSocket.send('message')
//    });
//
//    $WebSocket.onmessage(function(event) {
//        console.log('message: ', event.data);
//    });
///Model
    ///LoginModal
    $ionicModal.fromTemplateUrl('templates/modal-login.html', {
        scope: $scope,
        backdropClickToClose: false
    }).then(function(modal) {
//        console.log("modal-login.html init!!!");
        $scope.loginModal = modal;
        $scope.loginModal.user={
            username:"kermit",
            password:"kermit"
        };
        //Login Modal
        if(window.localStorage['auth']) {
            $scope.loginModal.hide();
        }else{
//     $urlRouterProvider.otherwise('/login');
            $scope.loginModal.show();
        }
    });
    ///TaskModal
    $ionicModal.fromTemplateUrl('templates/modal-task.html', {
        scope: $scope,
        backdropClickToClose: false
    }).then(function(modal) {
//        console.log("modal-task.html init!!!");
        $scope.taskModal = modal;
        $scope.taskModal.task={

        };
    });
///Basic
    $rootScope.$on("$stateChangeStart", function(){
        //Login Modal,only hide();
        if(window.localStorage['auth']) {
            $scope.loginModal.hide();
        }
        //ShowLoading
        $rootScope.showLoading();
    });

    $rootScope.$on("$stateChangeSuccess", function(){
        //ShowLoading
        $rootScope.hideLoading();
    });

///FixtureData
    var scrollItems = [];

    for (var i=1; i<=100; i++) {
        scrollItems.push("Item " + i);
    }

    $scope.scrollItems = scrollItems;
    $scope.invoice = {payed: true};

    $scope.userAgent =  navigator.userAgent;

    //Cleanup the modal when we're done with it!
    $scope.$on('$destroy', function() {
        $scope.loginModal.remove();
        $scope.taskModal.remove();
    });
    // Execute action on hide modal
    $scope.$on('modal.hidden', function() {
        // Execute action
    });
    // Execute action on remove modal
    $scope.$on('modal.removed', function() {
        // Execute action
    });
})

.controller('StatsCtrl', function ($scope) {
//        $http.get('performance.json').success(function(data) {
//            $scope.performance  = data;
//        }).error(function(error) {
//            console.log(error);
//        })
        /* Chart options */
        $scope.options = {
            chart: {
                type: 'discreteBarChart',
                // height:450,
                margin : {
                    top: 20,
                    right: 20,
                    bottom: 50,
                    left: 55
                },
                x: function(d){ return d.label; },
                y: function(d){ return d.value; },
                showValues: true,
                valueFormat: function(d){
                    return d3.format(',.4f')(d);
                },
                transitionDuration: 500,
                xAxis: {
                    axisLabel: 'X Axis'
                },
                yAxis: {
                    axisLabel: 'Y Axis',
                    axisLabelDistance: 30
                }
            }
        };

        /* Chart data */
        $scope.data = [{
            key: "Cumulative Return",
            values: [
                { "label" : "A" , "value" : 29.765957771107 },
                { "label" : "B" , "value" : 0 },
                { "label" : "C" , "value" : 32.807804682612 },
                { "label" : "D" , "value" : 196.45946739256 },
                { "label" : "E" , "value" : 0.19434030906893 },
                { "label" : "F" , "value" : 98.079782601442 },
                { "label" : "G" , "value" : 13.925743130903 },
                { "label" : "H" , "value" : 5.1387322875705 }
            ]
        }];
        //
        $scope.chart = {
            labels : ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"],
            datasets : [
                {
                    fillColor : "rgba(151,187,205,0)",
                    strokeColor : "#e67e22",
                    pointColor : "rgba(151,187,205,0)",
                    pointStrokeColor : "#e67e22",
                    data : [4, 3, 5, 4, 6]
                },
                {
                    fillColor : "rgba(151,187,205,0)",
                    strokeColor : "#f1c40f",
                    pointColor : "rgba(151,187,205,0)",
                    pointStrokeColor : "#f1c40f",
                    data : [8, 3, 2, 5, 4]
                }
            ]
        };
})
.controller('ReportsCtrl', function($scope, ReportService) {
  $scope.reports = ReportService.all();
})

.controller('ReportDetailCtrl', function($scope, $stateParams, ReportService) {
  $scope.report = ReportService.get($stateParams.reportId);
})

.controller('AccountCtrl', function($scope) {
})

.controller('UsersCtrl', function ($rootScope,$scope, $http, UserService, $rootScope, $location) {
//    console.log("$rootScope.loggedUser",$rootScope.loggedUser);
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
    $scope.users = UserService.get();


    /**
     * New user Initial data
     */
    $scope.newUser =  {
        "id": "",
        "firstName": "",
        "lastName": "",
        "email": "",
        "password": ""
    }

    /**
     * Create user function
     * @param newUser
     */
    $scope.createUser = function (newUser) {
        var user = new UserService(newUser);
        user.id= newUser.id;
        user.firstName= newUser.firstName;
        user.lastName= newUser.lastName;
        user.email= newUser.email;
        user.password= newUser.password;

        user.$save(function (u, putResponseHeaders) {
            $scope.users.data.push(u);
        });
    };

    /**
     * Controler for handling modal
     * @param $scope
     * @param $modalInstance
     * @param newUser
     * @constructor
     */
    var ModalInstanceCtrl = function ($scope, $modalInstance, newUser) {
        $scope.newUser = newUser;
        $scope.ok = function () {
            $modalInstance.close(newUser);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };

    /**
     * Show modal dialog
     */
    $scope.open = function () {
        var modalInstance = $modal.open({
            templateUrl: 'partials/modals/createUser.html',
            controller: ModalInstanceCtrl,
            resolve: {
                newUser: function () {
                    return $scope.newUser;
                }
            }
        });
        modalInstance.result.then(function (newUser) {
            $scope.createUser(newUser);
        }, function () {
        });
    };

    $scope.removeUser = function(user)
    {
        UserService.delete({"user": user.id}, function (data) {
            $scope.users = UserService.get();
        });
    }

    $scope.query = "";
})


.controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location,$log,
                                   TaskService,ProcessService,JobService,ExecutionService,HistoryService) {
    $rootScope.loggedUser = {};
    $rootScope.loggedin = false;

    $scope.userLogin = function () {
        $log.debug("$scope.loginModal.user.username:",$scope.loginModal.user.username,",$scope.loginModal.user.password:",$scope.loginModal.user.password);
        $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.loginModal.user.username + ":" + $scope.loginModal.user.password);

        UserService.get({user: $scope.loginModal.user.username}, function (data) {
            $log.debug("UserService.get(login) success!",data);
            $rootScope.loggedin = true;
            $rootScope.loggedUser = data;
            $rootScope.username = $scope.loginModal.user.username;
            $rootScope.password = $scope.loginModal.user.password;
            $location.path('/dashboard');
            //Remove login modal
            $scope.loginModal.hide();
            //Default getTasks;
            $log.debug("$rootScope.username:",$rootScope.username,",$rootScope.password:",$rootScope.password);
            //
            TaskService.get({user: $rootScope.username}, function (data) {
                $log.debug("TaskService.get() success!",data);
                $rootScope.tasks = data;
            });
            //getProcesses test
            ProcessService.get({user: $rootScope.username}, function (data) {
                $log.debug("ProcessService.get() success!",data);
                $rootScope.processes = data;
            });
            //getJobs test
            JobService.get({user: $rootScope.username}, function (data) {
                $log.debug("JobService.get() success!",data);
                $rootScope.jobs = data;
            });
            //getExecutions test
            ExecutionService.get({user: $rootScope.username}, function (data) {
                $log.debug("ExecutionService.get() success!",data);
                $rootScope.executions = data;
            });
            //getHistory(historic-process-instances) test
            HistoryService.get({user: $rootScope.username}, function (data) {
                $log.debug("HistoryService.get() success!",data);
                $rootScope.historices = data;
            });
        });
    };
})

.controller('TasksCtrl', function ($scope, $http, TaskService, Base64, $rootScope, $location,$log) {
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
})
.controller('TaskDetailCtrl', function($scope, $stateParams, TaskService) {
    $scope.task = TaskService.get($stateParams.taskId);
})

.controller('GroupsCtrl', function ($scope, $rootScope, $location, GroupService,$modal) {
    if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
        $location.path('/login');
        return;
    }
    $scope.groups = GroupService.get();

    /**
     * Initial data of new group
     * @type {{id: string, name: string, type: string}}
     */
    $scope.newGroup = {"id": "", "name": "", "type": "security-role"};

    /**
     * Create group function
     * @param newGroup
     */
    $scope.createGroup = function (newGroup) {
        var group = new GroupService(newGroup);
        group.name = newGroup.name;
        group.id = newGroup.id;
        group.$save(function (u, putResponseHeaders) {
            $scope.groups.data.push(u);
            $scope.isCollapsed = true;
            $scope.newGroup.id = "";
            $scope.newGroup.name = "";
        });
    };

    /**
     * Controler for handling modal actions
     * @param $scope
     * @param $modalInstance
     * @param newGroup
     * @constructor
     */
    var ModalInstanceCtrl = function ($scope, $modalInstance, newGroup) {
        $scope.newGroup = newGroup;
        $scope.ok = function () {
            $modalInstance.close($scope.newGroup);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    };

    /**
     * Open Modal
     */
    $scope.open = function () {
        var modalInstance = $modal.open({
            templateUrl: 'partials/modals/createGroup.html',
            controller: ModalInstanceCtrl,
            resolve: {
                newGroup: function () {
                    return $scope.newGroup;
                }
            }
        });
        modalInstance.result.then(function (newGroup) {
            $scope.createGroup(newGroup);
        }, function () {
        });
    };

    /**
     * Remove Group
     * @param group
     */
    $scope.removeGroup = function (group) {
        GroupService.delete({"group": group.id}, function (data) {
            $scope.groups = GroupService.get();
        });
    };

    $scope.cancel = function () {
        $scope.newGroup.id = "";
        $scope.newGroup.name = "";
    };

    $scope.query = "";
});