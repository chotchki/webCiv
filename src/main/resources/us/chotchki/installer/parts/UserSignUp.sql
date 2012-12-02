CREATE TABLE webciv.user
(
  username text NOT NULL,
  password text NOT NULL,
  enabled boolean NOT NULL default false,
  admin boolean NOT NULL default false,
  playerid bigint NOT NULL,
  CONSTRAINT user_pk PRIMARY KEY (username),
  CONSTRAINT user_fk FOREIGN KEY (playerid)
      REFERENCES webciv.player (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE webciv.player ADD COLUMN email text;
