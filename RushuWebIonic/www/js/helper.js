var DynamicEnvironment = DynamicEnvironment || {};
//Helper functions here.
/**
 * You can have as many environments as you like in here
 * just make sure the host matches up to your hostname including port
 */
var _environment;
var _environments = {
    local: {
        host: 'localhost:63342',
        config: {
            /**
             * Add any config properties you want in here for this environment
             */
            api_endpoint: 'http://localhost:8082/eip-rushucloud/'
            ,clientId:'clientApp'
            ,clientSecret:'1NDgzZGY1OWViOWRmNjI5ZT'
        }
    },
    dev: {
        host: 'localhost:8082',
        config: {
            /**
             * Add any config properties you want in here for this environment
             */
            api_endpoint: 'http://localhost:8082/eip-rushucloud/'
            ,clientId:'clientApp'
            ,clientSecret:'1NDgzZGY1OWViOWRmNjI5ZT'
        }
    },
    test: {
        host: '123.57.78.65:8082',
        config: {
            /**
             * Add any config properties you want in here for this environment
             */
            api_endpoint: 'http://123.57.78.65:8082/eip-rushucloud/'
            ,clientId:'clientApp'
            ,clientSecret:'1NDgzZGY1OWViOWRmNjI5ZT'
        }
    },
    stage: {
        host: '123.56.112.163:8082',
        config: {
            /**
             * Add any config properties you want in here for this environment
             */
            api_endpoint: 'http://123.56.112.163:8082/eip-rushucloud/'
            ,clientId:'clientApp'
            ,clientSecret:'1NDgzZGY1OWViOWRmNjI5ZT'
        }
    },
    prod: {
        host: 'production.com',
        config: {
            /**
             * Add any config properties you want in here for this environment
             */
            api_endpoint: 'http://localhost:8082/eip-rushucloud/'
            ,clientId:'clientApp'
            ,clientSecret:'1NDgzZGY1OWViOWRmNjI5ZT'
        }
    }
};
_getEnvironment = function () {
    var host = window.location.host;

    if (_environment) {
        return _environment;
    }

    for (var environment in _environments) {
        if (typeof _environments[environment].host && _environments[environment].host == host) {
            _environment = environment;
            return _environment;
        }
    }

    return null;
};
DynamicEnvironment.get = function (property) {
    var result = _environments[_getEnvironment()].config[property];
    console.log("DynamicEnvironment.get():",result);
    return result;
};