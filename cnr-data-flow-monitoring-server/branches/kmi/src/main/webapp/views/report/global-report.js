var report = angular.module('reportExplorer', ['ngRoute', 'jlareau.pnotify']);

report.filter('labels', function () {

	return function (map) {
		var result = "";
		for (var label in map) {
			result += label + "=" + map[label] + ","
		}
		return result.substring(0, result.length - 1);
	}
});

report.controller('GlobalReportController', function ($scope, $http, $location, $routeParams, notificationService) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.globalReport = {};
	
	$http.get('ajax/scenarios/'+ $routeParams.scenario +'/reports')
	.success(
		function(data) {
			$scope.globalReport.reports = data;
		})
	.error(
		function(data) {
			notificationService.error('Error..');
		}		
	);
	
	$scope.composeExposeUrl = function(selectors) {
		var url = '';
		var uniques = filterUniques(selectors);
		for (key in uniques) {
			url += uniques[key];
			if (key != uniques.length - 1) {
				url += '!';
			}
		}
		return url;
	};
	
	function filterUniques(selectors) {
		var uniques = [];
		for (key in selectors) {
			var widget = '';
			widget += selectors[key].metric;
			widget += ':';
			widget += selectors[key].labelName;
			if (uniques.indexOf(widget) != -1) {

			} else {
				uniques.push(widget);
			}
		}
		return uniques;
	}
});