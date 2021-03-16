from kumo.src.model.db import db


class User(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	username = db.Column(db.String(50), nullable=False, unique=True)
	password = db.Column(db.String(50), nullable=False)
	admin = db.Column(db.BOOLEAN, default=False)
