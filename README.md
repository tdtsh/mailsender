mailsender
==========

メールの一斉送信をします。Linuxベースの環境(Mac OS Xでも可)があれば動きます（別途SMTPサーバが必要です）。phpを使った管理機能と、javaとRDBMSでEメールを一斉に送信するコマンドラインのバッジプログラムがあります。




Mac OS X (Mountain Lion)でのインストール手順を書いておきます。





###########
一斉送信プログラム(java)のビルド
###########

ant all を実行 
(dist/MailSenderAssembler.jar が出来たら成功)




###########
phpを使った管理機能を動かす( Mac で Apache + php + Postgres)
###########



***
PostgreSQL
***

# port から インストール

port search postgresql
sudo port install postgresql91
sudo port install postgresql91-server

# DB初期化
sudo mkdir -p /usr/local/postgresql91/defaultdb

sudo chown postgres:postgres /usr/local/postgresql91/defaultdb

sudo su postgres -c '/opt/local/lib/postgresql91/bin/initdb -E UTF8 --no-locale -D /usr/local/postgresql91/defaultdb'

# psql, pg_ctlにシンボリックリンク貼る
# PATH上に古いのがあったらリネームするなど

sudo ln -s /opt/local/lib/postgresql91/bin/psql /opt/local/bin/
sudo ln -s /opt/local/lib/postgresql91/bin/pg_ctl /opt/local/bin/







***
Apacheの設定
***

cd /private/etc/apache2
sudo chmod 644 httpd.conf
sudo vi /private/etc/apache2/httpd.conf

-----------------------------------------------------------
#LoadModule php5_module libexec/apache2/libphp5.so
↓
LoadModule php5_module libexec/apache2/libphp5.so

#DocumentRoot "/Library/WebServer/Documents"
↓
DocumentRoot "/Users/yourname/path/to/mailsender/php"

#<Directory "/Library/WebServer/Documents">
↓
<Directory "/Users/yourname/path/to/mailsender/php">


<IfModule dir_module>
    #DirectoryIndex index.html
	↓
    DirectoryIndex index.html index.php
</IfModule>
-----------------------------------------------------------

***
phpの設定
***

# PHPがインストールされているか確認

php -v


sudo cp /etc/php.ini.default /etc/php.ini
sudo vi /etc/php.ini
-----------------------------------------------------------
date.timezone = Asia/Tokyo

display_errors = On

upload_tmp_dir = /Users/yourname/path/to/mailsender/php
-----------------------------------------------------------



***
使い方
***
# 管理画面開始
sudo su postgres -c 'pg_ctl -D /usr/local/postgresql91/defaultdb start'
sudo /usr/sbin/apachectl start


# DBスキーマ登録

psql -U postgres -h localhost

db/databaseScript_ml_postgresql.sql を参考にスキーマを登録

# 管理画面を見る
http://localhost/


# SMTPサーバ等の設定をする
vi dist/set.properties

# 送信実行
dist/run_mailmagazine.sh を実行


# 管理画面停止
sudo /usr/sbin/apachectl stop
sudo su postgres -c 'pg_ctl -D /usr/local/postgresql91/defaultdb stop'

