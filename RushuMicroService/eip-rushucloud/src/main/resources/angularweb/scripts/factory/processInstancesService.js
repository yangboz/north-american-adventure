angular.module('activitiApp').factory('ProcessInstancesService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'runtime/process-instances/:processInstance', {processInstance: "@processInstance"});
    return data;
});
