CREATE TABLE Scenarios (
	Name varchar(255) primary key,
	Description text,
	Status varchar(255) not null,
	LastModified timestamp
);

INSERT INTO Scenarios(name, description, status, lastModified) VALUES ('prepublic','The prepublic scenario monitoring the alignment between Solr and Redis', 'active', now());
INSERT INTO Scenarios(name, description, status, lastModified) VALUES ('native','The monitoring scenario tracks what happends during the ingestion of new content', 'inactive', now());
