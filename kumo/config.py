from os import path
from flask import current_app, json

media_directories = []


def init_config(app):
    with open(path.join(app.instance_path, "config.json")) as f:
        data = json.load(f)
        if "media" in data:
            global media_directories
            media_directories = data["media"]

