angular.module('starter.controllers', [])
//
    .controller('MainCtrl', function ($scope, $http, $rootScope, $location, $ionicModal, $ionicLoading, $ionicNavBarDelegate,
                                      CONFIG_ENV, $log, $cordovaToast) {
//Websocket/Stomp testing:
        var client = Stomp.client(CONFIG_ENV.stomp_uri, CONFIG_ENV.stomp_protocol);
        client.connect("employee1", "passwordpassword",
            function () {
                client.subscribe("SAMPLEQUEUE",
                    function (message) {
                        $log.debug(message);
                    },
                    {priority: 9}
                );
                client.send("SAMPLEQUEUE", {priority: 9}, "Pub/Sub over STOMP!");
            }
        );
//app.controller('MainController', function($scope, $http, $rootScope, $location,$ionicModal,$WebSocket){
//    console.log("MainController init!!!");
///GoBack
        $rootScope.goBack = function () {
            $ionicNavBarDelegate.back();
        };
///Loading
        $rootScope.showLoading = function () {
            $ionicLoading.show({
                template: 'Loading...'
            });
        };
        $rootScope.hideLoading = function () {
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
        }).then(function (modal) {
//        console.log("modal-login.html init!!!");
            $scope.loginModal = modal;
            $scope.loginModal.user = {
                username: "employee0",
                password: "passwordpassword"
            };
            //Login Modal
            if (window.localStorage['auth']) {
                $scope.loginModal.hide();
            } else {
//     $urlRouterProvider.otherwise('/login');
                $scope.loginModal.show();
            }
        });
        ///ItemModal
        $ionicModal.fromTemplateUrl('templates/modal-item.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-item.html init!!!");
            $scope.itemModal = modal;
        });
        ///TaskModal
        $ionicModal.fromTemplateUrl('templates/modal-task.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-task.html init!!!");
            $scope.taskModal = modal;
        });
        ///ReportModal
        $ionicModal.fromTemplateUrl('templates/modal-report.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-task.html init!!!");
            $scope.reportModal = modal;
        });
///Basic
        $rootScope.$on("$stateChangeStart", function () {
            //Login Modal,only hide();
            if (window.localStorage['auth']) {
                $scope.loginModal.hide();
            }
            //ShowLoading
            $rootScope.showLoading();
        });

        $rootScope.$on("$stateChangeSuccess", function () {
            //ShowLoading
            $rootScope.hideLoading();
        });

///FixtureData

        $scope.userAgent = navigator.userAgent;

        //Cleanup the modal when we're done with it!
        $scope.$on('$destroy', function () {
            $scope.loginModal.remove();
            $scope.taskModal.remove();
            $scope.reportModal.remove();
        });
        // Execute action on hide modal
        $scope.$on('modal.hidden', function () {
            // Execute action
        });
        // Execute action on remove modal
        $scope.$on('modal.removed', function () {
            // Execute action
        });
///App Toast,@see: http://blog.nraboy.com/2014/09/show-native-toast-notifications-using-ionic-framework/
        $scope.showToast = function (message, duration, location) {
            //TODO:device,web switch handler apply.
            $ionicLoading.show({template: message, noBackdrop: true, duration: duration == 'long' ? 5000 : 2000});
//        $cordovaToast.show(message, duration, location).then(function(success) {
//            console.log("The toast was shown");
//        }, function (error) {
//            console.log("The toast was not shown due to " + error);
//        });
        }
    })
