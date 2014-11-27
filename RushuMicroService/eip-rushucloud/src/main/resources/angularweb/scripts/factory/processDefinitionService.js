angular.module('activitiApp').factory('ProcessDefinitionService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'repository/process-definitions/:processDefinitionId', {processDefinitionId: "@processDefinitionId"},{
            update: {method: 'PUT', params: {processDefinitionId: "@processDefinitionId"}}
        }
    );
    return data;
});
