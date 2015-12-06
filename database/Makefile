create-db:
	createdb $(PGDATABASE)

initialize-db:
	psql --set "SOURCE='$(PWD)/data/schedule.json'" -f load/schedule.sql
	for year in `ls data/enrollment`; do	\
		psql --set "YEAR='$$(basename $$year .csv)'" --set "SOURCE='$(PWD)/data/enrollment/$$year'" -f load/enrollment.sql;	\
	done
	psql --set "SOURCE='$(PWD)/data/degrees.csv'" -f load/degrees.sql
	psql --set "SOURCE='$(PWD)/data/training/easy2.csv'" -f load/training.sql
	python load/prereq.py

delete-db:
	dropdb $(PGDATABASE)

tmp:
	psql --set "SOURCE='$(PWD)/data/training/josh.csv'" -f load/training.sql

reset-db: delete-db create-db initialize-db
