from flask import Blueprint, render_template
import os

bp = Blueprint("explorer", __name__)


@bp.route("/")
def index():
    files = os.listdir("/WIN_D")
    return render_template("index.html", files=files, baseurl="")


@bp.route("/explorer/<path:subpath>")
def explore(subpath):
    files = os.listdir(os.path.join("/WIN_D/", subpath))
    print(subpath)

    return render_template("index.html", files=files, baseurl=subpath)
