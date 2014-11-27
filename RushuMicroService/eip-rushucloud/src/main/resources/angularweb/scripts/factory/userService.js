angular.module('activitiApp').factory('UserService', function ($resource,Config_API) {
    var data = $resource(Config_API.endpoint+'identity/users/:user', {user: "@user"});
    return data;
});
