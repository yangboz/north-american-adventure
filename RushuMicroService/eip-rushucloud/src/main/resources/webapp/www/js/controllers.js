angular.module('starter.controllers', [])
//
    .controller('MainCtrl', function ($scope, $http, $rootScope, $location, $ionicModal, $ionicLoading, $ionicNavBarDelegate,
                                      CONFIG_ENV, $log, $cordovaToast) {
//Websocket/Stomp handler:
        $rootScope.connectStomp = function (username, password, queueName) {
            var client = Stomp.client(CONFIG_ENV.stomp_uri, CONFIG_ENV.stomp_protocol);
            client.connect(username, password,
                function () {
                    client.subscribe(queueName,
                        function (message) {
                            $log.debug(JSON.parse(message.body));
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
                    //client.send(queueName, {priority: 9}, "Pub/Sub over STOMP from"+username);//For testing...
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
            if (window.localStorage[CONFIG_ENV.WIN_LOCAL_STORAGE_NAME]) {
                $scope.loginModal.hide();
            } else {
//     $urlRouterProvider.otherwise('/login');
                $scope.loginModal.show();
            }
        });
        ///ItemListModal
        $ionicModal.fromTemplateUrl('templates/modal-item-list.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-item-list.html init!!!");
            $scope.itemListModal = modal;
        });
        ///UserListModal
        $ionicModal.fromTemplateUrl('templates/modal-user-list.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-user-list.html init!!!");
            $scope.userListModal = modal;
        });
        ///ManagerListModal
        $ionicModal.fromTemplateUrl('templates/modal-manager-list.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-manager-list.html init!!!");
            $scope.managerListModal = modal;
        });
        ///VendorListModal
        $ionicModal.fromTemplateUrl('templates/modal-vendor-list.html', {
            scope: $scope,
            backdropClickToClose: false
        }).then(function (modal) {
//        console.log("modal-vendor-list.html init!!!");
            $rootScope.vendorListModal = modal;
        });
///Basic
        $rootScope.$on("$stateChangeStart", function () {
            //Login Modal,only hide();
            if (window.localStorage[CONFIG_ENV.WIN_LOCAL_STORAGE_NAME]) {
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
            $rootScope.itemListModal.remove();
            $rootScope.userListModal.remove();
            $rootScope.managerListModal.remove();
            $rootScope.vendorListModal.remove();
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
        $rootScope.items = [];
        $rootScope.expenses = [];
        $rootScope.itemIDsSel = [];//selected item ids.
        $rootScope.itemIDsSelAmount = 0;//total number of selected item's amount.
        $rootScope.expenses = [];
        $rootScope.employeeIDs = [];
        $rootScope.managerIDs = [];
        $rootScope.employeeIDsSel = [];
        $rootScope.managerIDsSel = [];
        $rootScope.vendors = [];
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
    .controller('TabCtrlTasks', function ($scope, $rootScope, $ionicViewService, TaskService, $log, CONFIG_ENV) {
        //Slide-box view
        $scope.selectedViewIndex = 0;
        $scope.changeViewIndex = function (index) {
            //$log.info("TabCtrlTasks selected view index:", index);
            $scope.selectedViewIndex = index;
        };
    })
    .controller('ExpenseCtrl', function ($scope, $rootScope, CONFIG_ENV, ExpenseService, $log, $http, CONFIG_ENV) {
        //DELETE
        $scope.removeExpense = function (expenseId) {
            //return $log.debug("expenseId:", expenseId);
            //ExpenseService.delete({"owner": $rootScope.username, "expenseId": expenseId}, function (data) {
            //    $log.debug("ExpenseService.delete:", data);
            //    //Refresh expense list
            //    ExpenseService.get({}, function (response) {
            //        $log.debug("ExpenseService.get() success!", response);
            //        $rootScope.expenses = response.data;
            //    });
            //});
            //
            $http.delete(CONFIG_ENV.api_endpoint + 'expenses/' + expenseId, {})
                .success(function (data, status) {
                    $log.debug("ExpenseService.delete:", data);
                    //Refresh expense list
                    ExpenseService.get({owner: $rootScope.username}, function (response) {
                        $log.debug("ExpenseService.get() success!", response);
                        $rootScope.expenses = response.data;
                    });
                })
                .error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $log.error("ExpenseService.delete failure", data);
                });
        }
    })
    //
    .controller('ExpenseDetailCtrl', function ($scope, $rootScope, $stateParams, ExpenseService, $log) {
        //$log.info("$stateParams.expenseId:", $stateParams.expenseId);
        //
        ExpenseService.get({expenseId: $stateParams.expenseId}, function (response) {
            $scope.expense = response;
            $log.debug("ExpenseDetailCtrl $scope.expense", $scope.expense);
        });
    })
//@example:http://krispo.github.io/angular-nvd3/#/
    .controller('ReportsCtrl', function ($scope, $rootScope, Enum, $log, ReportService) {
        /* Chart options */
        //@example:http://plnkr.co/edit/jOoJik?p=preview
        /* Chart options */
        $scope.reportChartOptions = {
            chart: {
                type: 'pieChart',
                height: 350,
                donut: true,
                x: function (d) {
                    return d[0];
                },
                y: function (d) {
                    return d[1];
                },
                showLabels: true,
                pie: {
                    startAngle: function (d) {
                        return d.startAngle / 2 - Math.PI / 2
                    },
                    endAngle: function (d) {
                        return d.endAngle / 2 - Math.PI / 2
                    }
                },
                transitionDuration: 500,
                legend: {
                    margin: {
                        top: 5,
                        right: 70,
                        bottom: 5,
                        left: 0
                    }
                }
            }
        };

        /* Chart data load*/
        $scope.loadReportsData = function () {
            ReportService.get({owner: $rootScope.username}, function (response) {
                $log.info("ReportService.get() success, response:", response);
                $rootScope.reportChartData = response.data;
            }, function (error) {
                // failure handler
                $log.error("ReportService.get() failed:", JSON.stringify(error));
            });
        }
    })
    .controller('UsersCtrl', function ($scope, $http, UserService, $rootScope, $location, $log) {
        //UserListModal related
        //@see: http://stackoverflow.com/questions/14514461/how-can-angularjs-bind-to-list-of-checkbox-values
        $scope.toggleEmployeeListSelection = function (userId) {
            var idx = $rootScope.employeeIDsSel.indexOf(userId);
            if (idx > -1) {
                $rootScope.employeeIDsSel.splice(idx, 1);
            } else {
                $rootScope.employeeIDsSel.push(userId);
            }
            $log.debug("toggleEmployeeListSelection:", $rootScope.employeeIDsSel);
            //IdentityLink

        }
        $scope.toggleManagerListSelection = function (userId) {
            var idx = $rootScope.managerIDsSel.indexOf(userId);
            if (idx > -1) {
                $rootScope.managerIDsSel.splice(idx, 1);
            } else {
                $rootScope.managerIDsSel.push(userId);
            }
            $log.debug("toggleManagerListSelection:", $rootScope.managerIDsSel);
        }
    })

    .controller('LoginCtrl', function ($scope, $http, UserService, Base64, $rootScope, $location, $log,
                                       TaskService, ProcessService, JobService, ExecutionService,
                                       HistoryService, FormDataService, ItemService, CompanyService,
                                       ProcessDefinitionsService, CONFIG_ENV, Enum, LDAPService, ExpenseService) {
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
                //getTaskService test
                TaskService.get({}, function (response) {
//            TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
                //default get items for usage.
                ItemService.get({owner: $rootScope.username}, function (response) {
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
                $rootScope.companyInfo.processDefinitionId = null;
                $rootScope.companyInfo.processDefinitionKey = null;
                //
                CompanyService.get({}, function (response) {
                    $log.debug("CompanyService.get(default) success!", response.data[0]);
                    $rootScope.companyInfo = response.data[0];//Default value index is 0.
                    //Then
                    //getProcessDefinitionsService
                    ProcessDefinitionsService.get({}, function (response) {
                        var lastIndex = response.data.length - 1;
                        var dataArr = response.data;
                        $log.debug("ProcessDefinitionsService.get(dataArr) success!", dataArr);
                        var dataOne = dataArr[lastIndex];
                        $log.debug("ProcessDefinitionsService.get(dataOne) success!", dataOne);
                        $log.debug("$rootScope.companyInfo:",$rootScope.companyInfo);
                        $rootScope.companyInfo.processDefinitionId = dataOne.id;
                        $rootScope.companyInfo.processDefinitionKey = dataOne.key;
                        //Then
                        $rootScope.activemqQueueName = $rootScope.companyInfo.processDefinitionKey
                        + "/" + $rootScope.companyInfo.processDefinitionId
                        + "/" + $rootScope.username;
                        $log.info("activemqQueueName:", $rootScope.activemqQueueName);
                        //Connect to STOMP server with ActiveMQ QueueName.
                        $rootScope.connectStomp($rootScope.username, $rootScope.password, $rootScope.activemqQueueName);
                    });
                });
                //LoadExpenses
                ExpenseService.get({owner: $rootScope.username}, function (response) {
                    //ExpenseService.get({owner: $rootScope.username}, function (response) {
                    $log.info("ExpenseService.get() success, response:", response);
                    $rootScope.expenses = response.data;
                }, function (error) {
                    // failure handler
                    $log.error("ExpenseService.get() failed:", JSON.stringify(error));
                });
                //formData test
//            FormDataService.get({"taskId": 2513}, function (data) {
//                $log.debug("FormDataService.get() success!",data);
//            });
                ///Search LDAP users by ou(organization unit)
                var ouEncode_0 = "ou=" + Enum.groupNames[0] + ",";//employee
                LDAPService.get({
                    partition: ouEncode_0 + CONFIG_ENV.LDAP_PARTITION,
                    filter: CONFIG_ENV.LDAP_FILTER
                }, function (response) {
                    $log.info("LDAPService.get(0) success, response:", response);
                    $rootScope.employeeIDs = response.data;
                }, function (error) {
                    // failure handler
                    $log.error("LDAPService.get(0) failed:", JSON.stringify(error));
                });
                var ouEncode_1 = "ou=" + Enum.groupNames[1] + ",";//manager
                LDAPService.get({
                    partition: ouEncode_1 + CONFIG_ENV.LDAP_PARTITION,
                    filter: CONFIG_ENV.LDAP_FILTER
                }, function (response) {
                    $log.info("LDAPService.get(1) success, response:", response);
                    $rootScope.managerIDs = response.data;
                }, function (error) {
                    // failure handler
                    $log.error("LDAPService.get(1) failed:", JSON.stringify(error));
                });
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
                                       ItemService, Enum, $ionicNavBarDelegate) {
        //ng-model
        $rootScope.newItem = {"name": "", "vendors": "", "invoices": "", "date": "", "owner": ""};
        $scope.preferencesItemType = Enum.itemType;
        $scope.prefType = Enum.itemType[0];//Default setting.
        $scope.setTypeSelected = function (type) {
            $scope.prefType = type;
        }
        //Query
        //CREATE,
        $scope.createItem = function () {
            $log.debug("createItem(),$scope.newItem:", $scope.newItem);
            var anewItem = new ItemService($scope.newItem);
            anewItem.amount = $rootScope.newItem.amount;
            anewItem.name = $rootScope.newItem.name;
            anewItem.invoices = $rootScope.newItem.invoices;
            anewItem.vendors = $rootScope.newItem.vendors;
            anewItem.date = $rootScope.newItem.date;
            anewItem.owner = $rootScope.username;
            anewItem.type = $scope.prefType.data;
            //
            //Save
            anewItem.$save(function (t, putResponseHeaders) {
                $log.info("createItem() success, response:", t);
                //Refresh item list
                ItemService.get({owner: $rootScope.username}, function (response) {
                    $log.debug("ItemService.get() success!", response);
                    $rootScope.items = response.data;
                    //View history back to Expense tab inside of task table.
                    $ionicNavBarDelegate.back();
                });
                //Reset value
                $scope.newItem = {"name": "", "vendors": "", "invoices": "", "date": "", "owner": ""};
            });
        }
        //DELETE
        $scope.removeItem = function (itemId) {
            ItemService.delete({itemId: itemId}, function (data) {
                $log.debug("ItemService.delete:", data);
                //Refresh item list
                ItemService.get({owner: $rootScope.username}, function (response) {
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
        //ItemListModal related
        //@see: http://stackoverflow.com/questions/14514461/how-can-angularjs-bind-to-list-of-checkbox-values
        $scope.toggleItemListSelection = function (itemId, index) {
            var idx = $rootScope.itemIDsSel.indexOf(itemId);
            if (idx > -1) {
                $rootScope.itemIDsSel.splice(idx, 1);
                $rootScope.itemIDsSelAmount -= $rootScope.items[index].amount;
            } else {
                $rootScope.itemIDsSel.push(itemId);
                $rootScope.itemIDsSelAmount += $rootScope.items[index].amount;
            }
            $log.debug("toggleItemListSelection:", $rootScope.itemIDsSel, $rootScope.itemIDsSelAmount);
        }
    })
    .controller('TasksCtrl', function ($scope, $rootScope, $http, Base64, $location, $log,
                                       $ionicNavBarDelegate,
                                       ProcessDefinitionService, TasksService, FormDataService,
                                       TasksModalService, GroupService,
                                       TaskService, ProcessInstancesService, ExpenseService, Enum,
                                       ProcessDefinitionIdentityLinkService,
                                       $ionicActionSheet, $ionicPopup) {
        //Local variables
        $scope.processInstanceVariables = {};
        //Save the expense item at first.
        $scope.saveExpenseReport = function (startProcessInstance) {
            //return $log.debug($rootScope.companyInfo.processDefinitionId);
            var anewExpense = new ExpenseService();
            anewExpense.name = $scope.processInstanceVariables.name;
            anewExpense.owner = $rootScope.username;
            anewExpense.date = $scope.processInstanceVariables.date;
            anewExpense.itemIds = $rootScope.itemIDsSel.toString();
            anewExpense.managerId = $rootScope.managerIDsSel[0];
            anewExpense.participantIds = $rootScope.employeeIDsSel.toString();
            anewExpense.status = startProcessInstance ? Enum.expenseStatus.Submitted : Enum.expenseStatus.Saved;
            anewExpense.amount = $rootScope.itemIDsSelAmount;
            //Save
            anewExpense.$save(function (t, putResponseHeaders) {
                $log.info("saveExpenseItem() success, response:", t);
                //SubmitStartForm to start process if necessary.
                if (startProcessInstance) {
                    $scope.startProcessInstance(t.id);
                } else {
                    //View history back to Expense tab inside of task table.
                    $ionicNavBarDelegate.back();
                }
            }, function (error) {
                // failure handler
                $log.error("saveExpenseItem() failed:", JSON.stringify(error));
            });
        }
        //Add a candidate starter to a process definition
        $scope.identityLinks = function () {
            //return $log.info($rootScope.companyInfo.processDefinitionId);
            //Add a candidate starter to a process definition
            var anewProcessDefintionIdentityLinkService = new ProcessDefinitionIdentityLinkService();
            anewProcessDefintionIdentityLinkService.user = $rootScope.username;
            anewProcessDefintionIdentityLinkService.$save(
                {processDefinitionId: $rootScope.companyInfo.processDefinitionId + '/identitylinks'},
                function (t, putResponseHeaders) {
                    $log.info("saveProcessDefintionIdentityLinkService() success, response:", t);
                }, function (error) {
                    // failure handler
                    $log.error("saveProcessDefintionIdentityLinkService() failed:", JSON.stringify(error));
                });
        }
        //@see: http://www.activiti.org/userguide/#N12EE4
        $scope.startProcessInstance = function (expenseId) {
            //$log.debug("startProcessInstance() called!");
            if (angular.isUndefined($rootScope.companyInfo.processDefinitionKey) || angular.isUndefined($rootScope.companyInfo.businessKey)) {
                return $log.error("Undefined processDefinitionKey/businessKey", $rootScope.companyInfo.processDefinitionKey, $rootScope.companyInfo.businessKey);
            }
            //Then submitStartForm to start process
            var anewProcessInstance = new ProcessInstancesService();
            anewProcessInstance.processDefinitionKey = $rootScope.companyInfo.processDefinitionKey;
            anewProcessInstance.businessKey = $rootScope.companyInfo.businessKey;
            //Assemble variables
            anewProcessInstance.variables = [
                {'name': 'employeeName', 'value': $rootScope.username}
                , {'name': 'taskName', 'value': $scope.processInstanceVariables.name}
                , {'name': 'dueDate', 'value': $scope.processInstanceVariables.date}
                //, {'name': 'reportManagerId', 'value': $rootScope.managerIDsSel[0]}
                , {'name': 'participantIds', 'value': $rootScope.employeeIDsSel.toString()}
                , {'name': 'amountOfMoney', 'value': $rootScope.itemIDsSelAmount}
                , {'name': 'reimbursementMotivation', 'value': $scope.processInstanceVariables.name}
                , {'name': 'assignee', 'value': $rootScope.managerIDsSel[0]}
                , {'name': 'candidateUsers', 'value': $rootScope.employeeIDsSel.toString()}
                , {'name': 'candidateGroups', 'value': Enum.groupNames[0]}
            ];
            $log.debug("anewProcessInstance.variables:", anewProcessInstance.variables);
            //Save
            anewProcessInstance.$save(function (resp, putResponseHeaders) {
                $log.info("startProcessInstance() success, response:", resp);
                //Update expense Activiti Process Instance id.
                var updateExpense = new ExpenseService(expenseId);
                updateExpense.pid = resp.id;
                updateExpense.owner = $rootScope.username;
                updateExpense.status = Enum.expenseStatus.Submitted;
                //Save
                updateExpense.$patch({expenseId:expenseId},function (t, putResponseHeaders) {
                    $log.info("updateExpenseItem() success, response:", t);
                    //View history back to Expense tab inside of task table.
                    $ionicNavBarDelegate.back();
                }, function (error) {
                    // failure handler
                    $log.error("updateExpenseItem() failed:", JSON.stringify(error));
                });
                //View history back to Expense tab inside of task table.
                $ionicNavBarDelegate.back();
            }, function (error) {
                // failure handler
                $log.error("startProcessInstance.$save() failed:", JSON.stringify(error));
            });
        }
        //
        $scope.loadTasks = function () {
            //TaskService.get({}, function (response) {
            TaskService.get({assignee: $rootScope.username}, function (response) {
                $log.debug("TaskService.get() success!", response);
                $rootScope.tasks = response.data;
            });
        }
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
            //Claim task to report manager
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
            action.action = Enum.taskActions.Claim;
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
        //
        $scope.data = {};
        $scope.data.motivation = null;
        $scope.approveTask = function(decision,taskId) {
            //$ionicActionSheet.show({
            //    buttons: [
            //        { text: '<b>批准</b>' }
            //    ],
            //    destructiveText: '拒绝',
            //    titleText: '报销审批',
            //    cancelText: '取消',
            //    cancel: function() {
            //        // add cancel code..
            //    },
            //    buttonClicked: function(index) {
            //        //$log.debug("$ionicActionSheet clicked button index:",index);
            //        //$scope.completeTask(taskId);
            //        return true;
            //    },
            //    destructiveButtonClicked: function() {
            //        //$scope.resolveTask(taskId);
            //    }
            //});

            if(decision)//approve
            {
                $ionicPopup.show({
                    template: '<textarea placeholder="motivation" ng-model="data.motivation">',
                    title: 'Audit with motivation',
                    subTitle: 'Please input some things',
                    scope: $scope,
                    buttons: [
                        { text: 'Cancel' },
                        {
                            text: '<b>Approve</b>',
                            type: 'button-positive',
                            onTap: function(e) {
                                if (!$scope.data.motivation) {
                                    //don't allow the user to close unless he enters wifi password
                                    e.preventDefault();
                                } else {
                                    //Next func call.
                                    $scope.completeTask(decision,taskId,$scope.data.motivation);
                                    return $scope.data.motivation;
                                }
                            }
                        },
                    ]
                });
            }else{//reject
                $ionicPopup.show({
                    template: '<textarea placeholder="motivation" ng-model="data.motivation">',
                    title: 'Audit with motivation',
                    subTitle: 'Please input some things:',
                    scope: $scope,
                    buttons: [
                        { text: 'Cancel' },
                        {
                            text: '<b>Reject</b>',
                            type: 'button-positive',
                            onTap: function(e) {
                                if (!$scope.data.motivation) {
                                    //don't allow the user to close unless he enters wifi password
                                    e.preventDefault();
                                } else {
                                    //Next func call.
                                    $scope.completeTask(decision,taskId,$scope.data.motivation);
                                    return $scope.data.motivation;
                                }
                            }
                        },
                    ]
                });
            }
        }
        //CompleteTask
        $scope.completeTask = function (decision,taskId,motivation) {
            var action = new TaskService();
            action.action = Enum.taskActions.Complete;
            action.variables = [
                {'name': 'reimbursementApproved', 'value': decision}
                , {'name': 'managerMotivation', 'value': motivation}
            ];
            action.$save({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.complete() success!", resp);
                //refresh reports list view.
                TaskService.get({assignee: $rootScope.username}, function (response) {
                    $log.debug("TaskService.get() success!", response);
                    $rootScope.tasks = response.data;
                });
            });

        };
        //ResolveTask
        $scope.resolveTask = function (taskId) {
            var action = new TaskService();
            action.action = Enum.taskActions.Resolve;
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
        //DeleteTask
        $scope.deleteTask = function (taskId) {
            var action = new TaskService();
            action.$delete({"taskId": taskId}, function (resp) {
                //after finishing remove the task from the tasks list
                $log.debug("TaskService.delete() success!", resp);
                //refresh reports list view.
                TaskService.get({}, function (response) {
                    //TaskService.get({assignee: $rootScope.username}, function (response) {
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
    .controller('VendorsCtrl', function ($scope, $rootScope, $stateParams, $log) {
        //
        $rootScope.vendors = [];
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
    .controller('InvoiceCtrl', function ($scope, $rootScope, $location, $log, $http, CONFIG_ENV, FileUploader, Enum, InvoiceService) {
        $scope.fromComputer = true;
        $scope.imageURI=null;//For update the display image view.
        // init variables
        $scope.data = {};
        $scope.obj;
        var pictureSource;   // picture source
        var destinationType; // sets the format of returned value
        var url;
        // get upload URL for FORM
        $scope.data.uploadurl = CONFIG_ENV.api_endpoint + "upload";
        $scope.data.uploadFolderURI = CONFIG_ENV.api_endpoint + CONFIG_ENV.UPLOAD_FOLDER;

        // on DeviceReady check if already logged in (in our case CODE saved)
        ionic.Platform.ready(function () {
            //console.log("ready get camera types");
            if (navigator.camera) {
                // website handling
                $scope.fromComputer = false;
                //pictureSource=navigator.camera.PictureSourceType.PHOTOLIBRARY;
                pictureSource = navigator.camera.PictureSourceType.CAMERA;
                destinationType = navigator.camera.DestinationType.FILE_URI;
            }
        });
        // take picture
        $scope.takePicture = function () {
            if ($scope.fromComputer) {
                $scope.takePictureFromComputer();
            } else {
                $scope.takePictureFromDevice();
            }
        }
        // take picture from mobile device
        $scope.takePictureFromDevice = function () {
            //console.log("got camera button click");
            var options = {
                quality: 50,
                destinationType: destinationType,
                sourceType: pictureSource,
                encodingType: 0
            };
            if (!navigator.camera) {
                // error handling
                return;
            }
            navigator.camera.getPicture(
                function (imageURI) {
                    //console.log("got camera success ", imageURI);
                    $scope.mypicture = imageURI;
                },
                function (err) {
                    //console.log("got camera error ", err);
                    // error handling camera plugin
                },
                options);
        };

        //TODO:@see: http://blog.nraboy.com/2014/09/use-android-ios-camera-ionic-framework/
        $scope.takeCamPicture = function () {
            var options = {
                quality: 75,
                destinationType: Camera.DestinationType.DATA_URL,
                sourceType: Camera.PictureSourceType.CAMERA,
                allowEdit: true,
                encodingType: Camera.EncodingType.JPEG,
                targetWidth: 300,
                targetHeight: 300,
                popoverOptions: CameraPopoverOptions,
                saveToPhotoAlbum: false
            };

            $cordovaCamera.getPicture(options).then(function (imageData) {
                $scope.imgURI = "data:image/jpeg;base64," + imageData;
            }, function (err) {
                // An error occured. Show a message to the user
            });
        }
        //@see: http://codepen.io/ajoslin/pen/qwpCB?editors=101
        $scope.fileName = 'nothing';
        $scope.imageFile;
        //@see: http://stackoverflow.com/questions/17922557/angularjs-how-to-check-for-changes-in-file-input-fields
        $scope.onFileChangeHandler = function () {
            $scope.imageFile = event.target.files[0];
            $log.debug("openFileDialog->file:", $scope.imageFile);
            $scope.fileName = $scope.imageFile.name;
            //$scope.$apply();
        }
        //
        $scope.takePictureFromComputer = function () {
            //console.log('fire! $scope.takePictureFromComputer()');
            ionic.trigger('click', {target: document.getElementById('id_file_invoice')});
        };
        //
        var uploader = $scope.uploader = new FileUploader({
            url: $scope.data.uploadurl+"?owner="+$rootScope.username+"&name="+Enum.getTimestamp()
        });

        // FILTERS
        uploader.filters.push({
            name: 'customFilter',
            fn: function(item /*{File|FileLikeObject}*/, options) {
                return this.queue.length < 10;
            }
        });

        // CALLBACKS

        uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
            console.info('onWhenAddingFileFailed', item, filter, options);
        };
        uploader.onAfterAddingFile = function(fileItem) {
            //console.info('onAfterAddingFile', fileItem);
            //$log.debug(uploader,uploader.queue);
            uploader.queue[0].upload();
        };
        uploader.onAfterAddingAll = function(addedFileItems) {
            //console.info('onAfterAddingAll', addedFileItems);
        };
        uploader.onBeforeUploadItem = function(item) {
            //console.info('onBeforeUploadItem', item);
        };
        uploader.onProgressItem = function(fileItem, progress) {
            //console.info('onProgressItem', fileItem, progress);
        };
        uploader.onProgressAll = function(progress) {
            //console.info('onProgressAll', progress);
        };
        uploader.onSuccessItem = function(fileItem, response, status, headers) {
            //console.info('onSuccessItem', fileItem, response, status, headers);
        };
        uploader.onErrorItem = function(fileItem, response, status, headers) {
            //console.info('onErrorItem', fileItem, response, status, headers);
        };
        uploader.onCancelItem = function(fileItem, response, status, headers) {
            //console.info('onCancelItem', fileItem, response, status, headers);
        };
        uploader.onCompleteItem = function(fileItem, response, status, headers) {
            //console.info('onCompleteItem', fileItem, response, status, headers);
            $log.debug("Uploader->onCompleteItem.response:",response.picture.ico);
            //Update Invoice Image view
            $scope.imgURI = $scope.data.uploadFolderURI+response.picture.ico;
            $log.debug("$scope.imgURI:",$scope.imgURI,"id:",response.id);
            $rootScope.newItem.invoices = response.id;
        };
        uploader.onCompleteAll = function() {
            //console.info('onCompleteAll');
        };

        //console.info('uploader', uploader);
    })
;
