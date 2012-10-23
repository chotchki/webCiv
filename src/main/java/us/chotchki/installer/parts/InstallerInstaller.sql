CREATE TABLE webCivInstaller
(
   partName character varying NOT NULL,
   installed boolean NOT NULL DEFAULT false,
   statusDate timestamp with time zone NOT NULL DEFAULT now(),
   CONSTRAINT webCivInstaller_pk PRIMARY KEY (partName)
);

COMMENT ON COLUMN webCivInstaller.partName IS 'Name of the Installer Part';
COMMENT ON COLUMN webCivInstaller.installed IS 'Flag indicating whether this part has been installed.';
COMMENT ON TABLE webCivInstaller IS 'Table tracking the install process status.';