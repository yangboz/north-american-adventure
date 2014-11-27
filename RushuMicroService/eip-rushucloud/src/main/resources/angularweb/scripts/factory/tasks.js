angular.module('activitiApp').factory('TasksService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'runtime/tasks/:taskId', {taskId: "@taskId"},{
        update: {method: 'PUT', params: {taskId: "@taskId"}}
    });
    return data;
});