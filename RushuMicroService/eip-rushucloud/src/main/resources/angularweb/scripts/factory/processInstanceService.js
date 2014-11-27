angular.module('activitiApp').factory('ProcessInstanceService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'process-instance/:processInstance', {processInstance: "@processInstance"});
    return data;
});