//TabsCtrl,@see:http://codepen.io/anon/pen/GpmLn
    .controller('TabsCtrl', function ($scope, $ionicTabsDelegate) {
        $scope.goHome = function () {
            console.log($ionicTabsDelegate.$getByHandle('my-tabs'));
            console.log($ionicTabsDelegate.$getByHandle('my-tabs').selectedIndex());
            $ionicTabsDelegate.$getByHandle('my-tabs').select(0);
        }
    })
    .controller('TabCtrlDash', function ($scope, $rootScope, $ionicViewService) {
        $rootScope.DashHistoryID = $ionicViewService.getCurrentView().historyId;
        console.log('TabCtrlDash,homeHistoryID:', $rootScope.DashHistoryID);
    })
    .controller('TabLocalCtrlDash', function ($scope, $rootScope, $state, $ionicViewService) {
        console.log('TabLocalCtrlDash init!');
        $scope.onTabSelected = function () {
            $state.go('tab.dash');
            $ionicViewService.goToHistoryRoot($rootScope.DashHistoryID);
        }
    })
    .controller('TabLocalCtrlReports', function ($scope, $rootScope, $state, $ionicViewService) {
        console.log('TabLocalCtrlReports init!');
        $scope.onTabSelected = function () {
            $state.go('tab.reports');
            $ionicViewService.goToHistoryRoot($rootScope.ReportHistoryID);
        }
    })
    .controller('TabCtrlReports', function ($scope, $rootScope, $ionicViewService, ReportService, $log, $http) {
//    $rootScope.ReportHistoryID = $ionicViewService.getCurrentView().historyId;
//    console.log('TabCtrlReports,ReportHistoryID:',$rootScope.ReportHistoryID);
        //    $log.debug("$rootScope.username:",$rootScope.username);
        ReportService.get({assignee: $rootScope.username}, function (response) {
            $log.debug("TaskService.get() success!", response);
            $rootScope.reports = response.data;
        });
        //
        $scope.orderValue = 'asc';//desc
        //ORDER
        $scope.orderReports = function () {
            $scope.orderValue = ($scope.orderValue == 'asc') ? 'desc' : 'asc';
            //
            ReportService.get({assignee: $rootScope.username, order: $scope.orderValue}, function (response) {
                $log.debug("ReportService.get(order) success!", response);
                $rootScope.reports = response.data;
            });
        };
        /**
         * Used to determine whether to show the claim button or not
         */
//    $scope.isUnclaimedTask = function() {
//        return $scope.candidateGroup && $scope.candidateGroup.id != noGroupId;
//    };

        /**
         * Claim a task
         */
        $scope.claimTask = function (taskId) {
//        $http.put('service/task/' + taskId + "/claim").
//        success(function (data, status, headers, config) {
//            // After a successful claim, simply refresh the task list with the current search params
//            $log.debug('Claim task success: ' + status);
//        }).
//        error(function (data, status, headers, config) {
//            $log.info('Couldn\'t claim task : ' + status);
//        });

            var action = new ReportService();
            action.action = "claim";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.claim() success!", resp);
                //refresh reports list view.
                ReportService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.reports = response.data;
                });
            });
        };
        //CompleteTask
        $scope.completeTask = function (taskId) {
            var action = new ReportService();
            action.action = "complete";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.complete() success!", resp);
                //refresh reports list view.
                ReportService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.reports = response.data;
                });
            });

        };
        /**
         * Resolve a task
         */
        $scope.resolveTask = function (taskId) {
            var action = new ReportService();
            action.action = "resolve";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.resolve() success!", resp);
                //refresh reports list view.
                ReportService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.reports = response.data;
                });
            });
        };
        //ng-model
        $scope.newReport = {
            "name": "",
            "description": "",
            "dueDate": "",
            "owner": $rootScope.username,
            "parentTaskId": ""
        };
        //CREATE, //@see:http://www.activiti.org/userguide/#N1693F
        $scope.createReport = function () {
            //
            var anewReport = new ReportService($scope.newTask);
            anewReport.name = $scope.newReport.name;
            anewReport.description = $scope.newReport.description;
            anewReport.dueDate = $scope.newReport.dueDate;
            anewReport.owner = $scope.newReport.owner;
            anewReport.assignee = $rootScope.username;
            $log.debug("createReport(),$scope.newReport:", $scope.newReport);
            //Save
            anewReport.$save(function (r, putResponseHeaders) {
                $log.info("createReport() success, response:", r);
                //Hide report modal
                $scope.reportModal.hide();
                //Refresh report list
                ReportService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("ReportService.get() success!", response);
                    $rootScope.reports = response.data;
                });
                //Reset value
                $scope.newTask = {"name": "", "description": "", "dueDate": "", "owner": $rootScope.username};
            });
        }
        //Add an involved user to a process instance
        //@see: http://activiti.org/userguide/index.html#N1400A
        //POST runtime/process-instances/{processInstanceId}/identitylinks
        $scope.addInvolvedUsers = function (args){
            //

        }
    })
    .controller('ReportDetailCtrl', function ($scope, $rootScope, $stateParams, ReportService, $log) {
        ReportService.get({taskId: $stateParams.reportId}, function (response) {
//        $log.debug("ReportService.getTaskInfo success!",response);
            $scope.report = response;
//        $log.debug("ReportDetailCtrl $scope.report",$scope.report);
        });
    })
