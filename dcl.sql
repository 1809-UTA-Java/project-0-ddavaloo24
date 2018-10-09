/* Drop database and user if it exists */
DROP USER newUser CASCADE;

/* Create database and user */
CREATE USER newUser
IDENTIFIED BY tempPassword
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

/* Grant permissions to user */
GRANT connect to zoo;
GRANT resource to zoo;
GRANT create session to zoo;
GRANT create table to zoo;
GRANT create view to zoo;