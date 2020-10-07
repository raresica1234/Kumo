import os
from flask import Flask, g
from . import auth, explorer
from kumo.models import db, init_db_command


def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY="dev",
        SQLALCHEMY_TRACK_MODIFICATIONS=False,
        SQLALCHEMY_DATABASE_URI="sqlite:///" + os.path.join(app.instance_path, "kumo.sqlite")
    )

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass
    db.init_app(app)

    app.cli.add_command(init_db_command)

    app.register_blueprint(auth.bp)
    app.register_blueprint(explorer.bp)
    app.add_url_rule("/", endpoint="index")

    return app
