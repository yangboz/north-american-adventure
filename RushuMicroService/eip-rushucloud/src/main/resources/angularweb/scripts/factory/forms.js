angular.module('activitiApp').factory('FormDataService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'form/form-data', {}, {
        startTask: {method:'GET',  params: {processDefinitionId: "@processDefinitionId"}}
    });
    return data;
});