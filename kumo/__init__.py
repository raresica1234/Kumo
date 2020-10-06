import os
from flask import Flask, render_template
from . import db, auth, explorer


def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        SECRET_KEY="dev",
        DATABASE=os.path.join(app.instance_path, "kumo.sqlite")
    )

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass

    db.init_app(app)
    app.register_blueprint(auth.bp)
    app.register_blueprint(explorer.bp)
    app.add_url_rule("/", endpoint="index")

    return app
