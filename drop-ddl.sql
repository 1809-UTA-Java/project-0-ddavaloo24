Drop TABLE TRANSACTIONS;

CREATE TABLE TRANSACTIONS (
	b_ID varchar2(50),
	t_time TIMESTAMP,
	t_type varchar2(50),
	amount number(*,2),
    FOREIGN KEY (b_ID) REFERENCES BANKACCOUNTS(b_ID)
);
