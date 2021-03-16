import click
from flask_sqlalchemy import SQLAlchemy
from flask.cli import with_appcontext

db = SQLAlchemy()


class User(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(50), nullable=False, unique=True)
	password = db.Column(db.String(50), nullable=False)
	admin = db.Column(db.BOOLEAN, default=False)


class MediaDirectory(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.String(100), nullable=False, unique=True)
	path = db.Column(db.String(500), nullable=True, default="")
	root = db.Column(db.BOOLEAN, nullable=False, default=False)


class MediaPermissions(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	user_id = db.Column(db.Integer, db.ForeignKey("user.id"))
	media_id = db.Column(db.Integer, db.ForeignKey("media_directory.id"))
	permission_read = db.Column(db.BOOLEAN, default=False)
	permission_write = db.Column(db.BOOLEAN, default=False)


@click.command("init-db")
@with_appcontext
def init_db_command():
	"""Clear existing data and create new tables"""
	# db.drop_all()
	db.create_all()
	click.echo("Initialized the database.")
