var home = angular.module('home', ['ngRoute', 'jlareau.pnotify']);

home.controller('HomeController', function ($scope, $http, notificationService) {
	$scope.dashboard.currentScenario = '';
	$http.get('ajax/scenarios')
		.success(
		function (data) {
			$scope.dashboard.scenarios = data;
		})
		.error(
		function (data) {
			notificationService.error('Error..');
		}
	);
});
