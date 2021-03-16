from kumo.src.model.db import db


class MediaDirectory(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	name = db.Column(db.String(100), nullable=False, unique=True)
	path = db.Column(db.String(500), nullable=True, default="")
	root = db.Column(db.BOOLEAN, nullable=False, default=False)