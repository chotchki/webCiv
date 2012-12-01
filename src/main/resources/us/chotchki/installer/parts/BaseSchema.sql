--We'll drop this statement once we have stablized further 
DROP SCHEMA webCiv CASCADE;

CREATE SCHEMA webCiv;

CREATE TABLE webciv.game (
	id bigserial NOT NULL,
	CONSTRAINT game_pk PRIMARY KEY (id)
);

CREATE TABLE webciv.player (
	id bigserial NOT NULL,
	nickname text NOT NULL,
	CONSTRAINT player_pk PRIMARY KEY (id),
	CONSTRAINT nickname_ck UNIQUE (nickname)
);

CREATE TABLE webciv.gameplayer (
  gameid bigint NOT NULL,
  playerid bigint NOT NULL,
  CONSTRAINT gp_pk PRIMARY KEY (gameid, playerid),
  CONSTRAINT gp_game_fk FOREIGN KEY (gameid)
  	REFERENCES webciv.game (id) MATCH SIMPLE
  	ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT gp_player_fk FOREIGN KEY (playerid)
  	REFERENCES webciv.player (id) MATCH SIMPLE
  	ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE webciv.worldsize (
	name text NOT NULL,
	height integer NOT NULL,
	width integer NOT NULL,
	CONSTRAINT worldsize_pk PRIMARY KEY (name),
	CONSTRAINT worldsize_ck CHECK (height > 0 AND width > 0)
);

--Create World Sizes
INSERT INTO webciv.worldsize(name, height, width) VALUES ('xx-small', 17, 17);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('x-small', 33, 33);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('small', 65, 65);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('medium', 129, 129);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('large', 257, 257);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('huge', 513, 513);
INSERT INTO webciv.worldsize(name, height, width) VALUES ('ludacris', 1025, 1025);

CREATE TABLE webciv.worldtype (
	name text NOT NULL,
	land real NOT NULL,
	wetness real NOT NULL,
	CONSTRAINT worldtype_pk PRIMARY KEY (name)
);

--Create World Types (wetter to drier)
INSERT INTO webciv.worldtype(name, land, wetness) VALUES ('Islands', .4, .5);

CREATE OR REPLACE FUNCTION webciv.world_generate_fractal(worldid bigint)
  RETURNS void AS
$BODY$
DECLARE
	wsize webciv.worldsize%rowtype;
BEGIN
	select worldsize.* into wsize 
	from webciv.world, webciv.worldsize 
	where world.size = worldsize.name
	and world.id = worldid;
	
	--Now that we have the size and type, lets start the cycles
	DECLARE
		rand_surface real[][][];
		corner real := random();
		s_range real := 0.5;
		side_length int;
		half_side int;
		lat int;
		llong int;
		avg_land real;
		avg_wet real;

		dline text;
	BEGIN
		rand_surface := array_fill(0.5, ARRAY[wsize.height, wsize.width,2]);
		
		-- We will use the diamond/square approach from here 
		-- http://stackoverflow.com/a/2773032/160208
		-- http://www.gameprogrammer.com/fractal.html
	
		-- Generate the corner values
		rand_surface[1][1][1] := corner;
		rand_surface[1][1][2] := corner;
		rand_surface[1][wsize.width][1] := corner;
		rand_surface[1][wsize.width][2] := corner;
		rand_surface[wsize.height][1][1] := corner;
		rand_surface[wsize.height][1][2] := corner;
		rand_surface[wsize.height][wsize.width][1] := corner;
		rand_surface[wsize.height][wsize.width][2] := corner;

		-- Iterate through the diamond pattern, note the poor man's for loop
		side_length := wsize.height - 1;
		loop
			if side_length < 2 then
				exit;	
			end if;
			half_side := side_length / 2;

			--Generate Square Values
			--raise warning 'square';
			lat := 0;
			loop
				if lat >= wsize.height - 1 then
					exit;
				end if;

				llong := 0;
				loop
					if llong >= wsize.width - 1 then
						exit;	
					end if;

					avg_land := rand_surface[lat + 1][llong + 1][1] + 
							rand_surface[lat + side_length + 1][llong + 1][1] +
							rand_surface[lat + 1][llong + side_length + 1][1] +
							rand_surface[lat + side_length + 1][llong + side_length + 1][1];
					avg_land := avg_land / 4;
					
					avg_wet := rand_surface[lat + 1][llong + 1][2] + 
							rand_surface[lat + side_length + 1][llong + 1][2] +
							rand_surface[lat + 1][llong + side_length + 1][2] +
							rand_surface[lat + side_length + 1][llong + side_length + 1][2];
					avg_wet := avg_wet / 4;

					--raise notice '% % % % %', lat, half_side, llong, avg_land, avg_wet;
					rand_surface[lat + half_side + 1][llong + half_side + 1][1] := avg_land + (random() * 2 * s_range) - s_range;
					rand_surface[lat + half_side + 1][llong + half_side + 1][2] := avg_wet + (random() * 2 * s_range) - s_range;

					llong := llong + side_length;
				end loop;

				lat := lat + side_length;
			end loop;

			--Generate Diamond Values
			--raise warning 'diamond';
			lat := 0;
			loop
				if lat >= wsize.height then
					exit;	
				end if;

				llong := (lat + half_side) % side_length;
				loop
					if llong >= wsize.width then
						exit;
					end if;

					avg_land := rand_surface[(lat - half_side + wsize.height - 1) % wsize.height + 1][llong + 1][1] + 
							rand_surface[(lat + half_side) % (wsize.height - 1) + 1][llong + 1][1] +
							rand_surface[lat + 1][(llong + half_side) % (wsize.width - 1) + 1][1] +
							rand_surface[lat + 1][(llong - half_side + wsize.width - 1) % (wsize.width - 1) + 1][1];
					avg_land := avg_land / 4;

					avg_wet := rand_surface[(lat - half_side + wsize.height - 1) % wsize.height + 1][llong + 1][2] + 
							rand_surface[(lat + half_side) % (wsize.height - 1) + 1][llong + 1][2] +
							rand_surface[lat + 1][(llong + half_side) % (wsize.width - 1) + 1][2] +
							rand_surface[lat + 1][(llong - half_side + wsize.width - 1) % (wsize.width - 1) + 1][2];
					avg_wet := avg_wet / 4;

					rand_surface[lat + 1][llong + 1][1] := avg_land + (random() * 2 * s_range) - s_range;
					rand_surface[lat + 1][llong + 1][2] := avg_wet + (random() * 2 * s_range) - s_range;

					if lat = 0 then
						rand_surface[wsize.height][llong + 1][1] := avg_land + (random() * 2 * s_range) - s_range;
						rand_surface[wsize.height][llong + 1][2] := avg_wet + (random() * 2 * s_range) - s_range;
					end if;

					if llong = 0 then
						rand_surface[lat + 1][wsize.width][1] := avg_land + (random() * 2 * s_range) - s_range;
						rand_surface[lat + 1][wsize.width][2] := avg_wet + (random() * 2 * s_range) - s_range;
					end if;

					llong := llong + side_length;
				end loop;

				lat := lat + half_side;
			end loop;
			side_length := side_length / 2;
			s_range := s_range / 2;
		end loop;

		-- Take the random values and convert to tiles
		for lat in 1..wsize.height loop
			for llong in 1..wsize.width loop
				insert into webciv.surface(worldid, longitude, latitude, type) 
				values (worldid,(llong - 1), (lat - 1), (
					select name
					from webciv.surfacetype
					order by abs(rand_surface[lat][llong][1] - height), abs(rand_surface[lat][llong][2] - wetness)
					limit 1
					)
				);
			end loop;
		end loop;
	END;
END;
$BODY$ LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION webciv.world_generate_surface() RETURNS trigger AS $$
BEGIN
	perform webciv.world_generate_fractal(new.id);
	return new;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE webciv.world (
	id bigserial NOT NULL,
	size text NOT NULL,
	type text NOT NULL,
	CONSTRAINT world_pk PRIMARY KEY (id),
	CONSTRAINT w_worldsize_fk FOREIGN KEY (size)
		REFERENCES webciv.worldsize (name) MATCH SIMPLE
		ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT w_worldtype_fk FOREIGN KEY (type)
		REFERENCES webciv.worldtype (name) MATCH SIMPLE
		ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER world_surface_gen AFTER INSERT ON webciv.world FOR EACH ROW EXECUTE PROCEDURE webciv.world_generate_surface();

--Create Tile Types
CREATE TABLE webciv.surfacetype (
  name text NOT NULL,
  height real not null,
  wetness real not null,
  land boolean not null,
  CONSTRAINT surfacetype_pk PRIMARY KEY (name)
);

INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Mountain', 1, 1, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Hills', 0.9, 1, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Rainforest', 0.8, 1, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Swamp', 0.8, 0.9, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Forest', 0.8, 0.7, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Grassland', 0.8, 0.5, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Plain', 0.8, 0.2, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Desert', 0.8, 0, true);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Ocean', 0.2, 1, false);
INSERT INTO webciv.surfacetype(name, height, wetness, land) VALUES ('Deep Ocean', 0, 1, false);


CREATE TABLE webciv.surface
(
  worldid bigint NOT NULL,
  longitude integer NOT NULL,
  latitude integer NOT NULL,
  type text NOT NULL,
  CONSTRAINT surface_pk PRIMARY KEY (worldid, longitude, latitude),
  CONSTRAINT s_surfacetype_fk FOREIGN KEY (type)
      REFERENCES webciv.surfacetype (name) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT s_world_fk FOREIGN KEY (worldid)
      REFERENCES webciv.world (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);