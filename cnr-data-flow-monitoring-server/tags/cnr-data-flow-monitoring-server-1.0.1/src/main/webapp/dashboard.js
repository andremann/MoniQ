var dashboard = angular.module('dfmDashboard', ['ngRoute', 'home', 'scenarioFacts', 'reportExplorer', 'metricsExplorer', 'metricsGraphsExpose', 'controlsExplorer', 'configurationsExplorer']);

dashboard.config(function ($routeProvider) {
        $routeProvider
            .when('/home', {templateUrl: './views/home/home.html', controller: 'HomeController'})
            .when('/scenarios/:scenario', {
                templateUrl: './views/scenario/scenario-facts.html',
                controller: 'ScenarioFactsController'
            })
            .when('/scenarios/:scenario/metrics/', {
                templateUrl: './views/metrics/metrics-explorer.html',
                controller: 'MetricsExplorerController'
            })
            .when('/scenarios/:scenario/metrics/:metric', {
                templateUrl: './views/metrics/metrics-explorer.html',
                controller: 'MetricsExplorerController'
            })
            .when('/scenarios/:scenario/expose', {
                templateUrl: './views/expose/metrics-graphs-expose.html',
                controller: 'MetricsExposeController'
            })
            .when('/scenarios/:scenario/expose/:widgetConf', {
                templateUrl: './views/expose/metrics-graphs-expose.html',
                controller: 'MetricsExposeController'
            })
            .when('/scenarios/:scenario/controls', {
                templateUrl: './views/controls/controls-explorer.html',
                controller: 'ControlsExplorerController'
            })
            .when('/scenarios/:scenario/configurations', {
                templateUrl: './views/configurations/configurations-explorer.html',
                controller: 'ConfigurationsExplorerController'
            })
            .when('/scenarios/:scenario/report', {
                templateUrl: './views/report/global-report.html',
                controller: 'GlobalReportController'
            })
            .otherwise({redirectTo: '/home'});
    }
);

dashboard.controller('NavController', function ($scope, $http, $window) {
    $scope.dashboard = {};
    $scope.user = {};
    $scope.dashboard.currentScenario = '';

    $http.get('ajax/users')
        .success(
            function (data) {
                if (data.name) {
                    $scope.user.name = data.name;
                    $scope.user.roles = data.roles;
                    $scope.authenticated = true;
                } else {
                    $scope.authenticated = false;
                }
            })
        .error(
            function () {
                $rootScope.authenticated = false;
            });

    $http.get('ajax/servers')
        .success(
            function (data) {
                $scope.dashboard.influxHost = data['influxdb'];
            }
        );

    $scope.isAdmin = function () {
        if ($scope.user.roles != undefined)
            return ($scope.user.roles.indexOf('ROLE_ADMIN') != -1) ? true : false;
    };

    $scope.postLogout = function () {
        $http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8";
        $http.post('logout', $.param({'username': $scope.username, 'password': $scope.password}))
            .success(function (data) {
                $window.location.href = data;
            })
    }
});
