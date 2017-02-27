--CREATE TYPE status AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE Scenarios (
	Name         VARCHAR(255) PRIMARY KEY,
	Description  TEXT,
	Status       VARCHAR(25) NOT NULL DEFAULT 'active',
	LastModified TIMESTAMP
);

CREATE TABLE LogStash (
	Id                 SERIAL PRIMARY KEY,
	TimeMarker         TIMESTAMP DEFAULT now(),
	MonitoringScenario VARCHAR(255) NOT NULL REFERENCES Scenarios (name),
	SensorType         VARCHAR(255) NOT NULL,
	SensorId           VARCHAR(255) NOT NULL,
	Metric             VARCHAR(255) NOT NULL,
	Labels             JSON,
	Log                FLOAT        NOT NULL
);

CREATE TABLE Controls (
	Id                 SERIAL PRIMARY KEY,
	Name               VARCHAR(255) NOT NULL,
	MonitoringScenario VARCHAR(255) NOT NULL REFERENCES Scenarios (name),
	Selectors          JSON,
	AnalyzerClass      VARCHAR(255) NOT NULL,
	Status             VARCHAR(25)  NOT NULL DEFAULT 'active',
	LastModified       TIMESTAMP             DEFAULT now()
);

CREATE TABLE Configurations (
	Id           SERIAL PRIMARY KEY,
	Name         VARCHAR(255),
	Params       JSON,
	LastModified TIMESTAMP            DEFAULT now(),
	Status       VARCHAR(25) NOT NULL DEFAULT 'active'
);
