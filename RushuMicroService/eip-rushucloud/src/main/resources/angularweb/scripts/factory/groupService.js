angular.module('activitiApp').factory('GroupService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'identity/groups/:group', {group: "@group"});
    return data;
});
