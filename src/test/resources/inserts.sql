-- Populate the `airlines` table
INSERT INTO `airlines` (`Airline_ID`, `Name`, `Alias`, `IATA`, ICAO, `Callsign`, `Country`, `Active`) VALUES
        (4296, 'Ryanair', '\N', 'FR', 'RYR', 'RYANAIR', 'Ireland', 'Y');

-- Populate the `airports` table
INSERT INTO `airports` (`Airport_ID`, `Name`, `City`, `Country`, `IATA`, `ICAO`, `Latitude`, `Longitude`, `Altitude`, `Timezone`, `DST`, `Tz_Timezone`, `Type`, `Source`) VALUES
        (1638, 'Lisbon Portela Airport', 'Lisbon', 'Portugal', 'LIS', 'LPPT', 39, -9, 374, 0, 'E', 'Europe/Lisbon', 'airport', 'OurAirports');
INSERT INTO `airports` (`Airport_ID`, `Name`, `City`, `Country`, `IATA`, `ICAO`, `Latitude`, `Longitude`, `Altitude`, `Timezone`, `DST`, `Tz_Timezone`, `Type`, `Source`) VALUES
        (302, 'Brussels Airport', 'Brussels', 'Belgium', 'BRU', 'EBBR', 51, 4, 184, 1, 'E', 'Europe/Brussels', 'airport', 'OurAirports');

-- Populate the `routes` table
INSERT INTO `routes` (`Airline`, `Airline_ID`, `Source_Airport`, `Source_Airport_ID`, `Destination_Airport`, `Destination_Airport_ID`, `Codeshare`, `Stops`, `Equipment`) VALUES
        ('FR', 4296, 'LIS', 1638, 'BRU', 302, '\0', false, '738');

-- Populate the `aircraft_type` table
INSERT INTO `aircraft_type` (`ICAO_Code`, `IATA_Code`, `Model`) 
	VALUES ('A30B', 'AB3', 'Airbus A300');

-- Populate the `flights_Fabrizio` table
INSERT INTO `flights_Fabrizio` (`Flight_Code`, `Airline_Name`, `Departure_Airport`, `Destination_Airport`, `Departure_Date`, `Aircraft_Type`, `Seat_Availability`, `Price`)
	VALUES ('FR0001', 'Ryanair', 'LIS', 'BRU', '2017-04-12 10:35:00.0', 'Airbus A300', 100, 52.25);
-- INSERT INTO `flights_Fabrizio` (`Flight_Code`, `Airline_Name`, `Departure_Airport`, `Destination_Airport`, `Departure_Date`, `Aircraft_Type`, `Seat_Availability`, `Price`)
-- 	VALUES ('FR0001', 'Ryanair', 'LIS', 'BRU', '2017-04-12 09:35:00.0', 'Airbus A300', 95, 54.25);


