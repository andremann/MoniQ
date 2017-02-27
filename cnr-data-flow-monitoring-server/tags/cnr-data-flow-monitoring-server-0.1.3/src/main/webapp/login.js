var module = angular.module('dfmLogin', ['jlareau.pnotify']);

module.controller('LoginController', function ($scope, $http, $window, notificationService) {

    $scope.postLogin = function () {
        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8";
        $http.post('login', $.param({'username': $scope.username, 'password': $scope.password}))
            .success(function (data) {
                $window.location.href = data;
            })
            .error(function (data) {
                notificationService.error('Wrong username or password!');
            });
    }

});
