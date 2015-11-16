# cosi228a Course selection project

## Setup databse
### create a new pg database and user with access
- `psql`
- `CREATE USER cosi228_development;`
- `ALTER ROLE cosi228_development with SUPERUSER` (not necessary)
- `CREATE DATABASE cosi228_development OWNER cosi228_development;`
 
### make new database accessible to setup scripts
- export PGDATABASE=cosi228_development
- export PGHOST='127.0.0.1'

### initialize database
- `cd database`
- `make reset-db`

## Setup Server
- Install npm (nvm recommended tool for npm installation)
- `cd app`
- `npm install`
- `start postgres`
- `./bin/www`
- visit localhost:3000
