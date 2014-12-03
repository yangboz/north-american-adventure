//@examle:http://www.kdmooreconsulting.com/blogs/authentication-with-ionic-and-angular-js-in-a-cordovaphonegap-mobile-web-application/
//
angular.module('starter.services', [])

//TaskService where asignee==userId
.factory('ReportService', function($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'runtime/tasks/:taskId', {taskId: "@taskId"});
    return data;
})
//
.factory('StatsService', function() {

})
//Service REST API
//@see:http://www.activiti.org/userguide/#N1301E
///UserService
.factory('UserService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'identity/users/:user', {user: "@user"});
    return data;
})
///GroupService
.factory('GroupService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'identity/groups/:group', {group: "@group"});
    return data;
})
///GroupUserService
.factory('GroupUserService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.endpoint+'identity/groups/:group/members/:userId', {group: "@group",userId:"@userUrlId"});
    return data;
})
///TaskService
.factory('TaskService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'runtime/tasks/:taskId', {taskId: "@taskId"});
    return data;
})
///TasksService
.factory('TasksService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'runtime/tasks/:taskId', {taskId: "@taskId"},{
        update: {method: 'PUT', params: {taskId: "@taskId"}}
    });
    return data;
})
///TasksModelService
.factory('TasksModalService', function ($ionicModal, FormDataService, TasksService, $rootScope,UserService,ProcessInstanceService) {

    var ModalInstanceCtrl = function ($scope, $modalInstance, moment, taskDetailed) {
        $scope.taskDetailed = taskDetailed;


        function extractDataFromForm(objectOfReference) {
            var objectToSave = {
                "taskId": objectOfReference.id,
                properties: []
            }
            for (var key in objectOfReference.propertyForSaving) {
                var forObject = objectOfReference.propertyForSaving[key];

                if (!forObject.writable) {//if this is not writeable property do not use it
                    continue;
                }

                if (forObject.value != null) {
                    var elem = {
                        "id": forObject.id,
                        "value": forObject.value
                    };
                    if (typeof forObject.datePattern != 'undefined') {//format
                        var date = new Date(forObject.value);
                        elem.value = moment(date).format(forObject.datePattern.toUpperCase());
                    }
                    objectToSave.properties.push(elem);
                }
            }

            return objectToSave;
        }


        function extractDataFromFormForProcess(objectOfReference) {
            var objectToSave = {
                "processDefinitionId": objectOfReference.id,
                variables: []
            }
            for (var key in objectOfReference.propertyForSaving) {
                var forObject = objectOfReference.propertyForSaving[key];

                if (!forObject.writable) {//if this is not writeable property do not use it
                    continue;
                }

                if (forObject.value != null) {

                    console.log(forObject);
                    var elem = {
                        "name": forObject.id,
                        "value": forObject.value
                    };
                    if (typeof forObject.datePattern != 'undefined' && forObject.datePattern!=null) {//format
                        var date = new Date(forObject.value);

                        elem.value = moment(date).utc();
                    }
                    objectToSave.variables.push(elem);
                }
            }

            return objectToSave;
        }

        $scope.finish = function (detailedTask) {

            if (typeof detailedTask.propertyForSaving != "undefined") {
                var objectToSave = extractDataFromForm(detailedTask);

                var saveForm = new FormDataService(objectToSave);
                saveForm.$save(function () {
                    emitRefresh();
                    $modalInstance.dismiss('cancel');
                });
            } else {
                var action = new TasksService();
                action.action = "complete";
                action.$save({"taskId": detailedTask.id}, function () {
                    emitRefresh();
                    $modalInstance.dismiss('cancel');
                });
            }

        };

        $scope.startProcess = function (detailedTask) {

            if (typeof detailedTask.propertyForSaving != "undefined") {
                var objectToSave = extractDataFromFormForProcess(detailedTask);

                var saveForm = new ProcessInstanceService(objectToSave);
                saveForm.$save(function () {
                    emitRefresh();
                    $modalInstance.dismiss('cancel');
                });
            } else {
                var action = new TasksService();
                action.action = "complete";
                action.$save({"taskId": detailedTask.id}, function () {
                    emitRefresh();
                    $modalInstance.dismiss('cancel');
                });
            }

        };


        $scope.assignMe = function (detailedTask) {
            var taskToEdit = new TasksService({"assignee": $rootScope.username});
            taskToEdit.$update({"taskId": detailedTask.id}, function () {
                emitRefresh();
            });

        };

        $scope.takeOwnerShip = function (detailedTask) {
            var taskToEdit = new TasksService({"owner": $rootScope.username});
            taskToEdit.$update({"taskId": detailedTask.id}, function () {
                emitRefresh();
            });
        };

        $scope.openDatePicker = function (obj, $event) {
            $event.preventDefault();
            $event.stopPropagation();

            obj.opened = true;
        };

        $scope.setFormEnum = function (enumm, item,showText ) {
            item.value = enumm.id;
            if(typeof showText== 'undefined') {
                item.name = enumm.name;
            }else{
                item.name = showText;
            }
        };

        var emitRefresh = function () {
            $rootScope.$emit("refreshData", {});
        }

        $scope.cancel = function (taskDetailed) {
            $modalInstance.dismiss('cancel');
        };

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

            if(elem.type=="user")
            {
                elem.enumValues  =  UserService.get();
            }
        }

        task.form = data;
        task.propertyForSaving = propertyForSaving;

    }

    var loadTaskForm = function (task) {
        console.log(task);
        FormDataService.get({"taskId": task.id}, function (data) {
            extractForm(task, data);
        }, function (data) {

            if (data.data.statusCode == 404) {
                alert("there was an error");
            }

        });
    };

    var loadProcessForm = function (processDefinition) {
        FormDataService.get({"processDefinitionId": processDefinition.id}, function (data) {
            extractForm(processDefinition, data)
        }, function (data) {

            if (data.data.statusCode == 404) {
                alert("there was an error");
            }

        });
    };


    var factory = {
        loadTaskForm: function (task) {
            var modalInstance = $modal.open({
                templateUrl: 'views/modals/taskForm.html',
                controller: ModalInstanceCtrl,

                resolve: {
                    taskDetailed: function () {
                        return task;
                    }
                }
            });

            modalInstance.result.then(function (taskDetailed) {

            }, function () {

            });
            loadTaskForm(task);
        },
        loadProcessForm: function (processDefinition) {
            var modalInstance = $modal.open({
                templateUrl: 'views/modals/processDefinitionForm.html',
                controller: ModalInstanceCtrl,

                resolve: {
                    taskDetailed: function () {
                        return processDefinition;
                    }
                }
            });

            modalInstance.result.then(function (taskDetailed) {

            }, function () {

            });
            loadProcessForm(processDefinition);
        }
    };
    return factory;
})
///HistoryService
.factory('HistoryService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'history/historic-process-instances/:processInstanceId', {processInstanceId: "@processInstanceId"});
    return data;
})
///ProcessInstancesService
.factory('ProcessService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'process-instances/:processInstanceId', {processInstanceId: "@processInstanceId"});
    return data;
})
///ProcessDefinitionService
.factory('ProcessDefinitionService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'repository/process-definitions/:processDefinitionId', {processDefinitionId: "@processDefinitionId"},{
            update: {method: 'PUT', params: {processDefinitionId: "@processDefinitionId"}}
        }
    );
    return data;
})
///ProcessInstanceService
.factory('ProcessInstanceService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'process-instance/:processInstance', {processInstance: "@processInstance"});
    return data;
})
///ProcessInstancesService
//@see http://www.activiti.org/userguide/#N13DF1
.factory('ProcessInstancesService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'process-instances', {});
    return data;
})
///JobsService
.factory('JobService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'management/jobs/:jobId', {jobId: "@jobId"});
    return data;
})
///ExecutionsService
.factory('ExecutionService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'runtime/executions/:executionId', {executionId: "@executionId"});
    return data;
})
///FormDataService
.factory('FormDataService', function ($resource,CONFIG_ENV) {
//    var data = $resource(API_URL+'form/form-data?taskId=:taskId', {taskId: "@taskId"});
//    return data;
        var data = $resource(CONFIG_ENV.api_endpoint+'form/form-data', {}, {
            startTask: {method:'GET',  params: {processDefinitionId: "@processDefinitionId"}}
        });
        return data;
})
///GroupService
.factory('GroupService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'identity/groups/:group', {group: "@group"});
    return data;
})
///ItemService
.factory('ItemService', function ($resource,CONFIG_ENV) {
    var data = $resource(CONFIG_ENV.api_endpoint+'items/:itemId', {itemId: "@itemId"});
    return data;
})
///HTTP Header communication.
.factory('Base64', function () {
    var keyStr = 'ABCDEFGHIJKLMNOP' +
        'QRSTUVWXYZabcdef' +
        'ghijklmnopqrstuv' +
        'wxyz0123456789+/' +
        '=';
    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };
})
//WebSocket
.factory('WebsocketService', ['$rootScope', '$timeout', function($rootScope, $timeout) {

    var _ws;
    var _username = '';
    var messages = [];
    var users = [];

    function onMessage(e) {
        var data = JSON.parse(decodeURIComponent(e.data));
        $rootScope.$apply(function() {

            if (data.type === 'users') {
                users = data.message;
                $rootScope.$broadcast('websocket', 'users', users);
                return;
            }

            messages.splice(0, 0, { user: data.user, message: data.message, date: data.date });
            $rootScope.$broadcast('websocket', 'message', messages);
        });
    }

    return {

        login: function(url, username) {

            _username = username;

            _ws = new WebSocket(url);
            _ws.onmessage = onMessage;
            _ws.onopen = function () {
                _ws.send(encodeURIComponent(JSON.stringify({ type: 'change', message: _username })));
            };

        },

        logoff: function() {
            _ws.close();
            _ws = null;
            _username = '';
            users = [];
            $rootScope.$broadcast('websocket', 'users', users);
        },

        send: function(message) {
            _ws.send(encodeURIComponent(JSON.stringify({ type: 'message', message: message })));
            //
            _ws.onmessage = function(message) { console.log("_ws.onmessage:",message); }
        }
    };

}])
;