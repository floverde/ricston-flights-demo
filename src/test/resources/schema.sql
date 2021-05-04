-- Adds only the `aircraft_type` table since
-- it is not included in the persistence unit.
CREATE TABLE `aircraft_type` (
  `ICAO_Code` varchar(4) NOT NULL,
  `IATA_Code` char(3) DEFAULT NULL,
  `Model` varchar(100) NOT NULL
);

CREATE INDEX `idx_aircraft_type_Model` ON `aircraft_type`(`Model`);
CREATE INDEX `ICAO_IDX` ON `aircraft_type`(`ICAO_Code`);