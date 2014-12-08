angular.module('starter.controllers', [])
//
    .controller('MainCtrl', function ($scope, $http, $rootScope, $location, $ionicModal, $ionicLoading, $ionicNavBarDelegate,
                                      CONFIG_ENV, $log, $cordovaToast) {
//Websocket/Stomp handler:
        $rootScope.connectStomp = function (username, password) {
            var client = Stomp.client(CONFIG_ENV.stomp_uri, CONFIG_ENV.stomp_protocol);
            client.connect(username, password,
                function () {
                    client.subscribe("SAMPLEQUEUE",
                        function (message) {
                            //$log.debug(message);
                            //console.log(message.body);
                            if (window.plugins && window.plugins.toast) {
                                window.plugins.toast.showShortCenter(message.body);
                            }
                            else {
                                $ionicLoading.show({template: message.body, noBackdrop: true, duration: 10000});
                            }
                        },
                        {priority: 9}
                    );
                    //client.send("SAMPLEQUEUE", {priority: 9}, "Pub/Sub over STOMP from Ionic!");//For testing...
                }
            );
        }
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
                username: "employee1",
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
        ///ItemListModal
        $ionicModal.fromTemplateUrl('templates/modal-item-list.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-item-list.html init!!!");
            $scope.itemListModal = modal;
        });
        ///TaskModal
        $ionicModal.fromTemplateUrl('templates/modal-task.html', {
            scope: $scope
            , backdropClickToClose: false
            , animation: 'slide-in-up'
            , focusFirstInput: true
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
            $scope.itemModal.remove();
            $scope.itemListModal.remove();
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
        //Badge numbers for task notification.
        $rootScope.numberOfTasks = 0;
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
    .controller('TabLocalCtrlTasks', function ($scope, $rootScope, $state, $ionicViewService) {
        console.log('TabLocalCtrlTasks init!');
        $scope.onTabSelected = function () {
            $state.go('tab.tasks');
            $ionicViewService.goToHistoryRoot($rootScope.TaskHistoryID);
        }
    })
    .controller('TabCtrlTasks', function ($scope, $rootScope, $ionicViewService, TaskService, $log, $http) {
        //
        $scope.orderValue = 'asc';//desc
        //ORDER
        $scope.orderTasks = function () {
            $scope.orderValue = ($scope.orderValue == 'asc') ? 'desc' : 'asc';
            //
            TaskService.get({assignee: $rootScope.username, order: $scope.orderValue}, function (response) {
                $log.debug("TaskService.get(order) success!", response);
                $rootScope.tasks = response.data;
            });
        };
        /**
         * Used to determine whether to show the claim button or not
         */
        //$scope.isUnclaimedTask = function () {
        //    return $scope.candidateGroup && $scope.candidateGroup.id != noGroupId;
        //};

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

            var action = new TaskService();
            action.action = "claim";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.claim() success!", resp);
                //refresh reports list view.
                TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.reports = response.data;
                });
            });
        };
        //CompleteTask
        $scope.completeTask = function (taskId) {
            var action = new TaskService();
            action.action = "complete";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.complete() success!", resp);
                //refresh reports list view.
                TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.reports = response.data;
                });
            });

        };
        /**
         * Resolve a task
         */
        $scope.resolveTask = function (taskId) {
            var action = new TaskService();
            action.action = "resolve";
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.resolve() success!", resp);
                //refresh reports list view.
                TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
            });
        };
        //Add an involved user to a process instance
        //@see: http://activiti.org/userguide/index.html#N1400A
        //POST runtime/process-instances/{processInstanceId}/identitylinks
        $scope.addInvolvedUsers = function (args) {
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
                                       HistoryService, FormDataService, ItemService, CompanyService) {
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
                //ProcessService.get({user: $rootScope.username}, function (response) {
                //    $log.debug("ProcessService.get() success!", response);
                //    $rootScope.processes = response.data;
                //});
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
                //getCompanyInfo(businessKey,processDefinitionKey)
                $rootScope.companyInfo = {};
                CompanyService.get({}, function (response) {
                    $log.debug("CompanyService.get(default) success!", response.data[0]);
                    $rootScope.companyInfo = response.data[0];//Default value index is 0.
                });
                //formData test
//            FormDataService.get({"taskId": 2513}, function (data) {
//                $log.debug("FormDataService.get() success!",data);
//            });
                //Connect to STOMP server.
                $rootScope.connectStomp($rootScope.username, $rootScope.password);

            });
        };
    })
    .controller('ItemDetailCtrl', function ($scope, $rootScope, $stateParams, ItemService, $log) {
        $log.info("$stateParams.itemId:", $stateParams.itemId);
        //
        ItemService.get({itemId: $stateParams.itemId}, function (response) {
            $log.debug("ItemService.getTaskInfo success!", response);
            $scope.item = response;
            $log.debug("ItemDetailCtrl $scope.item", $scope.item);
        });
    })
    .controller('ItemsCtrl', function ($scope, $http, Base64, $rootScope, $location, $log,
                                       ItemService) {
        //ng-model
        $scope.newItem = {"name": "", "vendors": "", "invoices": "", "date": "", "owner": ""};
        $scope.preferences = {
            type: [
                {
                    name: "预审批",
                    data: "ApproveAhead"
                },

                {
                    name: "已消费",
                    data: "CostComsumed"
                }
            ]
        };
        $scope.prefType = $scope.preferences.type[1];
        $scope.setTypeSelected = function (type) {
            $scope.prefType = type;
        }
        //CREATE,
        $scope.createItem = function () {
            $log.debug("createItem(),$scope.newItem:", $scope.newItem);
            var anewItem = new ItemService($scope.newItem);
            anewItem.amount = $scope.newItem.amount;
            anewItem.name = $scope.newItem.name;
            anewItem.vendors = $scope.newItem.vendors;
            anewItem.invoices = $scope.newItem.invoices;
            anewItem.date = $scope.newItem.date;
            anewItem.owner = $rootScope.username;
            anewItem.type = $scope.prefType.data;
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
                $scope.newItem = {"name": "", "vendors": "", "invoices": "", "date": "", "owner": ""};
            });
        }
        //DELETE
        $scope.removeItem = function (itemId) {
            ItemService.delete({"itemId": itemId}, function (data) {
                $log.debug("ItemService.delete:", data);
                //Refresh item list
                ItemService.get({}, function (response) {
                    $log.debug("ItemService.get() success!", response);
                    $rootScope.items = response.data;
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
        $scope.loadItems = function (item) {
            $rootScope.items = ItemService.get();
        };
        ///Initialization call.
        //$scope.loadItems();
    })
    .controller('TasksCtrl', function ($scope, $http, Base64, $rootScope, $location, $log,
                                       ProcessDefinitionService, TasksService, FormDataService,
                                       TasksModalService, GroupService,
                                       TaskService, ProcessInstancesService) {
        //AddItems
        $scope.addItemFromList = function () {
            $ionicPopup.show({
                templateUrl: 'modal-item-list.html',
                title: 'ItemList',
                scope: $scope,
                buttons: [{
                    text: 'Ok',
                    type: 'button-positive',
                    onTap: function () {
                        return true;
                    }
                }]
            });
        }
        //submitStartForm to start process
        $scope.startProcessInstance = function () {
            var anewProcessInstance = new ProcessInstancesService();
            anewProcessInstance.processDefinitionKey = $rootScope.companyInfo.processDefinitionKey;
            anewProcessInstance.businessKey = $rootScope.companyInfo.businessKey;
            anewProcessInstance.variables = [];
            //Save
            anewProcessInstance.$save(function (t, putResponseHeaders) {
                $log.info("startProcessInstance() success, response:", putResponseHeaders, t);
            });
        }
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
