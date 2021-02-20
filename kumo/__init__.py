import os
from datetime import datetime
from flask import Flask, render_template, g, redirect, url_for
from kumo import auth, explorer, config, get_resource
from kumo.models import db, init_db_command
from kumo.media_directories import update_media_command
from kumo.config import init_config

import logging


def create_app(test_config=None):
	app = Flask(__name__, instance_relative_config=True)
	app.config.from_mapping(
		SQLALCHEMY_TRACK_MODIFICATIONS=False,
		SQLALCHEMY_DATABASE_URI="sqlite:///" + os.path.join(app.instance_path, "kumo.sqlite")
	)

	try:
		os.makedirs(app.instance_path)
	except OSError:
		pass

	if os.environ.get("KUMO_FILE_LOGGING") == 1:
		logging.basicConfig(filename="log/" + datetime.now().strftime("%Y-%m-%d_%H-%M-%S") + ".txt",
							level=logging.DEBUG)
	else:
		logging.basicConfig(level=logging.DEBUG)
	app.logger.debug("Initializing config")
	init_config(app)

	app.secret_key = config.secret_key

	db.init_app(app)
	app.logger.debug("Initialized database")

	app.cli.add_command(init_db_command)
	app.logger.debug("Added db-init command")

	app.cli.add_command(update_media_command)
	app.logger.debug("Added update-media command")

	app.register_blueprint(auth.bp)
	app.logger.debug("Registered auth blueprint")

	app.register_blueprint(explorer.bp)
	app.logger.debug("Registered explorer blueprint")

	app.register_blueprint(get_resource.bp)
	app.logger.debug("Registered get resource blueprint")

	app.add_url_rule("/", endpoint="index")

	@app.route("/")
	def index():
		if g.user is None:
			return redirect(url_for("auth.login"))
		return redirect(url_for('explorer.explore'))

	return app
