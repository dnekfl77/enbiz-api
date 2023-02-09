-- public.ord definition

-- Drop table

-- DROP TABLE ord;

CREATE TABLE ord (
	order_no varchar NOT NULL,
	goods_name varchar NOT NULL,
	goods_price int8 NOT NULL DEFAULT 0,
	buyer_name varchar NOT NULL,
	buyer_phone varchar NULL,
	buyer_email varchar NULL,
	target_pg varchar NULL,
	create_dt timestamp NULL DEFAULT now(),
	update_dt timestamp NULL
);

-- Permissions

ALTER TABLE public.ord OWNER TO enbiz;
GRANT ALL ON TABLE public.ord TO enbiz;



-- public.pay definition

-- Drop table

-- DROP TABLE pay;

CREATE TABLE pay (
	id serial4 NOT NULL,
	order_no varchar NOT NULL,
	res_msg varchar NULL,
	res_cd varchar NULL,
	pg_txid varchar NULL,
	amount int8 NULL,
	trace_no varchar NULL,
	tno varchar NULL,
	pay_method varchar NULL,
	pay_state varchar NOT NULL DEFAULT 'COMPLETE'::character varying,
	create_dt timestamp NULL DEFAULT now(),
	update_dt timestamp NULL,
	CONSTRAINT pay_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE public.pay OWNER TO enbiz;
GRANT ALL ON TABLE public.pay TO enbiz;
