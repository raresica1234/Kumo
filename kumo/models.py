import click
from flask_sqlalchemy import SQLAlchemy
from flask.cli import with_appcontext

db = SQLAlchemy()


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(50), nullable=False, unique=True)
    password = db.Column(db.String(50), nullable=False)


@click.command("init-db")
@with_appcontext
def init_db_command():
    """Clear existing data and create new tables"""
    db.create_all()
    click.echo("Initialized the database.")
