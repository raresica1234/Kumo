from flask import Blueprint, render_template
import os

bp = Blueprint("explorer", __name__)


@bp.route("/")
def index():
    file_data = []
    current_path = "/WIN_D/"
    files = os.listdir(current_path)
    for file in files:
        file_type = os.path.isdir(os.path.join(current_path, file))
        file_data.append((file, file_type))

    return render_template("index.html", files=file_data, baseurl="")


@bp.route("/explorer/<path:subpath>")
def explore(subpath):
    file_data = []
    current_path = os.path.join("/WIN_D/", subpath)
    files = os.listdir(current_path)
    for file in files:
        file_type = os.path.isdir(os.path.join(current_path, file))
        file_data.append((file, file_type))

    return render_template("index.html", file_data=file_data, baseurl=subpath)
