/**
 * @file databaseScript_brat_postgresql.sql
 * @brief メルマガ配信システム用データベーススクリプト
 * @author tdtsh
 * @date 2006-08-08
 * @version $Id$
 */

/*
psql -U postgres -d mailmagazine -h localhost

*/

/********************************************************************
--データベースとユーザの作成
********************************************************************/
/* initdb はUTF-8 */

DROP DATABASE mailmagazine;
CREATE DATABASE mailmagazine;

drop user slode;
create user slode with password 'mmuser';

/********************************************************************
--テーブルの作成  
********************************************************************/

drop sequence seq_OID;
drop index ix_AddressList_1;
drop index ix_shippingid;
drop index ix_ReplaceInfo_1;
drop index ix_shippinginfo_1;
drop table AddressList;
drop table ReplaceInfo;
drop table shippinginfo;

-- 配信情報テーブル
create table shippinginfo (
 id varchar(20) not null primary key 
 , beginTime   timestamp null     -- 配信開始日時
 , body    text   not null   -- 本文
 , completionTime timestamp null     -- 配信終了日時
 , fromAddress  varchar(80) not null                -- 送信元メールアドレス
 , lastModified  timestamp null default now() -- 更新日時
 , requestedStatus smallint not null default 0 -- 要求ステータス
 , reserveTime  timestamp null default now() -- 登録日時
 , returnAddress  varchar(80) null     -- 返信先メールアドレス
 , shippingstatus  smallint not null default 0 -- 配信ステータス
 , shippingtime  timestamp null default now() -- 配信予定日時
 , subject   varchar(100) not null   -- タイトル
    , mailid            varchar(20) null
) with oids;
create index ix_shippinginfo_1 on shippinginfo( id );
grant all on shippinginfo to slode;

-- 配信先アドレスリスト
create table AddressList (
 id     varchar(20) not null primary key    -- 
 , account   varchar(80) not null    -- 送信先アカウント( @以前 )
 , attribute1  varchar(100) null     -- 属性1( $REPLACE1 に対応 )
 , attribute2  varchar(100) null     -- 属性1( $REPLACE2 に対応 )
 , attribute3  varchar(100) null     -- 属性1( $REPLACE3 に対応 )
 , attribute4  varchar(100) null     -- 属性1( $REPLACE4 に対応 )
 , attribute5  varchar(100) null     -- 属性1( $REPLACE5 に対応 )
 , attribute6  varchar(100) null     -- 属性1( $REPLACE6 に対応 )
 , attribute7  varchar(100) null     -- 属性1( $REPLACE7 に対応 )
 , attribute8  varchar(100) null     -- 属性1( $REPLACE8 に対応 )
 , attribute9  varchar(100) null     -- 属性1( $REPLACE9 に対応 )
 , attribute10  varchar(100) null     -- 属性1( $REPLACE10に対応 )
 , domain   varchar(100) not null    -- 送信先ドメイン( @以降 )
 , name    varchar(100) not null    -- ニックネーム
 , shippingId  varchar(20) not null    -- 配信ID
 , shippingTime  timestamp null     -- 送信した際更新(途中終了対応)
 , foreign key ( shippingId ) references shippinginfo( id )
) with oids;
create index ix_AddressList_1 on AddressList(id);
grant all on AddressList to slode;
create index ix_shippingid on AddressList(shippingid);

-- 個別配信情報テーブル
create table ReplaceInfo(
 id     varchar(20) not null primary key   -- 
 , attribute   varchar(60) null      -- AddressList.Attribute1～10に対応
 , key    varchar(50) null      -- キーワード( REPLACE1～10 )
 , shippingId  varchar(20) not null     -- 配信ID
 , subBody   text  null      -- 置換内容
    , advid             varchar(20) null
    , mailid            varchar(20) null
 , foreign key ( shippingId ) references shippinginfo( id )
);
create index ix_ReplaceInfo_1 on ReplaceInfo(id);
grant all on ReplaceInfo to slode;


-- OID用シーケンス
create sequence seq_OID start 1 increment 1 maxvalue 999999 cycle;
grant all on seq_OID to slode;



















-- ############# data genarate ############

delete from ReplaceInfo;
delete from AddressList;
delete from shippinginfo;

insert into shippinginfo( id,shippingtime,reservetime,requestedStatus, shippingstatus, fromAddress, subject, body ) values
('1','2006-09-10 00:00:00','2006-08-30 00:00:00',1,0,'from@hoge','[テスト]@@@@@メルマガ1号','1号BODY' );
insert into AddressList (id,shippingId,account,domain,name ,attribute1) values ('100','1' ,'ml_test1', 'hoge.ne.jp', 'ML_TEST','いいひと' );


