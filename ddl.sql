/* Create tables */
CREATE TABLE CUSTOMERS (
	c_ID varchar2(50) primary key,
	c_FName varchar2(50),
	c_LName varchar2(50),
	c_Username varchar2(20),
	c_Password varchar2(20)
);


CREATE TABLE EMPLOYEES (
	e_ID varchar2(50) primary key,
	e_FName varchar2(50),
	e_LName varchar2(50),
	e_Username varchar2(20),
	e_Password varchar2(20)
);

CREATE TABLE ADMINS (
	a_ID varchar2(50) primary key,
	a_FName varchar2(50),
	a_LName varchar2(50),
	a_Username varchar2(20),
	a_Password varchar2(20)
);

CREATE TABLE BANKACCOUNTS (
	b_ID varchar2(50) primary key,
	balance number(*,2),
	approved char(1) 
);

CREATE TABLE CUSTOMERBANKCONN (
	c_ID varchar2(50),
	b_ID varchar2(50),
	FOREIGN KEY (c_ID) REFERENCES CUSTOMERS(c_ID),
	FOREIGN KEY (b_ID) REFERENCES BANKACCOUNTS(b_ID)
);

CREATE TABLE TRANSACTIONS (
	bank_ID varchar2(50),
	transferID varchar(50) DEFAULT null,
	type varchar2(50),
	amount number(*,2) check (amount > 0)
);
