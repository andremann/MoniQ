CREATE TABLE LogStash (
	Id serial primary key,
	TimeMarker timestamp default now(),
	MonitoringScenario varchar(255) not null references Scenarios(name), 
	SensorType varchar(255) not null, 
	SensorId varchar(255) not null,
	Metric varchar(255) not null,
	Labels json,
	Log float not null
);