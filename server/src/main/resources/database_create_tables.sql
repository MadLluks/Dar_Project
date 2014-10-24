

CREATE TABLE user
(
	login varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	PRIMARY KEY (login)
);

CREATE TABLE location
(
	loc_id INTEGER PRIMARY KEY,
	lat float NOT NULL,
	lon float NOT NULL,
	address varchar(255),
	postal_code varchar(255),
	city varchar(255),
	CONSTRAINT u_location UNIQUE (lat,lon)
);

CREATE TABLE user_location
(
	login varchar(255) NOT NULL,
	loc_id int NOT NULL,
	FOREIGN KEY (login) REFERENCES user(login),
	FOREIGN KEY (loc_id) REFERENCES location(loc_id)
);

CREATE TABLE cinema
(
	cine_id INTEGER PRIMARY KEY,
	loc_id int NOT NULL,
	cine_name varchar(255) NOT NULL,
	FOREIGN KEY (loc_id) REFERENCES location(loc_id)
	-- CONSTRAINT u_cinema UNIQUE(loc_id,cine_name
);

CREATE TABLE movie
(
	movie_id varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	PRIMARY KEY (movie_id)
);

CREATE TABLE seen_movie
(
	movie_id varchar(255)  NOT NULL,
	cine_id int NOT NULL,
	login varchar(255) NOT NULL,
	FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
	FOREIGN KEY (cine_id) REFERENCES cinema(cine_id),
	FOREIGN KEY (login) REFERENCES user(login),
	CONSTRAINT u_movie_seen UNIQUE (movie_id, cine_id, login)
);


INSERT INTO user(login,password) VALUES('test','test');
INSERT INTO user(login,password) VALUES('toto','titi');
INSERT INTO movie VALUES('P222', 'Il était une fois');
