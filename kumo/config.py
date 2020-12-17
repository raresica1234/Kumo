from os import path
import json
import secrets

media_directories = []
secret_key = ""


def init_config(app):
    with open(path.join(app.instance_path, "config.json"), "r") as f:
        data = json.load(f)
        if "media" in data:
            global media_directories
            media_directories = data["media"]
        global secret_key
        if "secret_key" not in data:
            secret_key = secrets.token_urlsafe(16)
            data["secret_key"] = secret_key
        else:
            secret_key = data["secret_key"]
    with open(path.join(app.instance_path, "config.json"), "w") as f:
        json.dump(data, f, indent="\t")