//@example:http://krispo.github.io/angular-nvd3/#/
    .controller('StatsCtrl', function ($scope) {
        /* Chart options */
        $scope.options = {
            chart: {
                type: 'discreteBarChart',
                // height:450,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 50,
                    left: 55
                },
                x: function (d) {
                    return d.label;
                },
                y: function (d) {
                    return d.value;
                },
                showValues: true,
                valueFormat: function (d) {
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
                {"label": "A", "value": 29.765957771107},
                {"label": "B", "value": 0},
                {"label": "C", "value": 32.807804682612},
                {"label": "D", "value": 196.45946739256},
                {"label": "E", "value": 0.19434030906893},
                {"label": "F", "value": 98.079782601442},
                {"label": "G", "value": 13.925743130903},
                {"label": "H", "value": 5.1387322875705}
            ]
        }];
    })

    .controller('UsersCtrl', function ($rootScope, $scope, $http, UserService, $rootScope, $location) {
//    console.log("$rootScope.loggedUser",$rootScope.loggedUser);
        if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
            $location.path('/login');
            return;
        }
        $scope.users = UserService.get();
        /**
         * New user Initial data
         */
        $scope.newUser = {
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
            user.id = newUser.id;
            user.firstName = newUser.firstName;
            user.lastName = newUser.lastName;
            user.email = newUser.email;
            user.password = newUser.password;
            ///
            user.$save(function (u, putResponseHeaders) {
                $scope.users.data.push(u);
            });
        };

        $scope.removeUser = function (user) {
            UserService.delete({"user": user.id}, function (data) {
                $scope.users = UserService.get();
            });
        }
    })


    .controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location, $log,
                                       TaskService, ProcessService, JobService, ExecutionService,
                                       HistoryService, FormDataService, ItemService) {
        $rootScope.loggedUser = {};
        $rootScope.loggedin = false;

        $scope.userLogin = function () {
//        $log.debug("$scope.loginModal.user.username:",$scope.loginModal.user.username,",$scope.loginModal.user.password:",$scope.loginModal.user.password);
            $http.defaults.headers.common['Authorization'] = 'Basic ' + Base64.encode($scope.loginModal.user.username + ":" + $scope.loginModal.user.password);

            UserService.get({user: $scope.loginModal.user.username}, function (response) {

                $log.debug("UserService.get(login) success!", response);
                $rootScope.loggedin = true;
                $rootScope.loggedUser = response;
                $rootScope.username = $scope.loginModal.user.username;
                $rootScope.password = $scope.loginModal.user.password;
                $location.path('/dashboard');
                //Remove login modal
                $scope.loginModal.hide();
                //Default getTasks;
                $log.debug("$rootScope.username:", $rootScope.username, ",$rootScope.password:", $rootScope.password);
                //
                TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
                //
                ItemService.get({}, function (response) {
                    $log.debug("ItemService.get() success!", response);
                    $rootScope.items = response.data;
                });
                //getProcesses test
                ProcessService.get({user: $rootScope.username}, function (response) {
                    $log.debug("ProcessService.get() success!", response);
                    $rootScope.processes = response.data;
                });
                //getJobs test
                JobService.get({user: $rootScope.username}, function (response) {
                    $log.debug("JobService.get() success!", response);
                    $rootScope.jobs = response.data;
                });
                //getExecutions test
                ExecutionService.get({user: $rootScope.username}, function (response) {
                    $log.debug("ExecutionService.get() success!", response);
                    $rootScope.executions = response.data;
                });
                //getHistory(historic-process-instances) test
                HistoryService.get({user: $rootScope.username}, function (response) {
                    $log.debug("HistoryService.get() success!", response);
                    $rootScope.historices = response.data;
                });
                //formData test
//            FormDataService.get({"taskId": 2513}, function (data) {
//                $log.debug("FormDataService.get() success!",data);
//            });

            });
        };
    })
    .controller('ItemsCtrl', function ($scope, $http, Base64, $rootScope, $location, $log,
                                       ItemService) {
        //ng-model
        $scope.newItem = {"name": "", "vendors": "", "invoices": "", "date": ""};
        //CREATE,
        $scope.createItem = function () {
            $log.debug("createItem(),$scope.newItem:", $scope.newItem);
            var anewItem = new ItemService($scope.newItem);
            anewItem.name = $scope.newItem.name;
            anewItem.vendors = $scope.newItem.vendors;
            anewItem.invoices = $scope.newItem.invoices;
            anewItem.date = $scope.newItem.date;
            //
            //Save
            anewItem.$save(function (t, putResponseHeaders) {
                $log.info("createItem() success, response:", t);
                //Hide item modal
                $scope.itemModal.hide();
                //Refresh task list
                ItemService.get({}, function (response) {
                    $log.debug("ItemService.get() success!", response);
                    $rootScope.items = response.data;
                });
                //Reset value
                $scope.newItem = {"name": "", "vendors": "", "invoices": "", "date": ""};
            });
        }
        //DELETE
        $scope.removeItem = function (itemId) {
            ItemService.delete({"itemId": itemId}, function (data) {
                $log.debug("ItemService.delete:", data);
                //Refresh item list
                ItemService.get({}, function (response) {
                    $log.debug("ItemService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
            });
            //
        }
        $scope.orderValue = 'asc';//desc
        //ORDER
        $scope.orderItems = function () {
            $scope.orderValue = ($scope.orderValue == 'asc') ? 'desc' : 'asc';
            //
            ItemService.get({order: $scope.orderValue}, function (response) {
                $log.debug("ItemService.get(order) success!", response);
                $rootScope.items = response.data;
            });
        }
        //LoadTask()
        $scope.loadItem = function (item) {
            $scope.items = ItemService.get();
        };
        ///Initialization call.
        $scope.loadItem();
    })
    .controller('TasksCtrl', function ($scope, $http, Base64, $rootScope, $location, $log,
                                       ProcessDefinitionService, TasksService, FormDataService,
                                       TasksModalService, ProcessDefinitionService, GroupService, TaskService) {
        //ng-model
        $scope.newTask = {
            "name": "",
            "description": "",
            "dueDate": "",
            "owner": $rootScope.username,
            "parentTaskId": ""
        };
        //CREATE, //@see:http://www.activiti.org/userguide/#N1693F
        $scope.createTask = function () {
            $log.debug("createTask(),$scope.newTask:", $scope.newTask);
            var anewTask = new TaskService($scope.newTask);
            anewTask.name = $scope.newTask.name;
            anewTask.description = $scope.newTask.description;
            anewTask.dueDate = $scope.newTask.dueDate;
            anewTask.owner = $scope.newTask.owner;
            //
            $http.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            //Save
            anewTask.$save(function (t, putResponseHeaders) {
                $log.info("createTask() success, response:", t);
                //Hide task modal
                $scope.taskModal.hide();
                //Refresh task list
                TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
                //Reset value
                $scope.newTask = {"name": "", "description": "", "dueDate": "", "owner": $rootScope.username};
            });
        }
        //DELETE runtime/tasks/{taskId}?cascadeHistory={cascadeHistory}&deleteReason={deleteReason}
        $scope.removeTask = function (taskId) {
            TaskService.delete({"taskId": taskId, cascadeHistory: true, deleteReason: 'testing'}, function (data) {
                $log.debug("TaskService.delete:", data);
                //Refresh task list
                TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
            });
            //
        }
        $scope.orderValue = 'asc';//desc
        //ORDER
        $scope.orderTasks = function () {
            $scope.orderValue = ($scope.orderValue == 'asc') ? 'desc' : 'asc';
            //
            TaskService.get({order: $scope.orderValue}, function (response) {
                $log.debug("TaskService.get(order) success!", response);
                $rootScope.tasks = response.data;
            });
        }
        //LoadTask()
        $scope.loadTask = function (task) {
            $log.info("loadTask():", task);
            FormDataService.get({"taskId": task.id}, function (data) {
                extractForm(task, data);
            }, function (data) {

                if (data.data.statusCode == 404) {
                    $log.error("there was an error!", data.data);
                }

            });
        };
        function extractForm(task, data) {
            var propertyForSaving = {};

            for (var i = 0; i < data.formProperties.length; i++) {
                var elem = data.formProperties[i];
                propertyForSaving[elem.id] = {
                    "value": elem.value,
                    "id": elem.id,
                    "writable": elem.writable
                };

                if (elem.datePattern != null) {//if date
                    propertyForSaving[elem.id].opened = false; //for date picker
                    propertyForSaving[elem.id].datePattern = elem.datePattern;
                }

                if (elem.required == true && elem.type == "boolean") {
                    if (elem.value == null) {
                        propertyForSaving[elem.id].value = false;
                    }
                }

                if (elem.type == "user") {
                    elem.enumValues = UserService.get();
                }
            }

            task.form = data;
            task.propertyForSaving = propertyForSaving;
        }

        /**
         * involved
         * owned
         * assigned
         *
         * @type {string}
         */
        $scope.tasksType = "assigned";

        function getTasksQuery() {
            if ($scope.tasksType == "involved") {
                return {"size": 1000, "involvedUser": $rootScope.username};
            } else if ($scope.tasksType == "owned") {
                return {"size": 1000, "owner": $rootScope.username};
            } else if ($scope.tasksType == "unassigned") {
                return {"size": 1000, "unassigned": true};
            } else {//assigned
                return {"size": 1000, "assignee": $rootScope.username};
            }
        }


        /**
         * Performs the load of the tasks and sets the tasksType
         * @param tasksType
         */
        $scope.loadTasksType = function (tasksType) {
            $scope.tasksType = tasksType;
            $scope.loadTasks();
        }

        /**
         * Loads the tasks
         */
        $scope.loadTasks = function () {
            //$scope.tasks = TasksService.get(getTasksQuery());
            loadTasks(getTasksQuery());
        }

        var loadTasks = function (params) {
            $scope.tasks = TasksService.get(params);
        }

        $scope.loadTask = function (task) {
            TasksModalService.loadTaskForm(task);
        };


        $rootScope.$on('refreshData', function (event, data) {
            $scope.loadTasks();

        });

        /**
         * Load definitions
         */
        $scope.loadDefinitions = function () {
            $scope.processes = ProcessDefinitionService.get({latest: "true"});
        }

        /**
         * starts the process
         * @param processDefinition
         */
        $scope.startTheProcess = function (processDefinition) {

            TasksModalService.loadProcessForm(processDefinition);

            var formService = new FormDataService({processDefinitionId: processDefinition.id});
            formService.$startTask(function (data) {
                console.log(data);
            });
        };

        $scope.loadUserGroups = function () {
            $scope.userGroups = GroupService.get({"member": $rootScope.username});
        }

        $scope.loadTasksGroups = function (group) {
            console.log(group);
            loadTasks({"size": 1000, "candidateGroup": group.id});
        }

        ///Initialization call.
        $scope.loadUserGroups();
        $scope.loadTasks();
        $scope.loadDefinitions();
    })
    .controller('TaskDetailCtrl', function ($scope, $rootScope, $stateParams, TaskService, $log) {
        $log.info("$stateParams.taskId:", $stateParams.taskId);
        //
//    TaskService.get({taskId:$stateParams.taskId}, function (response) {
        TaskService.get({taskId: $stateParams.taskId}, function (response) {
            $log.debug("TaskService.getTaskInfo success!", response);
            $scope.task = response;
            $log.debug("TaskDetailCtrl $scope.task", $scope.task);
        });
    })

    .controller('GroupsCtrl', function ($scope, $rootScope, $location, GroupService, $ionicModal) {
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

    .controller('TabCtrlProcesses', function ($scope, $rootScope, $location, ProcessDefinitionService,
                                              ProcessInstanceService, FormDataService, $ionicModal, moment,
                                              TasksModalService) {
        if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
            $location.path('/login');
            return;
        }

        $scope.loadDefinitions = function () {
            $scope.processes = ProcessDefinitionService.get({latest: "true"});
        }

        $scope.loadDefinitions();

        $scope.startTheProcess = function (processDefinition) {

            TasksModalService.loadProcessForm(processDefinition);

            var formService = new FormDataService({processDefinitionId: processDefinition.id});
            formService.$startTask(function (data) {
                console.log(data);
            });
        };

        $scope.activateTheProcessDefinition = function (processDefinition) {
            var action = new ProcessDefinitionService();
            action.action = "activate";
            action.includeProcessInstances = "true";
            action.$update({"processDefinitionId": processDefinition.id}, function () {
                $scope.loadDefinitions();
            });
        }

        $scope.query = "";
    })
    .controller('ProcessDetailCtrl', function ($scope, $rootScope, $stateParams, ProcessInstanceService, $log) {
        ProcessInstanceService.get({processInstanceId: $stateParams.processInstanceId}, function (response) {
            $log.debug("ProcessInstanceService.getProcessInstanceInfo success!", response);
            $scope.processInstance = response;
            $log.debug("ProcessDetailCtrl $scope.processInstance", $scope.processInstance);
        });
    })
    .controller('TabCtrlInstances', function ($scope, $rootScope, $location, $ionicModal, moment,
                                              ProcessInstancesService, ProcessInstanceService, ProcessDefinitionService) {
        if (typeof  $rootScope.loggedin == 'undefined' || $rootScope.loggedin == false) {
            $location.path('/login');
            return;
        }

        $scope.loadDefinitions = function () {
            ProcessInstancesService.get({size: 1000, latest: "true", sort: "id"}, function (instances) {
                $scope.instances = instances;
                ProcessDefinitionService.get({latest: "true"}, function (data) {
                    for (var i = 0; i < data.data.length; i++) {
                        var definition = data.data[i];
                        for (var j = 0; j < instances.data.length; j++) {
                            if (instances.data[j].processDefinitionId == definition.id) {
                                instances.data[j].name = definition.name;
                            }
                        }
                    }
                });
            });
        }

        $scope.loadDefinitions();


        var InstancesDetailsCtrl = function ($scope, $modalInstance, instance) {
            $scope.instance = instance;
            $scope.ok = function () {
                $modalInstance.close(group);
            };
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.delete = function (instance) {


                $modalInstance.dismiss('cancel');
            };

            $scope.diagram = "/service/process-instance/" + instance.id + "/diagram";


            $scope.instance.details = ProcessInstanceService.get({processInstance: instance.id});
        }

        $scope.showDetails = function (instance) {
            var modalInstance = $ionicModal.open({
                templateUrl: 'views/modals/instanceDetails.html',
                controller: InstancesDetailsCtrl,
                resolve: {
                    instance: function () {
                        return instance;
                    }
                }
            });
            modalInstance.result.then(function (newGroup) {

            }, function () {
            });
        };

        $scope.query = "";
    })
    .controller('InstanceDetailCtrl', function ($scope, $rootScope, $stateParams, ProcessInstanceService, $log) {
        ProcessInstanceService.get({processInstanceId: $stateParams.processInstanceId}, function (response) {
            $log.debug("ProcessInstanceService.getProcessInstanceInfo success!", response);
            $scope.processInstance = response;
            $log.debug("InstanceDetailCtrl $scope.processInstance", $scope.processInstance);
        });
    })
;
