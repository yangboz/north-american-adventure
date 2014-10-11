angular.module('starter.controllers', [])
//
.controller('MainController', function($scope, $http, $rootScope, $location,$ionicModal,$ionicLoading,$ionicNavBarDelegate){
//app.controller('MainController', function($scope, $http, $rootScope, $location,$ionicModal,$WebSocket){
//    console.log("MainController init!!!");
///GoBack
$rootScope.goBack = function () {
    $ionicNavBarDelegate.back();
};
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
//@example:http://krispo.github.io/angular-nvd3/#/
.controller('StatsCtrl', function ($scope) {
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
})
.controller('ReportsCtrl', function($scope,$rootScope, ReportService,$log) {
//    $log.debug("$rootScope.username:",$rootScope.username);
    ReportService.get({assignee: $rootScope.username}, function (response) {
        $log.debug("TaskService.get() success!",response);
        $rootScope.reports = response.data;
    });
    //
    $scope.orderValue = 'asc';//desc
    //ORDER
    $scope.orderReports = function(){
        $scope.orderValue = ($scope.orderValue=='asc')?'desc':'asc';
        //
        ReportService.get({assignee: $rootScope.username,order:$scope.orderValue}, function (response) {
            $log.debug("ReportService.get(order) success!",response);
            $rootScope.reports = response.data;
        });
    }
})

.controller('ReportDetailCtrl', function($scope, $stateParams, ReportService) {
  $scope.report = ReportService.get($stateParams.reportId);
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
        ///
        user.$save(function (u, putResponseHeaders) {
            $scope.users.data.push(u);
        });
    };

    $scope.removeUser = function(user)
    {
        UserService.delete({"user": user.id}, function (data) {
            $scope.users = UserService.get();
        });
    }
})


.controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location,$log,
                                   TaskService,ProcessService,JobService,ExecutionService,HistoryService) {
    $rootScope.loggedUser = {};
    $rootScope.loggedin = false;

    $scope.userLogin = function () {
//        $log.debug("$scope.loginModal.user.username:",$scope.loginModal.user.username,",$scope.loginModal.user.password:",$scope.loginModal.user.password);
        $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.loginModal.user.username + ":" + $scope.loginModal.user.password);

        UserService.get({user: $scope.loginModal.user.username}, function (response) {
            $log.debug("UserService.get(login) success!",response);
            $rootScope.loggedin = true;
            $rootScope.loggedUser = response;
            $rootScope.username = $scope.loginModal.user.username;
            $rootScope.password = $scope.loginModal.user.password;
            $location.path('/dashboard');
            //Remove login modal
            $scope.loginModal.hide();
            //Default getTasks;
            $log.debug("$rootScope.username:",$rootScope.username,",$rootScope.password:",$rootScope.password);
            //
            TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                $log.debug("TaskService.get() success!",response);
                $rootScope.tasks = response.data;
            });
            //getProcesses test
            ProcessService.get({user: $rootScope.username}, function (response) {
                $log.debug("ProcessService.get() success!",response);
                $rootScope.processes = response.data;
            });
            //getJobs test
            JobService.get({user: $rootScope.username}, function (response) {
                $log.debug("JobService.get() success!",response);
                $rootScope.jobs = response.data;
            });
            //getExecutions test
            ExecutionService.get({user: $rootScope.username}, function (response) {
                $log.debug("ExecutionService.get() success!",response);
                $rootScope.executions = response.data;
            });
            //getHistory(historic-process-instances) test
            HistoryService.get({user: $rootScope.username}, function (response) {
                $log.debug("HistoryService.get() success!",response);
                $rootScope.historices = response.data;
            });
        });
    };
})

.controller('TasksCtrl', function ($scope, $http, TaskService, Base64, $rootScope, $location,$log) {
    //ng-model
    $scope.newTask = {"name": "", "description": "","dueDate":"","owner":$rootScope.username};
    //CREATE, //@see:http://www.activiti.org/userguide/#N1693F
    $scope.createTask = function(){
        $log.debug("createTask(),$scope.newTask:",$scope.newTask);
        var anewTask = new TaskService($scope.newTask);
        anewTask.name = $scope.newTask.name;
        anewTask.description = $scope.newTask.description;
        anewTask.dueDate = $scope.newTask.dueDate;
        anewTask.owner = $scope.newTask.owner;
        //Save
        anewTask.$save(function (t, putResponseHeaders) {
            $log.info("createTask() success, response:",t);
            //Hide task modal
            $scope.taskModal.hide();
            //Refresh task list
            TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                $log.debug("TaskService.get() success!",response);
                $rootScope.tasks = response.data;
            });
            //Reset value
            $scope.newTask = {"name": "", "description": "","dueDate":"","owner":$rootScope.username};
        });
    }
    //DELETE runtime/tasks/{taskId}?cascadeHistory={cascadeHistory}&deleteReason={deleteReason}
    $scope.removeTask = function(taskId)
    {
        TaskService.delete({"taskId": taskId,cascadeHistory:true,deleteReason:'testing'}, function (data) {
            $log.debug("TaskService.delete:",data);
            //Refresh task list
            TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                $log.debug("TaskService.get() success!",response);
                $rootScope.tasks = response.data;
            });
        });
        //
    }
    $scope.orderValue = 'asc';//desc
    //ORDER
    $scope.orderTasks = function(){
        $scope.orderValue = ($scope.orderValue=='asc')?'desc':'asc';
        //
        TaskService.get({order:$scope.orderValue}, function (response) {
            $log.debug("TaskService.get(order) success!",response);
            $rootScope.tasks = response.data;
        });
    }
})
.controller('TaskDetailCtrl', function($scope,$rootScope, $stateParams, TaskService,$log) {
    $log.info("$stateParams.taskId:",$stateParams.taskId);
    //
//    TaskService.get({taskId:$stateParams.taskId}, function (response) {
    TaskService.get({taskId:$stateParams.taskId}, function (response) {
        $log.debug("TaskService.getTaskInfo success!",response);
        $scope.task = response;
        $log.debug("TaskDetailCtrl $scope.task",$scope.task);
    });
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
     * Remove Group
     * @param group
     */
    $scope.removeGroup = function (group) {
        GroupService.delete({"group": group.id}, function (data) {
            $scope.groups = GroupService.get();
        });
    };
})
;