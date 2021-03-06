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
- export PGUSER=cosi228_development

### initialize database
- `cd database`
- `source venv/bin/activate` (if using virtualenv)
- `pip install -r requirements.txt` (to make sure you have all dependencies)
- `make reset-db`

## Setup Server (new)
## (ignore original server setup if you're using new instructions)
- `cd app`
- `npm install`
- `start postgres`
- `node node-server.js`
- `visit localhost:8000`

## Setup Server (original)
- Install npm (nvm recommended tool for npm installation)
- `cd app`
- `npm install`
- `start postgres`
- `./bin/www`
- visit localhost:3000
