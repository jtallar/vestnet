# WinWings

### Local Database configuration 
Run Postgre service (macOS)
```
pg_ctl -D /usr/local/var/postgres start
```
Create user root
```
createuser root
```
Create paw database and access to it
```
createdb paw -O root
```
