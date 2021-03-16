from flask.cli import with_appcontext
import click
from flask_sqlalchemy import SQLAlchemy
db = SQLAlchemy()


@click.command("init-db")
@with_appcontext
def init_db_command():
	"""Clear existing data and create new tables"""
	# db.drop_all()
	db.create_all()
	click.echo("Initialized the database.")
