angular.module('activitiApp').factory('GroupUserService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'identity/groups/:group/members/:userId', {group: "@group",userId:"@userUrlId"});
    return data;
});
