var module = angular.module('metricsExplorer', ['ngRoute', 'jlareau.pnotify']);

module.controller('MetricsExplorerController', function ($scope, $http, $routeParams, notificationService) {
    $scope.dashboard.currentScenario = $routeParams.scenario;

    $scope.metricsExplorer = {};
    $scope.metricsExplorer.currentMetric = '';
    $scope.metricsExplorer.metrics = [];
    $scope.metricsExplorer.report = [];

    if ($scope.metricsExplorer.metrics.length == 0) {
        $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics')
            .success(
                function (data) {
                    $scope.metricsExplorer.metrics = data;
                })
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
    }
    ;

    $scope.metricsExplorer.currentMetric = $routeParams.metric;
    if ($routeParams.metric != undefined) {
        $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + $routeParams.metric + '/labels')
            .success(
                function (data) {
                    $scope.metricsExplorer.labels = data;
                    $scope.metricsExplorer.selectLabel(data[0]);
                })
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
    }

    $scope.metricsExplorer.selectLabel = function (l) {
        if ($scope.metricsExplorer.currentLabel != l || $scope.metricsExplorer.currentLabel == undefined) {
            $scope.metricsExplorer.currentLabel = l;

            var labelParam = ($scope.metricsExplorer.currentLabel != undefined) ? "?label=" + $scope.metricsExplorer.currentLabel : "";
            $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + $routeParams.metric + "/observations" + labelParam)
                .success(
                    function (data) {
                        //$scope.metricsExplorer.observations = data.observations;
                        //$scope.metricsExplorer.labelValues = data.labelValues;
                        var c3types = {};
                        data.labelValues.forEach(
                            function (entry) {
                                c3types[entry] = 'step';
                            }
                        );
                        c3.generate({
                            bindto: '#lineGraph',
                            line: {
                                step: {
                                    type: 'step-after'
                                }
                            },
                            data: {
                                json: fillIn(data.observations, data.labelValues),
                                keys: {
                                    x: 'timestamp',
                                    value: data.labelValues
                                },
                                types: c3types
                            },
                            point: {
                                r: 4
                            },
                            axis: {
                                x: {
                                    type: 'timeseries',
                                    tick: {
                                        format: '%d-%m-%Y@%H:%M:%S',
                                        fit: true,
                                        rotate: -20,
                                        multiline: true

                                    },
                                    height: 60
                                }
                            },
                            tooltip: {
                                format: {
                                    title: function (x) {
                                        return x;
                                    }
                                }
                            },
                            zoom: {
                                enabled: true
                            }
                        })
                        ;
                    }
                )
                .error(
                    function (data) {
                        notificationService.error('Error..');
                    }
                );

            $http.get('ajax/scenarios/' + $routeParams.scenario + '/reports/' + $routeParams.metric)
                .success(
                    function (data) {
                        $scope.metricsExplorer.report = data;
                    }
                )
                .error(
                    function (data) {
                        notificationService.error('Error..');
                    }
                );
        }
    };

    $scope.metricsExplorer.createControl = function () {
        $scope.metricsExplorer.selectedControlIndex = null;
        $scope.metricsExplorer.tmpControl = {monitoringScenario: $scope.dashboard.currentScenario, status: 'inactive'};
        $('#controlModal').modal('show');
    };

    $scope.metricsExplorer.removeSelector = function (selectorIndex) {
        $scope.metricsExplorer.tmpControl.selectors.splice(selectorIndex, 1);
    };

    $scope.metricsExplorer.addSelector = function () {
        if (!$scope.metricsExplorer.tmpControl.selectors) {
            $scope.metricsExplorer.tmpControl.selectors = [];
        }
        $scope.metricsExplorer.tmpControl.selectors.push({});
    };

    $scope.metricsExplorer.setStatus = function (status) {
        $scope.metricsExplorer.tmpControl.status = status;
    };

    $scope.metricsExplorer.fetchMetrics = function () {
        if ($scope.metricsExplorer.metrics.length == 0) {
            $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics')
                .success(
                    function (data) {
                        $scope.metricsExplorer.metrics = data;
                    })
                .error(
                    function (data) {
                        notificationService.error('Error..');
                    }
                );
        }
    };

    $scope.metricsExplorer.fetchLabelNames = function (m) {
        $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + m + '/labels')
            .success(
                function (data) {
                    $scope.metricsExplorer.labelNames = data;
                })
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
    };

    $scope.metricsExplorer.fetchLabelValues = function (m, l) {
        $http.get('ajax/scenarios/' + $routeParams.scenario + '/metrics/' + m + '/labels/' + l)
            .success(
                function (data) {
                    $scope.metricsExplorer.labelValues = data;
                })
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
    };

    $scope.metricsExplorer.fetchAnalyzers = function () {
        $http.get('ajax/analyzers/')
            .success(
                function (data) {
                    $scope.metricsExplorer.analyzers = data;
                })
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
    };

    $scope.metricsExplorer.save = function () {
        $scope.metricsExplorer.tmpControl.lastModified = new Date().getTime();
        $http.post('ajax/scenarios/' + $routeParams.scenario + '/controls', $scope.metricsExplorer.tmpControl)
            .success(
                function (data) {
                    $http.get('ajax/scenarios/' + $routeParams.scenario + '/reports/' + $routeParams.metric)
                        .success(
                            function (data) {
                                $scope.metricsExplorer.report = data;
                            }
                        )
                        .error(
                            function (data) {
                                notificationService.error('Error..');
                            }
                        );
                }
            )
            .error(
                function (data) {
                    notificationService.error('Error..');
                }
            );
        $('#controlModal').modal('hide');
    };
});

function fillIn(observations, labelValues) {
    var lastObservations = {};
    for (var label of labelValues) {
        lastObservations[label] = 0;
    }

    for (var observation of observations) {
        for (label of labelValues) {
            if (observation[label] == undefined) {
                observation[label] = lastObservations[label];
            } else {
                lastObservations[label] = observation[label];
            }
        }
    }
    return observations;
}
