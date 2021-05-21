var exec = require('cordova/exec');

exports.liveness = function (success, error) {
    exec(success, error, 'FaceLiveness', 'liveness');
};
