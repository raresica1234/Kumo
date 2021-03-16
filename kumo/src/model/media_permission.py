from kumo.src.model.db import db


class MediaPermission(db.Model):
	id = db.Column(db.Integer, primary_key=True)
	user_id = db.Column(db.Integer, db.ForeignKey("user.id"))
	media_id = db.Column(db.Integer, db.ForeignKey("media_directory.id"))
	permission_read = db.Column(db.BOOLEAN, default=False)
	permission_write = db.Column(db.BOOLEAN, default=False)